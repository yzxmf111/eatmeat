package com.xiaotian.controller;

import com.xiaotian.mapper.SingleTableMapper;
import com.xiaotian.pojo.SingleTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author xiaotian
 * @description
 * @date 2022-09-27 11:33
 */
@RestController
@RequestMapping("/data")
public class DataInsert {

    @Autowired
    private SingleTableMapper singleTableMapper;
    @GetMapping("/insert")
    public void insertData(){
        List<SingleTable> dataList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            SingleTable singleTable = new SingleTable();
            singleTable.setKey1(random.nextInt(100000) + "key1" + System.currentTimeMillis());
            singleTable.setKey2(i);
            singleTable.setKey3(random.nextInt(100000) + "key3" + System.currentTimeMillis());
            singleTable.setKeyPart1(random.nextInt(100000) + "key_part1" + System.currentTimeMillis());
            singleTable.setKeyPart2(random.nextInt(100000) + "key_part2" + System.currentTimeMillis());
            singleTable.setKeyPart3(random.nextInt(100000) + "key_part3" + System.currentTimeMillis());
            singleTable.setCommonField(random.nextInt(100000) + "common_field" + System.currentTimeMillis());
            dataList.add(singleTable);
        }
        singleTableMapper.insertList(dataList);

    }

}
