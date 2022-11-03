package com.xiaotian.demo;

/**
 * @description:
 * @author: yifan.tian
 * @date: 2021-12-03 16:39
 **/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.*;

class CallableTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallableTest.class);
    public static void main(String[] args) throws InterruptedException,
            ExecutionException {
//        ExecutorService exec = Executors.newCachedThreadPool();
//        ArrayList<Future<String>> results = new ArrayList<Future<String>>();    //Future 相当于是用来存放Executor执行的结果的一种容器
//        for (int i = 0; i < 10; i++) {
//            results.add(exec.submit(new TaskWithResult(i)));
//        }
//        for (Future<String> fs : results) {
//            if (fs.isDone()) {
//                System.out.println(fs.get());
//            } else {
//                System.out.println("Future result is not yet complete");
//            }
//        }
//        exec.shutdown();

        ExecutorService executor = new ThreadPoolExecutor(2, 100, 30, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>());
        for (int i = 0; i < 100; i++) {

            executor.execute(() -> {
                        LOGGER.info(Thread.currentThread().getName());
                        try {
                            Thread.sleep(100000000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
//            executor.submit(() -> LOGGER.info(Thread.currentThread().getName()));

        };

    }
}
