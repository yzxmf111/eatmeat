package com.xiaotian.service;

import com.xiaotian.pojo.Category;
import com.xiaotian.pojo.vo.CategoryVo;

import java.util.List;

/**
 * @author xiaotian
 * @description
 * @date 2021-12-06 19:17
 */
public interface CategoryService {

    /**
     * 查询所有的一级分类
     * @return List<Category>
     */
    List<Category> queryAllMainCategory();

    /**
     * 页面第一次加载的时候只加载所有的一级分类，在鼠标移动至改一级分类时，查询其所有的二级及三级分类
     * 之后鼠标再次移动到一级分类处不在加载（前端有缓存）
     * @param fatherId
     * @return
     */
    List<CategoryVo> queryOtherCategory(Integer fatherId);
}
