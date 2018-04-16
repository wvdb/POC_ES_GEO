package be.ictdynamic.ES_GEO_POC;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by wvdbrand on 30/06/2017.
 */
@TestPropertySource(value = {"classpath:/application.properties"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ESTestApplication.class)
@WebAppConfiguration
public class Geo1Test {
    @Autowired
    private RestHighLevelClient restClient;

    @Test
    public void geoDistanceQuery() throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        QueryBuilder query = QueryBuilders.matchAllQuery();

        QueryBuilder geoDistanceQueryBuilder = QueryBuilders.geoDistanceQuery("myLocation").point(51.2061132, 4.40)
                .distance(100, DistanceUnit.KILOMETERS);

        QueryBuilder finalQuery = QueryBuilders.boolQuery().must(query).filter(geoDistanceQueryBuilder);

        sourceBuilder.query(finalQuery);

        SearchRequest searchRequest = new SearchRequest("commune").source(sourceBuilder);

        SearchResponse searchResponse = restClient.search(searchRequest);

        SearchHits hits = searchResponse.getHits();

        for (SearchHit hit : hits.getHits()) {
            getEventFromESHit(hit);
        }

    }

    @Test
    public void geoBoundingBoxQuery() throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        QueryBuilder query = QueryBuilders.matchAllQuery();

        QueryBuilder geoDistanceQueryBuilder = QueryBuilders.geoBoundingBoxQuery("myLocation").setCorners(51.24, 3.50, 50.90, 4.57);

        QueryBuilder finalQuery = QueryBuilders.boolQuery().must(query).filter(geoDistanceQueryBuilder);

        sourceBuilder.query(finalQuery);

        SearchRequest searchRequest = new SearchRequest("commune").source(sourceBuilder);

        SearchResponse searchResponse = restClient.search(searchRequest);

        SearchHits hits = searchResponse.getHits();

        for (SearchHit hit : hits.getHits()) {
            getEventFromESHit(hit);
        }

    }

    @Test
    public void geoPolygonQuery() throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        QueryBuilder query = QueryBuilders.matchAllQuery();

        List<GeoPoint> points = List.of(new GeoPoint(51.383977,4.101346),
                                        new GeoPoint(51.425098,5.156033),
                                        new GeoPoint(50.714051,4.980252),
                                        new GeoPoint(50.714051,3.958523));

        QueryBuilder geoQueryBuilder = QueryBuilders.geoPolygonQuery("myLocation", points);

        QueryBuilder finalQuery = QueryBuilders.boolQuery().must(query).filter(geoQueryBuilder);

        sourceBuilder.query(finalQuery);

        SearchRequest searchRequest = new SearchRequest("commune").source(sourceBuilder);

        SearchResponse searchResponse = restClient.search(searchRequest);

        SearchHits hits = searchResponse.getHits();

        for (SearchHit hit : hits.getHits()) {
            getEventFromESHit(hit);
        }

    }

//    @Test
//    public void geoDistanceAggregation() throws IOException {
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//
//        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.1/java-rest-high-search.html
//
//        TermsAggregationBuilder aggregation = AggregationBuilders.terms("my customers - breakdown by distance")
//                .field("myLocation");
//
//        aggregation.subAggregation(AggregationBuilders.avg("average_age")
//                .field("age"));
//        sourceBuilder.aggregation(aggregation);
//
//
//        GeoDistanceAggregationBuilder geoDistanceAggregationBuilder = new GeoDistanceAggregationBuilder("", new GeoPoint(51.383977,4.101346));
//
//        QueryBuilder query = QueryBuilders.matchAllQuery();
//
//        QueryBuilder finalQuery = QueryBuilders.boolQuery().must(query).filter(geoQueryBuilder);
//
//        sourceBuilder.query(finalQuery);
//
//        SearchRequest searchRequest = new SearchRequest("commune").source(sourceBuilder);
//
//        SearchResponse searchResponse = restClient.search(searchRequest);
//
//        SearchHits hits = searchResponse.getHits();
//
//        for (SearchHit hit : hits.getHits()) {
//            getEventFromESHit(hit);
//        }
//
//    }

    private static void getEventFromESHit(SearchHit hit) {
        Map<String,Object> source = hit.getSourceAsMap();
        System.out.println("City = " + source.get("city"));
    }


}
