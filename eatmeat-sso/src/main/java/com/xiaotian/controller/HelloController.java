package com.xiaotian.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaotian
 * @description
 * @date 2022-08-18 10:23
 */

@RestController
@RequestMapping
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "hello world";
    }
}
