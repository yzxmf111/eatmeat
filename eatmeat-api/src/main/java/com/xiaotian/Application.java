package com.xiaotian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @description:
 * @author: yifan.tian
 * @date: 2021-10-02 13:02
 **/
@SpringBootApplication
// 扫描 mybatis 通用 mapper 所在的包
@MapperScan(basePackages = "com.xiaotian.mapper")
// 扫描所有包以及相关组件包
@ComponentScan(basePackages = {"com.xiaotian", "org.n3r.idworker"})
//@EnableTransactionManagement
@EnableScheduling       // 开启定时任务
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
