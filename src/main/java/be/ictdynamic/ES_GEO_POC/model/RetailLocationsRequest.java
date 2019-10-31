package be.ictdynamic.ES_GEO_POC.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RetailLocationsRequest {
    private List<Location> locations;

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    public static class Location {
        private String address;
        private String description;
        private String lat;
        private String lon;
        private float distance;
    }

}
