package be.ictdynamic.ES_GEO_POC.service;

import be.ictdynamic.ES_GEO_POC.model.LocationRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.simple.JSONObject;
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

@Component
@PropertySource(value = {"classpath:/application.properties"}, ignoreResourceNotFound = true)
public class LocationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);

    @Autowired
    private RestHighLevelClient restClient;

    public void processLocations(LocationRequest locationRequest) throws IllegalArgumentException, IOException {
        Date startDate = new Date();

        for (LocationRequest.Location location : locationRequest.getLocations()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Location = {}.", location);
            }

            JSONObject myLocation = new JSONObject();

            myLocation.put("lat", location.getPoint_lat());
            myLocation.put("lon", location.getPoint_lng());

            IndexRequest indexRequest = new IndexRequest("location", "doc")
                    .source(
                            "id", location.getId(),
                            "name", location.getName(),
                            "myLocation", myLocation,
                            "status", location.getStatus(),
                            "level", location.getLevel(),
                            "planning", location.getPlanning(),
                            "shape", location.getShape(),
                            "objectId", location.getObjectId(),
                            "gisId", location.getGisId()
                    );

            IndexResponse indexResponse = restClient.index(indexRequest);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Persisted location: {}", location);
            }
        }

        MDE_Utilities.timedReturn(LOGGER, new Object() {}.getClass().getEnclosingMethod().getName(), startDate.getTime());
    }

    public Set<LocationRequest.Location> geoDistanceQuery(double lat, double lon, int distance) throws IOException {
        Set<LocationRequest.Location> locations = new LinkedHashSet<>();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        QueryBuilder query = QueryBuilders.matchAllQuery();

        QueryBuilder geoDistanceQueryBuilder = QueryBuilders
                .geoDistanceQuery("myLocation")
                .point(lat, lon)
                .distance(distance, DistanceUnit.KILOMETERS);

        QueryBuilder finalQuery = QueryBuilders.boolQuery().must(query).filter(geoDistanceQueryBuilder);

        sourceBuilder.query(finalQuery);

        SearchRequest searchRequest = new SearchRequest("location").source(sourceBuilder);

        SearchResponse searchResponse = restClient.search(searchRequest);

        SearchHits hits = searchResponse.getHits();

        for (SearchHit hit : hits.getHits()) {
            locations.add(getDocumentsFromHit(hit));
        }

        return locations;
    }

    private static LocationRequest.Location getDocumentsFromHit(SearchHit hit) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("hit: id = {}", hit.getId());
        }

        LocationRequest.Location location = new LocationRequest.Location();

        Map<String,Object> source = hit.getSourceAsMap();

        location.setId((String) source.get("id"));
        location.setName((String) source.get("name"));
        location.setObjectId((String) source.get("objectId"));

        return location;
    }

}
