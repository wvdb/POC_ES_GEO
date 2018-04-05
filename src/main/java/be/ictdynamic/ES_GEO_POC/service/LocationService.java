package be.ictdynamic.ES_GEO_POC.service;

import be.ictdynamic.ES_GEO_POC.model.LocationRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static be.ictdynamic.ES_GEO_POC.service.MDE_Utilities.timedReturn;

@Component
@PropertySource(value = {"classpath:/application.properties"}, ignoreResourceNotFound = true)
public class LocationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);
    public static final int SIZE_ES_QUERY = 100;

    @Autowired
    private RestHighLevelClient restClient;

    public Set<LocationRequest.Location> geoDistanceQuery(double lat, double lon, int distance) throws IOException {
        Date startDate = new Date();

        Set<LocationRequest.Location> locations = new LinkedHashSet<>();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        QueryBuilder query = QueryBuilders.matchAllQuery();

        QueryBuilder geoDistanceQueryBuilder = QueryBuilders
                .geoDistanceQuery("myLocation")
                .point(lat, lon)
                .distance(distance, DistanceUnit.KILOMETERS);

        QueryBuilder finalQuery = QueryBuilders.boolQuery().must(query).filter(geoDistanceQueryBuilder);

        sourceBuilder.query(finalQuery).size(SIZE_ES_QUERY);

        SearchRequest searchRequest = new SearchRequest("location")
                .source(sourceBuilder.sort(SortBuilders.geoDistanceSort("myLocation", lat, lon)
                        .order(SortOrder.ASC)
                        .unit(DistanceUnit.KILOMETERS)));

        SearchResponse searchResponse = restClient.search(searchRequest);

        SearchHits hits = searchResponse.getHits();

        for (SearchHit hit : hits.getHits()) {
            locations.add(LocationService.getDocumentsFromHit(hit));
        }

        return timedReturn(LOGGER, new Object() {}.getClass().getEnclosingMethod().getName(), startDate.getTime(), locations);
    }

    private static LocationRequest.Location getDocumentsFromHit(SearchHit hit) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("hit: id = {}", hit.getId());
            LOGGER.debug("hit: score = {}", hit.getScore());
        }

        LocationRequest.Location location = new LocationRequest.Location();

        Map<String,Object> source = hit.getSourceAsMap();

        location.setId((String) source.get("id"));
        location.setName((String) source.get("name"));
        location.setObjectId((String) source.get("objectId"));
        location.setScore(hit.getScore());

        return location;
    }

}
