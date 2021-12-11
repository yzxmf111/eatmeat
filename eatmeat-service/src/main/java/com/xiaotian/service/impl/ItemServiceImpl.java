package com.xiaotian.service.impl;

import com.xiaotian.mapper.*;
import com.xiaotian.pojo.*;
import com.xiaotian.pojo.vo.CategoryVo;
import com.xiaotian.pojo.vo.NewItemsVO;
import com.xiaotian.service.CategoryService;
import com.xiaotian.service.ItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 轮播图接口
 * @author: yifan.tian
 * @date: 2021-12-03 10:49
 **/
@Service
public class ItemServiceImpl implements ItemService {

    @Resource
    private ItemsMapper itemsMapper;

    @Resource
    private ItemsSpecMapper itemsSpecMapper;

    @Resource
    private ItemsParamMapper itemsParamMapper;

    @Resource
    private ItemsImgMapper itemsImgMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<NewItemsVO> querySixNewItems(Integer rootCatId) {
        return itemsMapper.querySixNewItems(rootCatId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItem(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgs(String itemId) {
        Example example = new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        return itemsImgMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpec(String itemId) {
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        return itemsSpecMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParam(String itemId) {
        Example example = new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        return itemsParamMapper.selectOneByExample(example);
    }
}
