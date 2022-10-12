package com.xiaotian.demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @description:
 * @author: Tian
 * @time: 2022/8/24 22:45
 */


public class testCountDownLatch {
    CountDownLatch countDownLatch = new CountDownLatch(100);

    private  void runThread() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for(int i=0;i<100 ;i++){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        countDownLatch.await();
                        System.out.println("Thread:"+Thread.currentThread().getName()+",time: "+System.currentTimeMillis());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        countDownLatch.countDown();



        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            final int index = i;
            new Thread(() -> {
                try {
                    Thread.sleep(1000 + ThreadLocalRandom.current().nextInt(1000));
                    System.out.println("finish" + index + Thread.currentThread().getName());
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        countDownLatch.await();// 主线程在阻塞，当计数器==0，就唤醒主线程往下执行。
        System.out.println("主线程:在所有任务运行完成后，进行结果汇总");
    }

    public static void main(String[] args) throws InterruptedException {
        testCountDownLatch test = new testCountDownLatch();
        test.runThread();
    }
}
