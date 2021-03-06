package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EsCourseService {
    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Value("${xuecheng.elasticsearch.course.index}")
    private String index;
    @Value("${xuecheng.elasticsearch.course.type}")
    private String type;
    @Value("${xuecheng.elasticsearch.course.source_field}")
    private String source_field;
    private static final Logger LOGGER = LoggerFactory.getLogger( EsCourseService.class );
    //课程搜索
    public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam){
        //创建课程搜索请求对象
        SearchRequest searchRequest = new SearchRequest(index);
        //设置搜索类型
        searchRequest.types(type);
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //过滤源字段
        String[] source_field_array = source_field.split(",");
        searchSourceBuilder.fetchSource(source_field_array,new String[]{});
        //创建布尔条件查询
        BoolQueryBuilder boolQueryBuilder= QueryBuilders.boolQuery();
        //搜索条件
        //根据关键字搜索
        if(StringUtils.isNotEmpty(courseSearchParam.getKeyword())){
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(courseSearchParam, "name", "description", "teachplan");
            //设置匹配占比
            multiMatchQueryBuilder.minimumShouldMatch("70%");
            //提升另个字段的Boost值
            multiMatchQueryBuilder.field( "name", 10 );
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }
        //设置boolQueryBuilder到searchSourceBuilder
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        //执行搜索
        QueryResult<CoursePub> queryResult=new QueryResult();
        List<CoursePub> list=new ArrayList<>();
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            //获得响应结果
            SearchHits hits = searchResponse.getHits();
            //匹配的总记录数
           long totalHits= hits.totalHits;
           queryResult.setTotal(totalHits);
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                CoursePub coursePub=new CoursePub();
                //源文档
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                String name = (String) sourceAsMap.get("name");
                coursePub.setName(name);
                //图片
                String pic = (String) sourceAsMap.get( "pic" );
                coursePub.setPic( pic );
                //价格
                Double price = null;
                try {
                    if(sourceAsMap.get("price") != null) {
                       price = (Double)( sourceAsMap.get("price"));
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
                coursePub.setPrice(price);
                //原价
                Double price_old = null;
                try {
                    if(sourceAsMap.get( "price_old" ) != null) {
                        price_old = (Double) sourceAsMap.get( "price_old" );
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
                coursePub.setPrice_old(price_old);
                list.add(coursePub);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        QueryResponseResult<CoursePub> coursePubQueryResponseResult = new QueryResponseResult<CoursePub>( CommonCode.SUCCESS, queryResult );
        return coursePubQueryResponseResult;
    }
}

