package be.ictdynamic.ES_GEO_POC.model;

import com.fasterxml.jackson.annotation.JsonInclude;

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
    public static class Location {
        private String address;
        private String description;
        private String lat;
        private String lon;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        @Override
        public String toString() {
            return "Location{" + "address='" + address + '\'' + ", description='" + description + '\'' + ", lat='" + lat + '\'' + ", lon='" + lon + '\'' + '}';
        }
    }

}
