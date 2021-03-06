package com.xiaotian.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaotian.enums.YesOrNo;
import com.xiaotian.pojo.Carousel;
import com.xiaotian.pojo.Category;
import com.xiaotian.pojo.vo.CategoryVO;
import com.xiaotian.pojo.vo.NewItemsVO;
import com.xiaotian.service.CarouselService;
import com.xiaotian.service.CategoryService;
import com.xiaotian.service.ItemService;
import com.xiaotian.utils.RedisOperator;
import com.xiaotian.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(value = "主页接口", tags = {"主页面显示的相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "查询所有的轮播图", notes = "查询所有的轮播图", httpMethod = "GET")
    @GetMapping("carousel")
    public Response queryAllCarouse() {
        String carousels = redisOperator.get("carousels");
        List<Carousel> carouselList = null;
        if (StringUtils.isBlank(carousels)) {
            carouselList = carouselService.queryAllCarousel(YesOrNo.YES.type);
            carousels = JSONObject.toJSONString(carouselList);
            //过期时间一天，实际场景中有很多时候需要根据需求设置不同的过期时间
            //todo:思考相应的缓存刷新机制（很多轮播图都是广告，是有时限的）
            //1.运营后台刷新并放置新的首页轮播图 2.每天定时刷新 3.自己设置过期时间
            redisOperator.set("carousels", carousels, 86400);
        } else {
            carouselList = JSONArray.parseArray(carousels, Carousel.class);
        }
        return Response.ok(carouselList);
    }

    @ApiOperation(value = "查询所有商品大分类", notes = "查询所有商品大分类", httpMethod = "GET")
    @GetMapping("cats")
    public Response queryAllMainCategory() {
        String cats = redisOperator.get("cats");
        List<Category> categoryList = new ArrayList<>();
        if (StringUtils.isBlank(cats)) {
            categoryList = categoryService.queryAllMainCategory();
            cats = JSONObject.toJSONString(categoryList);
            redisOperator.set("cats", cats);
        } else {
            categoryList = JSONArray.parseArray(cats, Category.class);
        }
        return Response.ok(categoryList);
    }

    @ApiOperation(value = "查询所有商品大分类下的子分类", notes = "查询所有商品大分类下的子分类", httpMethod = "GET")
    @GetMapping("subCat/{fatherId}")
    public Response queryOtherCategory(
            @ApiParam(name = "一级分类id", value = "一级分类id", required = true)
            @PathVariable Integer fatherId) {
        if (fatherId == null) {
            return Response.errorMsg("分类不存在");
        }
        String subCat = redisOperator.get("subCat:" + fatherId);
        List<CategoryVO> categoryVOList = null;
        if (StringUtils.isBlank(subCat)) {
            categoryVOList = categoryService.queryOtherCategory(fatherId);
            subCat = JSONObject.toJSONString(categoryVOList);
            redisOperator.set("subCat:" + fatherId, subCat);
        } else {
            categoryVOList = JSONArray.parseArray(subCat, CategoryVO.class);
        }
        return Response.ok(categoryVOList);
    }


    @ApiOperation(value = "查询所有商品大分类下最新创建的6个商品", notes = "查询所有商品大分类下最新创建的6个商品", httpMethod = "GET")
    @GetMapping("sixNewItems/{fatherId}")
    public Response querySixNewItems(
            @ApiParam(name = "一级分类id", value = "一级分类id", required = true)
            @PathVariable Integer fatherId) {
        if (fatherId == null) {
            return Response.errorMsg("分类不存在");
        }
        List<NewItemsVO> newItemsVOS = itemService.querySixNewItems(fatherId);
        return Response.ok(newItemsVOS);
    }



}



