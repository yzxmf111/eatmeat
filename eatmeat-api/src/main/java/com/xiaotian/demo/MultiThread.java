package com.xiaotian.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @description:
 * @author: yifan.tian
 * @date: 2021-12-03 16:26
 **/
public class MultiThread {

    private static Executor executor;

    static {
        executor = new ThreadPoolExecutor(2,4,20, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>());
    }

    public static void main(String[] args) {
        Map<String, Object> returnMap = new HashMap<>();
        Callable<List> callable = new Callable<List>() {
            @Override
            public List call() throws Exception {
                return  new ArrayList(); //去service 查询数据返回
            }
        };

        Callable<List> callable2 = new Callable<List>() {
            @Override
            public List call() throws Exception {
                return  new ArrayList(); //去service 查询数据返回2
            }
        };

        Callable<List<String>> listCallable = new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                return null;
            }
        };

        FutureTask<List> futureTask = new FutureTask<>(callable);
        FutureTask<List> futureTask2= new FutureTask<>(callable2);
        FutureTask<List<String>> listFutureTask = new FutureTask<>(listCallable);
        Thread t1 = new Thread(futureTask); //声明线程
        Thread t2 = new Thread(futureTask2);
        Thread t3 = new Thread(listFutureTask);
        t1.start(); //开始线程
        t2.start();
        t3.start();
        try {
            List list1 = futureTask.get(); //得到结果
            List list2 = futureTask2.get();
            List list3 = listFutureTask.get();
            returnMap.put("list1",list1); //将结果放入map中返回
            returnMap.put("list2",list2);
            returnMap.put("list3",list3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }finally {
            t1.interrupt(); //关闭线程
            t2.interrupt();
            t3.interrupt();
        }
        //返回  returnMap
    }

}
