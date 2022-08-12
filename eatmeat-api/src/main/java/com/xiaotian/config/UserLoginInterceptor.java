package com.xiaotian.config;

import com.alibaba.fastjson.JSONObject;
import com.xiaotian.utils.RedisOperator;
import com.xiaotian.utils.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author xiaotian
 * @description
 * @date 2022-08-11 09:21
 */
public class UserLoginInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginInterceptor.class);

    private static final String USER_TOKEN = "user_token";

    @Autowired
    private RedisOperator redisOperator;
    /**
     * 访问controller之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return false;
        }
        String userId = request.getHeader("headerUserId");
        String token = request.getHeader("headerUserToken");

        if(StringUtils.isAnyBlank(userId, token)) {
            LOGGER.error("userId = {}, token = {} 中存在空值", userId, token);
            // * 拦截器处理只能返回true or false
            //     * 使用这个方法可以将返回放在response中
            returnErrorResponse(response, Response.errorMsg("请登录"));
            return false;
        }
        String tokenFromRedis = redisOperator.get(USER_TOKEN + ":" + userId);
        if (!StringUtils.equals(tokenFromRedis, token)) {
            LOGGER.error("tokenFromRedis={}, token={}", tokenFromRedis, token);
            returnErrorResponse(response, Response.errorMsg("请登录"));
            return false;
        }
        return true;
    }

    /**
     * 拦截器处理只能返回true or false
     * 使用这个方法可以将返回放在response中
     * @param response
     * @param res
     */
    private void returnErrorResponse(HttpServletResponse response, Response res) {
        OutputStream os = null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        try {
            os = response.getOutputStream();
            os.write(JSONObject.toJSONString(res).getBytes("utf-8"));
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 访问controller之后，渲染试图之前
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 渲染试图之后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
