package com.xiaotian.controller;

import com.xiaotian.enums.YesOrNo;
import com.xiaotian.pojo.Carousel;
import com.xiaotian.pojo.Category;
import com.xiaotian.pojo.vo.CategoryVO;
import com.xiaotian.pojo.vo.NewItemsVO;
import com.xiaotian.service.CarouselService;
import com.xiaotian.service.CategoryService;
import com.xiaotian.service.ItemService;
import com.xiaotian.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "查询所有的轮播图", notes = "查询所有的轮播图", httpMethod = "GET")
    @GetMapping("carousel")
    public Response queryAllCarouse() {
        List<Carousel> carousels = carouselService.queryAllCarousel(YesOrNo.YES.type);
        return Response.ok(carousels);
    }

    @ApiOperation(value = "查询所有商品大分类", notes = "查询所有商品大分类", httpMethod = "GET")
    @GetMapping("cats")
    public Response queryAllMainCategory() {
        List<Category> carousels = categoryService.queryAllMainCategory();
        return Response.ok(carousels);
    }

    @ApiOperation(value = "查询所有商品大分类下的子分类", notes = "查询所有商品大分类下的子分类", httpMethod = "GET")
    @GetMapping("subCat/{fatherId}")
    public Response queryOtherCategory(
            @ApiParam(name = "一级分类id", value = "一级分类id", required = true)
            @PathVariable Integer fatherId) {
        if (fatherId == null) {
            return Response.errorMsg("分类不存在");
        }
        List<CategoryVO> categoryVOS = categoryService.queryOtherCategory(fatherId);
        return Response.ok(categoryVOS);
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



