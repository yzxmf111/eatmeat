package com.xiaotian.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

/**
 * @description:
 * @author: yifan.tian
 * @date: 2021-12-03 11:36
 **/
@Component
public class ScheduleDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleDemo.class);

    @Resource(name = "executor")
    private ExecutorService executor;

    @Scheduled(fixedDelay = 1000) //每一秒运行一次
    public void testScheduling() {
//        try {
////            executor.
//            Thread.sleep(5000);
//            //以下两种方法会从线程池开新线程去执行方法
//            executor.execute(() -> LOGGER.info(Thread.currentThread().getName())
//            );
//            executor.submit(() -> LOGGER.info(Thread.currentThread().getName()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////        System.out.println(Thread.currentThread().getName());
//    }
//
//    @Scheduled(fixedDelay = 1000) //每一秒运行一次
//    public void testScheduling222() {
//        try {
//            Thread.sleep(5000);
//            executor.execute(() -> LOGGER.info(Thread.currentThread().getName())
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(Thread.currentThread().getName())
    }
}

