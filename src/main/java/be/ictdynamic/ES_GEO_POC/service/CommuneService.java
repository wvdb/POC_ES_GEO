package be.ictdynamic.ES_GEO_POC.service;

import be.ictdynamic.ES_GEO_POC.model.CommuneRequest;
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

        IctDynamicUtilities.timedReturn(LOGGER, new Object() {
        }.getClass().getEnclosingMethod().getName(), startDate.getTime());
    }

}
