package com.xiaotian.mapper;

import com.xiaotian.my.mapper.MyMapper;
import com.xiaotian.pojo.Items;
import com.xiaotian.pojo.vo.NewItemsVO;

import java.util.List;

/**
 * @author xiaotian
 */
public interface ItemsMapper extends MyMapper<Items> {

    /**
     * 根据一级分类id查询留个最近创建的商品
     *
     * @param fatherId
     * @return
     */
    List<NewItemsVO> querySixNewItems(Integer fatherId);

}