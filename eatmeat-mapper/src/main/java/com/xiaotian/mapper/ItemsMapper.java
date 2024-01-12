package com.xiaotian.mapper;

import com.xiaotian.my.mapper.MyMapper;
import com.xiaotian.pojo.Items;
import com.xiaotian.pojo.vo.NewItemsVO;
import com.xiaotian.pojo.vo.QueryItemsVo;
import com.xiaotian.pojo.vo.ShopCatVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    /**
     * 商品查询接口1：根据keyword查询商品
     * @param map
     * @return
     */
    List<QueryItemsVo> itemsQueryPages(@Param("paramMap") Map<String,Object> map);

    /**
     * 商品查询接口2:根据分类名查询商品
     * @param map
     * @return
     */
    List<QueryItemsVo> queryItemsByCat(@Param("paramMap") Map<String,Object> map);

    /**
     * 刷新购物车
     * @param specIds
     * @return
     */
    List<ShopCatVO> refresh(@Param("specIds") List<String> specIds);


    int decreaseItemSpecStock(@Param("specId") String specId,
                                     @Param("pendingCounts") int pendingCounts);
}