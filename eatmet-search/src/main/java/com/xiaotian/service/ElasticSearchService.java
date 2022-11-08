package com.xiaotian.service;

import com.xiaotian.utils.PageResult;
import org.springframework.stereotype.Service;

/**
 * @author xiaotian
 * @description
 * @date 2022-11-03 10:32
 */
public interface ElasticSearchService {

    PageResult itemsQueryPages(Integer page, Integer pageSize, String keywords, String sort);
}
