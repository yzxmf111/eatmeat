package com.xiaotian.service;

import com.xiaotian.pojo.vo.CommentPageVO;
import com.xiaotian.utils.PageResult;

import java.util.List;

/**
 * @author xiaotian
 * @description
 * @date 2021-12-12 21:02
 */
public interface CommentService {

    /**
     * 根据评论等级查询相关评论的个数
     *
     * @param itemId
     * @return
     */
    Integer queryCommentCountByLevel(String itemId, Integer level);

    /**
     * 分页查询对应的评论
     *
     * @param itemId
     * @param level
     * @return
     */
    PageResult commentsPageVo(Integer pageNum, Integer pageSize, String itemId, Integer level);

}
