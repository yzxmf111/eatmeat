package com.xiaotian.controller;

import com.xiaotian.pojo.vo.ItemInfoVO;
import com.xiaotian.service.ItemService;
import com.xiaotian.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
