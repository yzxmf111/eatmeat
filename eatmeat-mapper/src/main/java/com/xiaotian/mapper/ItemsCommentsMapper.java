package com.xiaotian.mapper;

import com.xiaotian.my.mapper.MyMapper;
import com.xiaotian.pojo.ItemsComments;
import com.xiaotian.pojo.vo.CommentCountVO;
import com.xiaotian.pojo.vo.CommentPageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapper extends MyMapper<ItemsComments> {


    /**
     * 根据itemId和level查询对应的评论
     * @param map
     * @return
     */
    List<CommentPageVO> commentsPageVo(@Param("paramsMap") Map<String,Object> map);
}