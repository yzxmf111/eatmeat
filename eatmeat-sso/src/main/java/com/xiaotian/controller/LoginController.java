package com.xiaotian.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaotian.pojo.User;
import com.xiaotian.pojo.vo.UserVO;
import com.xiaotian.service.UserService;
import com.xiaotian.utils.CookieUtils;
import com.xiaotian.utils.MD5Utils;
import com.xiaotian.utils.RedisOperator;
import com.xiaotian.utils.Response;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

/**
 * @author xiaotian
 * @description
 * @date 2022-08-15 17:34
 */
@Controller
@RequestMapping("/sso")
public class LoginController {

    public static final String COOKIE_USER_TICKET = "cookie_user_ticket";
    public static final String REDIS_USER_TICKET = "redis_user_ticket";
    public static final String USER_TOKEN = "user_token";
    public static final String USER_TEMP_TICKET = "user_temp_ticket";

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/login")
    public String login(HttpServletRequest request,
                        HttpServletResponse Response,
                        Model model,
                        String returnUrl
    ) {

        // 1. 从cookie中获取userTicket门票，如果cookie中能够获取到，进行校验并通过了
        // 证明用户登录过，此时签发一个一次性的临时票据并且回跳
        String userTicket = getCookie(request, COOKIE_USER_TICKET);
        Boolean res = verifyUserTicket(userTicket);
        //获取临时票据并回跳
        if (res) {
            String tmpTicket = createTicket();
            return "redirect:" + returnUrl + "?tmpTicket=" + tmpTicket;
        }

        // 2. 用户从未登录过，第一次进入则跳转到CAS的统一登录页面
        return "login";
    }

    private Boolean verifyUserTicket(String userTicket) {
        if (StringUtils.isBlank(userTicket)) {
            return false;
        }
        String userId = redisOperator.get(REDIS_USER_TICKET + ":" + userTicket);
        if (StringUtils.isAnyBlank(userId)) {
            //model.addAttribute("errmsg", "用户票据异常");
            //return "login";
            return false;
        }
        String userInfo = redisOperator.get(USER_TOKEN + ":" + userId);
        if (StringUtils.isBlank(userInfo)) {
            return false;
        }
        return true;
    }

    /**
     * CAS的统一登录接口
     * 目的：
     * 1. 登录后创建用户的全局会话                 ->  uniqueToken
     * 2. 创建用户全局门票，用以表示在CAS端是否登录  ->  userTicket
     * 3. 创建用户的临时票据，用于回跳回传          ->  tmpTicket
     */
    @PostMapping("/dologin")
    public String doLogin(HttpServletRequest request,
                          HttpServletResponse response,
                          String userName,
                          String passward,
                          String returnUrl,
                          Model model) {
        //1.验证用户是否存在
        if (StringUtils.isAnyBlank(userName, passward)) {
            model.addAttribute("errmsg", "用户名或密码不能为空");
            return "login";
        }
        User userResult = null;
        try {
            userResult = userService.queryUserForLogin(userName, MD5Utils.getMD5Str(passward));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userResult == null) {
            model.addAttribute("errmsg", "用户名或密码错误");
            return "login";
        }
        //2.创建全局会话
        String token = UUID.randomUUID().toString().trim();
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userResult, userVO);
        userVO.setUserUniqueToken(token);
        redisOperator.set(USER_TOKEN + ":" + userVO.getId(), JSONObject.toJSONString(userVO), 7 * 60 * 60 * 24);
        // 3. 生成ticket门票，全局门票，代表用户在CAS端登录过,之后设置在cookie中，不用再登陆了
        String userTicket = UUID.randomUUID().toString().trim();
        // 3.1 用户全局门票需要放入CAS端的cookie中
        setCookie(COOKIE_USER_TICKET, userTicket, response);
        //4. userTicket关联用户id，并且放入到redis中，代表这个用户有门票了，可以在各个景区游玩
        redisOperator.set(REDIS_USER_TICKET + ":" + userTicket, userVO.getId());
        //4. 生成临时票据，回跳到调用端网站，是由CAS端所签发的一个一次性的临时ticket
        String tmpTicket = createTicket();
        /**
         * userTicket: 用于表示用户在CAS端的一个登录状态：已经登录
         * tmpTicket: 用于颁发给用户进行一次性的验证的票据，有时效性
         */

