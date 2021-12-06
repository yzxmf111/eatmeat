package com.xiaotian.service;

import com.xiaotian.pojo.Carousel;

import java.util.List;

/**
 * @description: 轮播图接口
 * @author: yifan.tian
 * @date: 2021-12-03 10:49
 **/
public interface CarouselService {

    /**
     * 查询所有的轮播图
     * @return
     */
    List<Carousel> queryAllCarousel(Integer isShow);
}
