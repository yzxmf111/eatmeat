package com.xiaotian.controller;

import com.xiaotian.enums.YesOrNo;
import com.xiaotian.pojo.Carousel;
import com.xiaotian.service.CarouselService;
import com.xiaotian.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "主页接口", tags = {"主页面显示的相关接口"})
@RestController
@RequestMapping("carousel")
public class CarouselController {

    @Autowired
    private CarouselService carouselService;

    @ApiOperation(value = "查询所有的轮播图", notes = "查询所有的轮播图", httpMethod = "GET")
    @GetMapping("/queryAllCarouse")
    public Response queryAllCarouse() {
        List<Carousel> carousels = carouselService.queryAllCarousel(YesOrNo.YES.type);
        return Response.ok(carousels);
    }
}
