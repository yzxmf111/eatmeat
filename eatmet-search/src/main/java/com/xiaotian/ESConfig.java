package com.xiaotian;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @description:
 * @author: Tian
 * @time: 2022/10/18 23:24
 */


@Configuration
public class ESConfig {

    /**
     * 解决netty引起的issue
     */
    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

}
