package com.xiaotian.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaotian.mapper.*;
import com.xiaotian.pojo.*;
import com.xiaotian.pojo.vo.CommentPageVO;
import com.xiaotian.pojo.vo.NewItemsVO;
import com.xiaotian.service.CommentService;
import com.xiaotian.service.ItemService;
import com.xiaotian.utils.DesensitizationUtil;
import com.xiaotian.utils.PageResult;
import com.xiaotian.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 轮播图接口
 * @author: yifan.tian
 * @date: 2021-12-03 10:49
 **/
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private ItemsCommentsMapper commentsMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer queryCommentCountByLevel(String itemId, Integer level) {
        Example example = new Example(ItemsComments.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        criteria.andEqualTo("commentLevel", level);
        return commentsMapper.selectCountByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PageResult commentsPageVo(Integer pageNum, Integer pageSize, String itemId, Integer level) {

        PageHelper.startPage(pageNum, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("itemId", itemId);
        map.put("level", level);
        List<CommentPageVO> list = commentsMapper.commentsPageVo(map);
        for (CommentPageVO commentPageVO : list) {
            commentPageVO.setUserNickName(DesensitizationUtil.commonDisplay(commentPageVO.getUserNickName()));
        }
        PageInfo<CommentPageVO> commentPageVOPageInfo = new PageInfo<>(list);
        return new PageResult(pageNum, commentPageVOPageInfo.getPages(), commentPageVOPageInfo.getTotal(), list);
    }
}
