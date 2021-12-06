package com.xiaotian.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @description:
 * @author: yifan.tian
 * @date: 2021-12-03 15:27
 **/
@Component
@EnableScheduling
public class ScheduleConfig{

    @Bean("executor")
    public ExecutorService taskExecutor() {
        //指定线程池大小
        return new ThreadPoolExecutor(2,4,30, TimeUnit.MICROSECONDS,new LinkedBlockingQueue<>());
//        return Executors.newScheduledThreadPool(10);
    }

}