        /**
         * 举例：
         *      我们去动物园玩耍，大门口买了一张统一的门票，这个就是CAS系统的全局门票和用户全局会话。
         *      动物园里有一些小的景点，需要凭你的门票去领取一次性的票据，有了这张票据以后就能去一些小的景点游玩了。
         *      这样的一个个的小景点其实就是我们这里所对应的一个个的站点。
         *      当我们使用完毕这张临时票据以后，就需要销毁。
         */

        return "redirect:" + returnUrl + "?tmpTicket=" + tmpTicket;
    }

    @PostMapping("/verifyTmpTicket")
    @ResponseBody
    public Response verifyTmpTicket(String tmpTicket,
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    Model model) throws Exception {
        if (StringUtils.isBlank(tmpTicket)) {
            //model.addAttribute("errmsg", "用户票据异常");
            return Response.errorMsg("用户票据异常");
        }
        String tmpTicketFromRedis = redisOperator.get(USER_TEMP_TICKET + ":" + tmpTicket);
        if (StringUtils.isBlank(tmpTicketFromRedis)) {
            //model.addAttribute("errmsg", "用户票据异常");
            //return "login";
            return Response.errorMsg("用户票据异常");
        }
        if (!StringUtils.equals(MD5Utils.getMD5Str(tmpTicketFromRedis), tmpTicket)) {
            //model.addAttribute("errmsg", "用户票据异常");
            return Response.errorMsg("用户票据异常");
        } else {
            //临时票据通过校验需要销毁
            redisOperator.del(USER_TEMP_TICKET + ":" + tmpTicket);
        }
        //1.临时票据通过验证。用户会话进行回传。通过cookie逐渐获取全局token
        String userTicket = CookieUtils.getCookieValue(request, COOKIE_USER_TICKET);
        String userId = redisOperator.get(REDIS_USER_TICKET + ":" + userTicket);
        if (StringUtils.isAnyBlank(userTicket, userId)) {
            //model.addAttribute("errmsg", "用户票据异常");
            //return "login";
            return Response.errorMsg("用户票据异常");
        }
        String userJson = redisOperator.get(USER_TOKEN + ":" + userId);
        if (StringUtils.isBlank(userJson)) {
            //model.addAttribute("errmsg", "用户票据异常");
            //return "login";
            return Response.errorMsg("用户票据异常");
        }
        return Response.ok(JSONObject.parseObject(userJson, UserVO.class));
    }


    @GetMapping
    public Response logout(HttpServletRequest request,
                           HttpServletResponse response,
                           String userId) {
        //1.清除cookie
        String userTicket = getCookie(request, COOKIE_USER_TICKET);
        CookieUtils.deleteCookie(request, response, COOKIE_USER_TICKET);
        //2.清除token
        redisOperator.del(USER_TOKEN + ":" + userId);
        //3.    清除全局门票
        redisOperator.del(REDIS_USER_TICKET + ":" + userTicket);
        return Response.ok();
    }

    private void deleteCookie(String key,
                              HttpServletResponse response) {

        Cookie cookie = new Cookie(key, null);
        cookie.setDomain("sso.com");
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }

    private void setCookie(String key,
                           String val,
                           HttpServletResponse response) {

        Cookie cookie = new Cookie(key, val);
        cookie.setDomain("sso.com");
        cookie.setPath("/");
        //response.addCookie (cookie)一旦执行，服务器端会自动发回消息头set-cookie给浏览器，set-cookie是会携带cookie键值对的
        response.addCookie(cookie);
    }

    private String createTicket() {
        String tmpTicket = UUID.randomUUID().toString().trim();
        try {
            redisOperator.set(USER_TEMP_TICKET + ":" + tmpTicket,
                    MD5Utils.getMD5Str(tmpTicket), 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpTicket;

    }


    private String getCookie(HttpServletRequest request, String key) {

        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || StringUtils.isBlank(key)) {
            return null;
        }

        String cookieValue = null;
        for (int i = 0; i < cookieList.length; i++) {
            if (cookieList[i].getName().equals(key)) {
                cookieValue = cookieList[i].getValue();
                break;
            }
        }

        return cookieValue;
    }


}
