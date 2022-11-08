package com.xiaotian.controller;

import com.xiaotian.service.ElasticSearchService;
import com.xiaotian.utils.PageResult;
import com.xiaotian.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaotian
 * @description
 * @date 2022-11-03 10:11
 */
@RestController
@RequestMapping("items")
public class ItemSearchController {

    @Autowired
    private ElasticSearchService elasticSearchService;

    @ApiOperation(value = "商品查询接口", notes = "商品查询接口", httpMethod = "GET")
    @GetMapping("search")
    public Response queryItem(
            @RequestParam String keywords,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        if (StringUtils.isBlank(keywords)) {
            return Response.errorMsg("请输入搜索内容");
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 20;
        }
        //es的页码从0开始
        page --;
        PageResult pageResult = elasticSearchService.itemsQueryPages(page, pageSize, keywords, sort);
        return Response.ok(null);
    }
}
