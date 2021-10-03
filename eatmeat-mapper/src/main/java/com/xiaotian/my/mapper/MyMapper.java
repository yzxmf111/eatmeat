package com.xiaotian.my.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @description:继承自己的MyMapper
 * @author: yifan.tian
 * @date: 2021-10-03 00:58
 **/

public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
