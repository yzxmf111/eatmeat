package com.xiaotian.test;

import com.xiaotian.Application;
import com.xiaotian.pojo.Stu;
import io.swagger.models.auth.In;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Tian
 * @time: 2022/10/18 23:27
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ESTest {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Test
    public void createIndex() {
        Stu stu = new Stu();
        stu.setAge(11);
        stu.setName("xiaotian");
        stu.setStuId(1001L);
        stu.setMoney(6.111);
        stu.setSex(true);
        stu.setDesc("save zhang");
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(stu).build();
        esTemplate.index(indexQuery);
    }

    @Test
    public void testDeleteIndex() {
        esTemplate.deleteIndex(Stu.class);
    }

    //    ----------------------------------------分割----------------------------------------

    //    操作文档数据
//
    @Test
    public void updataStuDocument() {
        Map<String, Object> map = new HashMap<>();
        map.put("desc", "lalalallalal");
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.source(map);
        UpdateQuery query = new UpdateQueryBuilder()
                .withIndexRequest(indexRequest)
                .withClass(Stu.class).withId("1002")
                .build();
        esTemplate.update(query);
    }

    //    删除
    @Test
    public void deleteStuDocument() {
        esTemplate.delete(Stu.class, "1002");
    }

    //    查询
    @Test
    public void queryStuDocument() {
        GetQuery getQuery = new GetQuery();
        getQuery.setId("1001");

        Stu stu = esTemplate.queryForObject(getQuery, Stu.class);
        System.out.println(stu);
    }

    //分页展示
    @Test
    public void queryStuDocumentByPage() {
         Pageable pageable = PageRequest.of(0,10);
         String preTags = "<font color = 'red'>";
         String postTags = "</font>";
        SortBuilder sortBuilderOne = new FieldSortBuilder("money").order(SortOrder.ASC);
        SortBuilder sortBuilderTwo = new FieldSortBuilder("age").order(SortOrder.DESC);

        SearchQuery query = new NativeSearchQueryBuilder().
                //一些api操作在这里都可以实现
                withQuery(QueryBuilders.matchQuery("desc", "save"))
                //设置高亮
                .withHighlightFields(new HighlightBuilder.Field("desc")
                        .preTags(preTags)
                        .postTags(postTags))
                //设置排序
                .withSort(sortBuilderOne)
                .withSort(sortBuilderTwo)
                //设置分页
                .withPageable(pageable)
                .build();
//        包含了分页信息的对象
        AggregatedPage<Stu> aggregatedPage = esTemplate.queryForPage(query, Stu.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<Stu> returnStuList = new ArrayList<>();
                SearchHits hits = response.getHits();
                for (SearchHit hit : hits) {
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    HighlightField highlightField = highlightFields.get("desc");
                    String highlightdesc = highlightField.getFragments()[0].toString();
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                    Stu stu = new Stu();
                    stu.setStuId(Long.parseLong(sourceAsMap.get("stuId").toString()));
                    stu.setName((String)sourceAsMap.get("name"));
                    stu.setSex((Boolean) sourceAsMap.get("sex"));
                    stu.setMoney((Double) sourceAsMap.get("money"));
                    stu.setAge((Integer) sourceAsMap.get("age"));
                    stu.setDesc(highlightdesc);
                    returnStuList.add(stu);
                }
                return new AggregatedPageImpl(returnStuList);
            }
        });
        int totalPages = aggregatedPage.getTotalPages();
        List<Stu> content = aggregatedPage.getContent();
        System.out.println("分页数:"+ totalPages);
        for (Stu stu : content) {
            System.out.println(stu.toString());
        }

    }
}
