package be.ictdynamic.ES_GEO_POC.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationRequest {
    private List<RailwayStation> railwayStations;

    public List<RailwayStation> getRailwayStations() {
        return railwayStations;
    }

    public void setRailwayStations(List<RailwayStation> railwayStations) {
        this.railwayStations = railwayStations;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    public static class RailwayStation {
        private String id;
        private String naam;
        private String point_lat;
        private String point_lng;
        private String status;
        private String niveau;
        private String planning;
        private String shape;
        @JsonProperty("objectid")
        private String objectId;
        @JsonProperty("gisid")
        private String gisId;
        private float score;

        @Override
        public String toString() {
            return "RailwayStation{" + "id='" + id + '\'' + ", naam='" + naam + ", point_lat='" + point_lat + '\'' + ", point_lng='" + point_lng + '\'' + '}';
        }
    }

}
