package be.ictdynamic.ES_GEO_POC.service;

import be.ictdynamic.ES_GEO_POC.model.CommuneRequest;
import be.ictdynamic.ES_GEO_POC.model.LocationRequest;
import be.ictdynamic.ES_GEO_POC.model.RetailLocationsRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

import static be.ictdynamic.ES_GEO_POC.service.IctDynamicUtilities.timedReturn;

@Component
@PropertySource(value = {"classpath:/application.properties"}, ignoreResourceNotFound = true)
public class EsPersistService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsPersistService.class);

    @Autowired
    private RestHighLevelClient restClient;

    @SuppressWarnings("unchecked")
    public void persistRetailLocations(RetailLocationsRequest retailLocationsRequest) throws IllegalArgumentException, IOException {
        Date startDate = new Date();

        for (RetailLocationsRequest.Location location : retailLocationsRequest.getLocations()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Location = {}.", location);
            }

            JSONObject retailLocation = new JSONObject();

            // oepsie ... small mistake in JSON
            retailLocation.put("lon", location.getLat());
            retailLocation.put("lat", location.getLon());

            IndexRequest indexRequest = new IndexRequest("retail_location")
                    .source(
                            "retailer", "Starbucks",
                            "address", location.getAddress(),
                            "description", location.getDescription(),
                            "retailLocation", retailLocation
                    );

            IndexResponse indexResponse = restClient.index(indexRequest, RequestOptions.DEFAULT);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("IndexResponse: {}", indexResponse);
            }
        }

        timedReturn(LOGGER, new Object() {}.getClass().getEnclosingMethod().getName(), startDate.getTime());
    }

    @SuppressWarnings("unchecked")
    public void persistCommunes(CommuneRequest communeRequest) throws IllegalArgumentException, IOException {
        Date startDate = new Date();

        for (CommuneRequest.Commune commune : communeRequest.getCommunes()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Location = {}.", commune);
            }

            JSONObject myLocation = new JSONObject();

            myLocation.put("lat", commune.getLat());
            myLocation.put("lon", commune.getLng());

            IndexRequest indexRequest = new IndexRequest("commune", "doc", commune.getZip())
                    .source(
                            "city", commune.getCity(),
                            "myLocation", myLocation
                    );

            IndexResponse indexResponse = restClient.index(indexRequest, RequestOptions.DEFAULT);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("IndexResponse: {}", indexResponse);
            }
        }

        timedReturn(LOGGER, new Object() {}.getClass().getEnclosingMethod().getName(), startDate.getTime());
    }

    @SuppressWarnings("unchecked")
    public void persistRailwayStations(LocationRequest locationRequest) throws IllegalArgumentException, IOException {
        Date startDate = new Date();

        for (LocationRequest.RailwayStation railwayStation : locationRequest.getRailwayStations()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("RailwayStation = {}.", railwayStation);
            }

            JSONObject railwayStationLocation = new JSONObject();

            railwayStationLocation.put("lat", railwayStation.getPoint_lat());
            railwayStationLocation.put("lon", railwayStation.getPoint_lng());

            IndexRequest indexRequest = new IndexRequest("railway_station")
                    .source(
                            "id", railwayStation.getId(),
                            "name", railwayStation.getNaam(),
                            "railwayStationLocation", railwayStationLocation,
                            "status", railwayStation.getStatus(),
                            "level", railwayStation.getNiveau(),
                            "planning", railwayStation.getPlanning(),
                            "shape", railwayStation.getShape(),
                            "objectId", railwayStation.getObjectId(),
                            "gisId", railwayStation.getGisId()
                    );

            IndexResponse indexResponse = restClient.index(indexRequest, RequestOptions.DEFAULT);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("IndexResponse: {}", indexResponse);
            }
        }

        timedReturn(LOGGER, new Object() {}.getClass().getEnclosingMethod().getName(), startDate.getTime());
    }

}
