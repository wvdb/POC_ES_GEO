package be.ictdynamic.ES_GEO_POC.service;

import be.ictdynamic.ES_GEO_POC.model.CommuneRequest;
import be.ictdynamic.ES_GEO_POC.model.GeoAggregationRequest;
import be.ictdynamic.ES_GEO_POC.model.LocationRequest;
import be.ictdynamic.ES_GEO_POC.model.RetailLocationsRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.range.GeoDistanceAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.ParsedGeoDistance;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

import static be.ictdynamic.ES_GEO_POC.service.IctDynamicUtilities.timedReturn;

@Component
@PropertySource(value = {"classpath:/application.properties"}, ignoreResourceNotFound = true)
public class GEO_Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(GEO_Service.class);

    private static final int SIZE_ES_QUERY = 100;

    @Autowired
    private RestHighLevelClient restClient;

    public Set<?> geoDistanceQuery(String index, String nameGeoPointField, String objectType, double lat, double lon, int distance) throws IOException {
        Date startDate = new Date();

        Set<?> objectsWithinDistance = new LinkedHashSet<>();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        QueryBuilder query = QueryBuilders.matchAllQuery();

        QueryBuilder geoDistanceQueryBuilder = QueryBuilders
                .geoDistanceQuery(nameGeoPointField)
                .point(lat, lon)
                .distance(distance, DistanceUnit.KILOMETERS);

        QueryBuilder finalQuery = QueryBuilders.boolQuery().must(query).filter(geoDistanceQueryBuilder);

        sourceBuilder.query(finalQuery).size(SIZE_ES_QUERY);

        SearchRequest searchRequest = new SearchRequest(index)
                .source(sourceBuilder.sort(SortBuilders.geoDistanceSort(nameGeoPointField, lat, lon)
                        .order(SortOrder.ASC)
                        .unit(DistanceUnit.KILOMETERS)));

        SearchResponse searchResponse = restClient.search(searchRequest);

        SearchHits hits = searchResponse.getHits();

        for (SearchHit hit : hits.getHits()) {
            objectsWithinDistance.add(GEO_Service.getObjectFromES_Hit(hit, objectType));
        }

        return timedReturn(LOGGER, new Object() {}.getClass().getEnclosingMethod().getName(), startDate.getTime(), objectsWithinDistance);
    }

    @SuppressWarnings("unchecked")
    private static <T> T getObjectFromES_Hit(SearchHit hit, String objectType) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("hit: id = {}", hit.getId());
            LOGGER.debug("hit: score = {}", hit.getScore());
            if (hit.getSortValues().length >= 1) {
                LOGGER.debug("hit: sort value = {}", hit.getSortValues()[0]);
            }
        }

        Map<String, Object> source = hit.getSourceAsMap();

        switch(objectType) {
            case "location":
                LocationRequest.Location location = new LocationRequest.Location();

                location.setId((String) source.get("id"));
                location.setName((String) source.get("name"));
                location.setObjectId((String) source.get("objectId"));
                location.setScore(hit.getScore());
                return (T) location;
            case "commune":
                CommuneRequest.Commune commune = new CommuneRequest.Commune();

                commune.setCity((String) source.get("city"));
                if (hit.getSortValues().length >= 1) {
                    //TODO : to clarify why sort values is an array and if we can simply take first element of array
                    if (hit.getSortValues().length >= 1) {
                        commune.setDistance((double) hit.getSortValues()[0]);
                    }
                }
                return (T) commune;
            case "retailer":
                RetailLocationsRequest.Location retailLocation = new RetailLocationsRequest.Location();

                retailLocation.setAddress((String) source.get("address"));
                retailLocation.setDescription((String) source.get("description"));
                return (T) retailLocation;
            default:
                LOGGER.error("invalid objectType: this objectType is currently not supported.");
                return null;
        }

    }

    public Set<CommuneRequest.Commune> geoBoundingBoxQuery(double[] corners) throws IOException {
        Date startDate = new Date();

        Set<CommuneRequest.Commune> communes = new LinkedHashSet<>();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        QueryBuilder query = QueryBuilders.matchAllQuery();

        QueryBuilder geoDistanceQueryBuilder = QueryBuilders
                .geoBoundingBoxQuery("myLocation")
                .setCorners(corners[0], corners[1], corners[2], corners[3]);

        QueryBuilder finalQuery = QueryBuilders.boolQuery().must(query).filter(geoDistanceQueryBuilder);

        sourceBuilder.query(finalQuery).size(SIZE_ES_QUERY);

        SearchRequest searchRequest = new SearchRequest("commune").source(sourceBuilder);

        SearchResponse searchResponse = restClient.search(searchRequest);

        SearchHits hits = searchResponse.getHits();

        for (SearchHit hit : hits.getHits()) {
            communes.add(GEO_Service.getObjectFromES_Hit(hit, "commune"));
        }

        return timedReturn(LOGGER, new Object() {}.getClass().getEnclosingMethod().getName(), startDate.getTime(), communes);
    }

    public Set<CommuneRequest.Commune> geoPolygonQuery(List<GeoPoint> geoPoints) throws IOException {
        Date startDate = new Date();

        Set<CommuneRequest.Commune> communes = new LinkedHashSet<>();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        QueryBuilder query = QueryBuilders.matchAllQuery();

        QueryBuilder geoQueryBuilder = QueryBuilders
                .geoPolygonQuery("myLocation", geoPoints);

        QueryBuilder finalQuery = QueryBuilders.boolQuery().must(query).filter(geoQueryBuilder);

        sourceBuilder.query(finalQuery).size(SIZE_ES_QUERY);

        SearchRequest searchRequest = new SearchRequest("commune").source(sourceBuilder);

        SearchResponse searchResponse = restClient.search(searchRequest);

        SearchHits hits = searchResponse.getHits();

        for (SearchHit hit : hits.getHits()) {
            communes.add(GEO_Service.getObjectFromES_Hit(hit, "commune"));
        }

        return timedReturn(LOGGER, new Object() {}.getClass().getEnclosingMethod().getName(), startDate.getTime(), communes);
    }

    public Map<String, Long> geoAggregation(GeoAggregationRequest geoAggregationRequest) throws IOException {
        Map<String, Long> aggregations = new LinkedHashMap<>();

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.1/java-rest-high-search.html
        // https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/_bucket_aggregations.html

        GeoDistanceAggregationBuilder geoDistanceAggregationBuilder = new GeoDistanceAggregationBuilder("distanceRanges", new GeoPoint(geoAggregationRequest.getLat(), geoAggregationRequest.getLon()));

        for (GeoAggregationRequest.Range range : geoAggregationRequest.getRanges()) {
            GeoDistanceAggregationBuilder.Range geoRange = new GeoDistanceAggregationBuilder.Range(range.getKey(), range.getFrom(), range.getTo());
            geoDistanceAggregationBuilder.addRange(geoRange);
        }

        geoDistanceAggregationBuilder.field("location");
        // Setting the keyed flag to true will associate a unique string key with each bucket and return the ranges as a hash rather than an array
        geoDistanceAggregationBuilder.keyed(true);
        geoDistanceAggregationBuilder.unit(DistanceUnit.KILOMETERS);

        sourceBuilder.aggregation(geoDistanceAggregationBuilder);

        SearchRequest searchRequest = new SearchRequest("retail_locations").source(sourceBuilder);

        SearchResponse searchResponse = restClient.search(searchRequest);

        ParsedGeoDistance parsedGeoDistance = searchResponse.getAggregations().get("distanceRanges");

        for (Range.Bucket entry : parsedGeoDistance.getBuckets()) {
            String key = entry.getKeyAsString();            // bucket key
            long docCount = entry.getDocCount();            // doc count
            aggregations.put(key, docCount);
        }

        return aggregations;
    }


}
