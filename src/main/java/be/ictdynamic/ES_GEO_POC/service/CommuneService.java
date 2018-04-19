package be.ictdynamic.ES_GEO_POC.service;

import be.ictdynamic.ES_GEO_POC.model.CommuneRequest;
import be.ictdynamic.ES_GEO_POC.model.RetailLocationsRequest;
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
public class CommuneService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommuneService.class);

    @Autowired
    private RestHighLevelClient restClient;

    @SuppressWarnings("unchecked")
    public void persistRetailLocations(RetailLocationsRequest retailLocationsRequest) throws IllegalArgumentException, IOException {
        Date startDate = new Date();

        for (RetailLocationsRequest.Location location : retailLocationsRequest.getLocations()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Location = {}.", location);
            }

            JSONObject myLocation = new JSONObject();

            // oepsie ... small mistake in JSON
            myLocation.put("lon", location.getLat());
            myLocation.put("lat", location.getLon());

            IndexRequest indexRequest = new IndexRequest("retail_locations", "doc")
                    .source(
                            "retailer", "Starbucks",
                            "address", location.getAddress(),
                            "description", location.getDescription(),
                            "location", myLocation
                    );

            IndexResponse indexResponse = restClient.index(indexRequest);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("IndexResponse: {}", indexResponse);
            }
        }

        IctDynamicUtilities.timedReturn(LOGGER, new Object() {}.getClass().getEnclosingMethod().getName(), startDate.getTime());
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

            IndexResponse indexResponse = restClient.index(indexRequest);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("IndexResponse: {}", indexResponse);
            }
        }

        IctDynamicUtilities.timedReturn(LOGGER, new Object() {}.getClass().getEnclosingMethod().getName(), startDate.getTime());
    }

}
