package com.xiaotian.controller;

import com.xiaotian.pojo.vo.CommentCountVO;
import com.xiaotian.pojo.vo.ItemInfoVO;
import com.xiaotian.service.CommentService;
import com.xiaotian.service.ItemService;
import com.xiaotian.utils.PageResult;
import com.xiaotian.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author xiaotian
 * @description
 * @date 2021-12-11 19:01
 */
@Api(value = "商品展示相关接口", tags = {"商品展示相关接口"})
@RestController
@RequestMapping("items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "商品信息查询", notes = "商品信息查询", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public Response queryItemInfo(
            @ApiParam(name = "商品id", value = "商品id", required = true)
            @PathVariable String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return Response.errorMsg("参数为空");
        }
        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(itemService.queryItem(itemId));
        itemInfoVO.setItemImgList(itemService.queryItemImgs(itemId));
        itemInfoVO.setItemParams(itemService.queryItemParam(itemId));
        itemInfoVO.setItemSpecList(itemService.queryItemSpec(itemId));
        return Response.ok(itemInfoVO);
    }


    @ApiOperation(value = "商品评论数量统计", notes = "商品评论数量统计", httpMethod = "GET")
    @GetMapping("commentLevel")
    public Response queryItemCommentCount(
            @ApiParam(name = "商品id", value = "商品id", required = true)
            @RequestParam String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return Response.errorMsg("参数为空");
        }
        //enum
        Integer goodCount = commentService.queryCommentCountByLevel(itemId, 1);
        Integer normalCount = commentService.queryCommentCountByLevel(itemId, 2);
        Integer badCount = commentService.queryCommentCountByLevel(itemId, 3);
        Integer totalCount = goodCount + normalCount + badCount;
        CommentCountVO commentCountVO = new CommentCountVO(goodCount, normalCount, badCount, totalCount);
        return Response.ok(commentCountVO);
    }

    @ApiOperation(value = "商品评论查询", notes = "商品评论查询", httpMethod = "GET")
    @GetMapping("comments")
    public Response queryItemComment(
            @ApiParam(name = "当前页数", value = "pageNum", required = false)
            @RequestParam(required = false) Integer pageNum,
            @ApiParam(name = "当前页数评论数量", value = "pageSize", required = false)
            @RequestParam(required = false) Integer pageSize,
            @ApiParam(name = "商品id", value = "itemId", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "评论级别", value = "level", required = false)
            @RequestParam(required = false) Integer level) {
        if (StringUtils.isBlank(itemId)) {
            return Response.errorMsg("参数为空");
        }
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        //enum
        PageResult pageResult = commentService.commentsPageVo(pageNum, pageSize, itemId, level);
        return Response.ok(pageResult);
    }


    //@ApiOperation(value = "商品查询接口", notes = "商品查询接口", httpMethod = "GET")
    //@GetMapping("search")
    //public Response queryItem(
    //        @ApiParam(name = "搜素关键字", value = "keywords", required = true)
    //        @RequestParam String keywords,
    //        @ApiParam(name = "排序方式", value = "sort", required = false)
    //        @RequestParam(required = false) String sort,
    //    @ApiParam(name = "当前页数", value = "pageNum", required = false)
    //    @RequestParam(required = false) Integer page,
    //    @ApiParam(name = "当前页数商品数量", value = "pageSize", required = false)
    //    @RequestParam(required = false) Integer pageSize) {
    //    if (StringUtils.isBlank(keywords)) {
    //        return Response.errorMsg("请输入搜索内容");
    //    }
    //    if (page == null) {
    //        page = 1;
    //    }
    //    if (pageSize == null) {
    //        pageSize = 20;
    //    }
    //    //enum
    //    PageResult pageResult = itemService.itemsQueryPages(page, pageSize, keywords, sort);
    //    return Response.ok(pageResult);
    //}

    @ApiOperation(value = "商品查询接口", notes = "商品查询接口", httpMethod = "GET")
    @GetMapping("catItems")
    public Response querycatItems(
            @ApiParam(name = "分类id", value = "catId", required = true)
            @RequestParam String catId,
            @ApiParam(name = "排序方式", value = "sort", required = false)
            @RequestParam(required = false) String sort,
            @ApiParam(name = "当前页数", value = "pageNum", required = false)
            @RequestParam(required = false) Integer page,
            @ApiParam(name = "当前页数商品数量", value = "pageSize", required = false)
            @RequestParam(required = false) Integer pageSize) {
        if (StringUtils.isBlank(catId)) {
            return Response.errorMsg("");
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 20;
        }
        //enum
        PageResult pageResult = itemService.queryItemsByCat(page, pageSize, catId, sort);
        return Response.ok(pageResult);
    }

    @ApiOperation(value = "购物车商品刷新接口", notes = "购物车商品刷新接口，防止商品价格，图片，名称等出现变更", httpMethod = "GET")
    @GetMapping("refresh")
    public Response refresh(
            @ApiParam(name = "分类id", value = "itemSpecIds", required = true)
            @RequestParam String itemSpecIds) {
        if (StringUtils.isBlank(itemSpecIds)) {
            return Response.ok("");
        }
        String[] split = itemSpecIds.split(",");
        List<String> specIds = Arrays.asList(split);
    return Response.ok(itemService.refresh(specIds)) ;
    }

}
