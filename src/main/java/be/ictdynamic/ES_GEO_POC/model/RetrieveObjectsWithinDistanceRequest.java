package be.ictdynamic.ES_GEO_POC.model;

import lombok.Data;

import java.util.Map;

@Data
public class RetrieveObjectsWithinDistanceRequest {
    private Map<String, Object> conditions;
}
