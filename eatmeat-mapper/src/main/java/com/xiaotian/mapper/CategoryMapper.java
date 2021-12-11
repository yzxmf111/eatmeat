package com.xiaotian.mapper;

import com.xiaotian.my.mapper.MyMapper;
import com.xiaotian.pojo.Category;
import com.xiaotian.pojo.vo.CategoryVo;
import io.swagger.models.auth.In;

import java.util.List;

public interface CategoryMapper extends MyMapper<Category> {

    /**
     * 根据一级分类id查找其他级分类
     *
     * @param fatherId
     * @return
     */
    List<CategoryVo> queryOtherCategory(Integer fatherId);
    
}