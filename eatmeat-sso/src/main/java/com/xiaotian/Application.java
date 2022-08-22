package com.xiaotian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author xiaotian
 * @description
 * @date 2022-08-15 17:35
 */
@SpringBootApplication
@MapperScan(basePackages = "com.xiaotian.mapper")
@ComponentScan(basePackages = {"com.xiaotian", "org.n3r.idworker"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
