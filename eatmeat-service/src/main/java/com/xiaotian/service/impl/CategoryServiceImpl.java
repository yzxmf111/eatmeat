package com.xiaotian.service.impl;

import com.xiaotian.mapper.CategoryMapper;
import com.xiaotian.pojo.Category;
import com.xiaotian.pojo.vo.CategoryVO;
import com.xiaotian.service.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> queryAllMainCategory() {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type",1);
        return categoryMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> queryOtherCategory(Integer fatherId) {
        return categoryMapper.queryOtherCategory(fatherId);
    }
}
