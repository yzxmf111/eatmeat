package com.xiaotian.demo;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * @description:
 * @author: yifan.tian
 * @date: 2021-12-03 16:38
 **/
class TaskWithResult implements Callable<String> {
    private int id;

    public TaskWithResult(int id) {
        this.id = id;
    }

    @Override
    public String call() throws Exception {
        return "result of TaskWithResult " + id;
    }
}

