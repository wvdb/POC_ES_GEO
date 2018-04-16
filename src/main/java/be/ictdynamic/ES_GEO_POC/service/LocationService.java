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

    @SuppressWarnings("unchecked")
    public void persistLocations(LocationRequest locationRequest) throws IllegalArgumentException, IOException {
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
                LOGGER.debug("IndexResponse: {}", indexResponse);
            }
        }

        IctDynamicUtilities.timedReturn(LOGGER, new Object() {
        }.getClass().getEnclosingMethod().getName(), startDate.getTime());
    }

}
