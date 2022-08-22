package com.xiaotian.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaotian.enums.ErrorEnum;
import com.xiaotian.pojo.bo.ShopCatBO;
import com.xiaotian.utils.CookieUtils;
import com.xiaotian.utils.RedisOperator;
import com.xiaotian.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "购物车接口", tags = {"购物车的相关接口"})
@RestController
@RequestMapping("shopcart")
public class ShopCatController extends BaseController {

    @Autowired
    private RedisOperator redisOperator;


    @ApiOperation(value = "同步cookie中购物车信息到数据库redis", notes = "同步cookie中购物车信息到数据库redis", httpMethod = "POST")
    @PostMapping("add")
    public Response syncShopCatFromCookie(@RequestParam String userId,
                                          @RequestBody ShopCatBO shopCatBO,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        if (StringUtils.isEmpty(userId)) {
            return Response.errorMsg(ErrorEnum.PARAM_ERROR.desc);
        }

        //  前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存
        String shopCatFromCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);
        String shopCatFromRedis = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        //List<ShopCatBO> shopCatBOSForCookie = JSONObject.parseArray(shopCatFromCookie, ShopCatBO.class);
        List<ShopCatBO> shopCatBOSForRedis = JSONObject.parseArray(shopCatFromRedis, ShopCatBO.class);
        Boolean flag = false;
        if (!CollectionUtils.isEmpty(shopCatBOSForRedis)) {
            for (ShopCatBO shopCat : shopCatBOSForRedis) {
                //当前商品已在缓存存在
                if (StringUtils.equals(shopCat.getSpecId(), shopCatBO.getSpecId())) {
                    shopCat.setBuyCounts(shopCat.getBuyCounts() + shopCatBO.getBuyCounts());
                    flag = true;
                }
            }
            //当前商品在缓存中不存在
            if (!flag) {
                shopCatBOSForRedis.add(shopCatBO);
            }
            redisOperator.set("shopcart:" + userId, JSONObject.toJSONString(shopCatBOSForRedis));
        } else {
            redisOperator.set("shopcart:" + userId, shopCatFromCookie);
        }
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
        String shopCatFromRedis = redisOperator.get("shopcart:" + userId);
        List<ShopCatBO> shopCatBOS = JSONObject.parseArray(shopCatFromRedis, ShopCatBO.class);
        List<ShopCatBO> collect = shopCatBOS.stream().filter(shopCatBO -> !StringUtils.equals(shopCatBO.getSpecId(), itemSpecId)).collect(Collectors.toList());
        redisOperator.set("shopcart:" + userId, JSONObject.toJSONString(collect));
        return Response.ok();
    }

}



