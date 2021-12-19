package com.xiaotian.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaotian.mapper.*;
import com.xiaotian.pojo.*;
import com.xiaotian.pojo.vo.NewItemsVO;
import com.xiaotian.pojo.vo.QueryItemsVo;
import com.xiaotian.pojo.vo.ShopCatVO;
import com.xiaotian.service.ItemService;
import com.xiaotian.utils.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.xml.ws.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Example example = new Example(Items.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", itemId);
        return itemsMapper.selectOneByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgs(String itemId) {
        Example example = new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsImgMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpec(String itemId) {
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsSpecMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParam(String itemId) {
        Example example = new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsParamMapper.selectOneByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PageResult itemsQueryPages(Integer pageNum, Integer pageSize, String keyword, String sort) {
        PageHelper.startPage(pageNum, pageSize);
        Map<String, Object> map = new HashMap<>(2);
        map.put("keyword", keyword);
        map.put("sort", sort);
        List<QueryItemsVo> list = itemsMapper.itemsQueryPages(map);
        PageInfo<QueryItemsVo> info = new PageInfo<>(list);
        return new PageResult(pageNum, info.getPages(), info.getTotal(), list);
    }

    @Override
    public PageResult queryItemsByCat(Integer pageNum, Integer pageSize, String catId, String sort) {
        PageHelper.startPage(pageNum, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("catId", catId);
        map.put("sort", sort);
        List<QueryItemsVo> list = itemsMapper.queryItemsByCat(map);
        PageInfo<Object> info = new PageInfo<>();
        PageResult pageResult = new PageResult(pageNum, info.getPages(), info.getTotal(), list);
        return pageResult;
    }

    @Override
    public List<ShopCatVO> refresh(List<String> specIds) {
        return itemsMapper.refresh(specIds);
    }
}
