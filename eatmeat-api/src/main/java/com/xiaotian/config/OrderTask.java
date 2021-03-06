package com.xiaotian.config;

import com.xiaotian.service.OrderTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @description:
 * @author: Tian
 * @time: 2022/2/9 23:29
 */

@Configuration
public class OrderTask {

    @Autowired
    private OrderTaskService orderTaskService;

    /**
     * 使用定时任务关闭超期未支付订单，会存在的弊端
     * 1.会有时间差，程序不严谨
     * 10：39下单，11：00检查不足1小时，12：00检查，超时1小时多余39分钟
     * 2，不支持集群()
     * 单机没毛病，使用集群后，就会有多个定时任务
     * 解决方案：只使用一台计算机节点，单独用来运行所有的定时任务
     * 3.会对数据库全表搜索，及其影响数据库性能； select * from  order where orderStatus = 10;
     * 定时任务，仅仅只使用于小型轻量级项目，传统项目
     *
     * 解决办法:
     * 使用消息队列：MQ->RabbitMQ，RocketMQ，Kafka，ZeroMQ
     * 延时任务（队列）
     * 10：12分下单，未付款（10）状态，11：12分检查，如果当前状态还是10，则直接关闭订单
     */
    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void closeOrderTask() {
        orderTaskService.closeOrder();
    }

}
