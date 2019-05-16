package be.ictdynamic.ES_GEO_POC.model;

import lombok.Data;

import java.util.Set;

@Data
public class EsQuery {
    private Set<EsCondition> esConditions;

    @Data
    public static class EsCondition {
        private QueryType queryType;
        private String queryField;
        private Object queryValue;
    }

    public enum QueryType {
        TERM_QUERY, REGEXP_QUERY
    }
}
