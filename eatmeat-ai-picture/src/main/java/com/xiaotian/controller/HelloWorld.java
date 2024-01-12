package com.xiaotian.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: yifan.tian
 * @date: 2021-10-02 13:05
 **/
@RestController
public class HelloWorld {

    @GetMapping("hello")
    public Object helloWorld() {
        return "hello Elasticsearch";
    }
}
