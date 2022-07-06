package com.xiaotian.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaotian.pojo.User;
import com.xiaotian.pojo.bo.ShopCatBO;
import com.xiaotian.pojo.bo.UserBO;
import com.xiaotian.service.UserService;
import com.xiaotian.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public Response usernameIsExist(@RequestParam String username) {

        // 1. 判断用户名不能为空
        if (StringUtils.isBlank(username)) {
            return Response.errorMsg("用户名不能为空");
        }

        // 2. 查找注册的用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return Response.errorMsg("用户名已经存在");
        }

        // 3. 请求成功，用户名没有重复
        return Response.ok();
    }

    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public Response regist(@RequestBody UserBO userBO,
                           HttpServletRequest request,
                           HttpServletResponse response) {

        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPwd = userBO.getConfirmPassword();

        // 0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(confirmPwd)) {
            return Response.errorMsg("用户名或密码不能为空");
        }

        // 1. 查询用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return Response.errorMsg("用户名已经存在");
        }

        // 2. 密码长度不能少于6位
        if (password.length() < 6) {
            return Response.errorMsg("密码长度不能少于6");
        }

        // 3. 判断两次密码是否一致
        if (!password.equals(confirmPwd)) {
            return Response.errorMsg("两次密码输入不一致");
        }

        // 4. 实现注册
        User userResult = userService.createUser(userBO);

        userResult = setNullProperty(userResult);

        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);

        // TODO 生成用户token，存入redis会话(分布式的会话)
        // TODO 同步购物车数据（也是到redis）
        synCookieAndRedisData(userResult.getId(), request, response);
        return Response.ok();
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public Response login(@RequestBody UserBO userBO,
                          HttpServletRequest request,
                          HttpServletResponse response) throws Exception {

        String username = userBO.getUsername();
        String password = userBO.getPassword();

        // 0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            return Response.errorMsg("用户名或密码不能为空");
        }

        // 1. 实现登录
        User userResult = userService.queryUserForLogin(username,
                MD5Utils.getMD5Str(password));

        if (userResult == null) {
            return Response.errorMsg("用户名或密码不正确");
        }

        userResult = setNullProperty(userResult);

        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);


        // TODO 生成用户token，存入redis会话
        // TODO 同步购物车数据 （多台电脑之间的数据同步）
        synCookieAndRedisData(userResult.getId(), request, response);
        return Response.ok(userResult);
    }

    private User setNullProperty(User userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }

    /**
     * 同步cookie和redis的数据
     *
     * @param userId
     * @param request
     * @param response
     */
    private void synCookieAndRedisData(String userId, HttpServletRequest request, HttpServletResponse response){
        String shopcatFromRedis = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        String shopcatFromCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);
        List<ShopCatBO> shopCatBOFromRedis = new ArrayList<>();
        List<ShopCatBO> shopCatBOFromCookie = new ArrayList<>();
        if (StringUtils.isNotBlank(shopcatFromRedis)) {
            shopCatBOFromRedis = JSONObject.parseArray(shopcatFromRedis, ShopCatBO.class);
        }
        if (StringUtils.isNotBlank(shopcatFromCookie)) {
            shopCatBOFromCookie = JSONObject.parseArray(shopcatFromCookie, ShopCatBO.class);
        }
        /**
         * redis 中购物车数据为空，cookie也为空，不做处理
         *                       cookie不为空，cookie放入redis
         * redis 中购物车数据不为空，cookie为空，放入cookie
         *                         cookie不为空，且cookie中商品在redis中存在的，以cookie为主，覆盖掉该商品
         *                                      剩余不存在，直接加上
         *redis覆盖cookie
         */
        if (CollectionUtils.isEmpty(shopCatBOFromRedis)) {
            if (!CollectionUtils.isEmpty(shopCatBOFromCookie)) {
                shopCatBOFromRedis.addAll(shopCatBOFromCookie);
            }
        } else {
            List<ShopCatBO> pendingRemovedCat = new ArrayList<>();
            List<ShopCatBO> pendingAddCat = new ArrayList<>();
            if (!CollectionUtils.isEmpty(shopCatBOFromCookie)) {
                for (ShopCatBO shopCatRedis: shopCatBOFromRedis) {
                    for (ShopCatBO shopCatCookie : shopCatBOFromCookie) {
                        if (StringUtils.equals(shopCatCookie.getSpecId(), shopCatRedis.getSpecId())) {
                            pendingAddCat.add(shopCatCookie);
                            pendingRemovedCat.add(shopCatRedis);
                        }
                    }
                }
                shopCatBOFromRedis.removeAll(pendingRemovedCat);
                shopCatBOFromRedis.addAll(pendingAddCat);
            }
        }
        redisOperator.set(FOODIE_SHOPCART + ":" + userId, JSONObject.toJSONString(shopCatBOFromRedis));
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JSONObject.toJSONString(shopCatBOFromRedis), true);
    }


    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public Response logout(@RequestParam String userId,
                           HttpServletRequest request,
                           HttpServletResponse response) {

        // 清除用户的相关信息的cookie
        CookieUtils.deleteCookie(request, response, "user");

        // TODO 用户退出登录，需要清空cookie购物车
        CookieUtils.deleteCookie(request, response, FOODIE_SHOPCART + ":" + userId);
        // TODO 分布式会话中需要清除用户数据

        return Response.ok();
    }

}
