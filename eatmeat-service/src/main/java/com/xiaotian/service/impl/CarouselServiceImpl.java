package com.xiaotian.service.impl;

import com.xiaotian.mapper.CarouselMapper;
import com.xiaotian.pojo.Carousel;
import com.xiaotian.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @description: 轮播图接口
 * @author: yifan.tian
 * @date: 2021-12-03 10:49
 **/
@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;
    /**
     * 查询所有的轮播图
     *
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Carousel> queryAllCarousel(Integer isShow) {
        Example example = new Example(Carousel.class);
        example.orderBy("sort").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isShow", isShow);
        carouselMapper.selectByExample(example);
        return carouselMapper.selectByExample(example);
    }


}
