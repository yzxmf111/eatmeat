package com.xiaotian.controller;

import com.xiaotian.enums.ErrorEnum;
import com.xiaotian.enums.YesOrNo;
import com.xiaotian.pojo.Carousel;
import com.xiaotian.pojo.Category;
import com.xiaotian.pojo.bo.ShopCatBO;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

@Api(value = "主页接口", tags = {"主页面显示的相关接口"})
@RestController
@RequestMapping("shopcart")
public class ShopCatController {


    @ApiOperation(value = "同步cookie中购物车信息到数据库redis", notes = "同步cookie中购物车信息到数据库redis", httpMethod = "POST")
    @GetMapping("add")
    public Response syncShopCatFromCookie(@RequestParam String userId,
                                          @RequestBody List<ShopCatBO> shopCatBOS,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        if (StringUtils.isEmpty(userId)) {
            return Response.errorMsg(ErrorEnum.PARAM_ERROR.desc);
        }

        // TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存
        return Response.ok();

    }


    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    @PostMapping("/del")
    public Response del(
            @RequestParam String userId,
            @RequestParam String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (org.apache.commons.lang3.StringUtils.isBlank(userId) || org.apache.commons.lang3.StringUtils.isBlank(itemSpecId)) {
            return Response.errorMsg("参数不能为空");
        }

        // TODO 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除后端购物车中的商品

        return Response.ok();
    }

}



