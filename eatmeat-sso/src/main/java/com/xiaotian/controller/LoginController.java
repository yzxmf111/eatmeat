package com.xiaotian.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xiaotian
 * @description
 * @date 2022-08-15 17:34
 */
@Controller
@RequestMapping("/sso")
public class LoginController {

    @GetMapping("/login")
    public String login(HttpServletRequest request,
                        HttpServletResponse Response
                        ) {
        return "login";
    }
}
