package com.xiaotian.service;

import com.xiaotian.mapper.ItemsMapper;
import com.xiaotian.pojo.Items;
import com.xiaotian.pojo.ItemsImg;
import com.xiaotian.pojo.ItemsParam;
import com.xiaotian.pojo.ItemsSpec;
import com.xiaotian.pojo.vo.NewItemsVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author xiaotian
 * @description
 * @date 2021-12-09 18:51
 */
public interface ItemService {

    /**
     * 查询最新创建的6条记录
     * @param rootCatId
     * @return
     */
    List<NewItemsVO> querySixNewItems(Integer rootCatId);

    /**
     * 查询商品信息接口，一分为四
     * @param itemId
     * @return
     */
    Items queryItem(String itemId);

    /**
     * 查询商品信息接口，一分为四
     * @param itemId
     * @return
     */
    List<ItemsImg> queryItemImgs(String itemId);

    /**
     * 查询商品信息接口，一分为四
     * @param itemId
     * @return
     */
    List<ItemsSpec> queryItemSpec(String itemId);

    /**
     * 查询商品信息接口，一分为四
     * @param itemId
     * @return
     */
    ItemsParam queryItemParam(String itemId);

}
