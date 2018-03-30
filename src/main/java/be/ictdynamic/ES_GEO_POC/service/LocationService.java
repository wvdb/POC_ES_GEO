package be.ictdynamic.ES_GEO_POC.service;

import be.ictdynamic.ES_GEO_POC.model.LocationRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

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
                            "name", location.getNaam(),
                            "myLocation", myLocation,
                            "status", location.getStatus(),
                            "level", location.getNiveau(),
                            "planning", location.getPlanning(),
                            "shape", location.getShape(),
                            "objectId", location.getObjectid(),
                            "gisId", location.getGisid()
                    );

            IndexResponse indexResponse = restClient.index(indexRequest);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Persisted location: {}", location);
            }
        }

        MDE_Utilities.timedReturn(LOGGER, new Object() {}.getClass().getEnclosingMethod().getName(), startDate.getTime());
    }

//    public Event retrieveEvent(String elasticSearchId) throws IOException {
//        Date startDate = new Date();
//
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//
//        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
//        boolQuery.must(QueryBuilders.idsQuery().addIds(elasticSearchId));
//        sourceBuilder.query(boolQuery);
//
//        SearchRequest searchRequest = new SearchRequest(ELASTIC_SEARCH_INDEX_NAME)
//                .types(ELASTIC_SEARCH_INDEX_TYPE)
//                .source(sourceBuilder);
//
//        SearchResponse searchResponse = restClient.search(searchRequest);
//
//        SearchHits hits = searchResponse.getHits();
//
//        Event event = new Event();
//
//        if (hits.getHits().length == 1) {
//            SearchHit hit = hits.getHits()[0];
//            event = getEventFromESHit(hit);
//        }
//        else {
//            LOGGER.warn("Searching event on event id {} failed (not exactly one hit). # of hits = {}.", elasticSearchId, hits.getHits().length);
//        }
//
//        return MDE_Utilities.timedReturn(LOGGER, new Object(){}.getClass().getEnclosingMethod().getName(), startDate.getTime(), event);
//    }

}
