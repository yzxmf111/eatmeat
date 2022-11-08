package com.xiaotian.service.impl;

import com.xiaotian.pojo.ItemSearch;
import com.xiaotian.pojo.Stu;
import com.xiaotian.service.ElasticSearchService;
import com.xiaotian.utils.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiaotian
 * @description
 * @date 2022-11-03 10:34
 */
@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public PageResult itemsQueryPages(Integer page, Integer pageSize, String keywords, String sort) {
        Pageable pageable = PageRequest.of(page,pageSize);
        String preTags = "<font color = 'red'>";
        String postTags = "</font>";
        //排序的text（文本）字段需要用用keyword属性
        SortBuilder sortMethod = null;
        if (StringUtils.equals(sort, "")) {
            sortMethod  = new FieldSortBuilder("").order(SortOrder.ASC);
        } else if (StringUtils.equals(sort, "1")) {
            sortMethod  = new FieldSortBuilder("").order(SortOrder.ASC);
        } else {
            sortMethod  = new FieldSortBuilder("").order(SortOrder.ASC);
        }
        //SortBuilder sortBuilderTwo = new FieldSortBuilder("age").order(SortOrder.DESC);
        String searchField = "itemName";
        SearchQuery query = new NativeSearchQueryBuilder()
                //一些api操作在这里都可以实现
                .withQuery(QueryBuilders.matchQuery(searchField, keywords))
                //设置高亮
                .withHighlightFields(new HighlightBuilder.Field(searchField)
                        .preTags(preTags)
                        .postTags(postTags))
                //设置排序
                .withSort(sortMethod)
                //.withSort(sortBuilderTwo)
                //设置分页
                .withPageable(pageable)
                .build();
//        包含了分页信息的对象
        AggregatedPage<ItemSearch> aggregatedPage = esTemplate.queryForPage(query, ItemSearch.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<ItemSearch> itemSearchList = new ArrayList<>();
                SearchHits hits = response.getHits();
                for (SearchHit hit : hits) {
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    HighlightField highlightField = highlightFields.get(searchField);
                    String highlightdesc = highlightField.getFragments()[0].toString();
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                    ItemSearch itemSearch = new ItemSearch();
                    itemSearch.setItemId((String) sourceAsMap.get("itemId"));
                    itemSearch.setItemName(highlightdesc);
                    itemSearch.setImageUrl((String)sourceAsMap.get("imageUrl"));
                    itemSearch.setPrice((Integer) sourceAsMap.get("price"));
                    itemSearch.setSellCounts((Integer) sourceAsMap.get("sellCount"));
                    itemSearchList.add(itemSearch);
                }
                return new AggregatedPageImpl(itemSearchList, pageable, hits.getTotalHits());
            }
        });
        PageResult pageResult = new PageResult();
        pageResult.setPage(page);
        pageResult.setRecords(aggregatedPage.getTotalElements());
        pageResult.setRows(aggregatedPage.getContent());
        pageResult.setTotal(aggregatedPage.getTotalPages());
        return pageResult;
    }
}
