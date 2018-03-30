package be.ictdynamic.ES_GEO_POC.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationRequest {
    private List<Location> locations;

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Location {
        private String id;
        private String name;
        private String status;
        private String level;
        private String planning;
        private String shape;
        private String objectId;
        private String gisId;
        private String point_lat;
        private String point_lng;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPoint_lat() {
            return point_lat;
        }

        public void setPoint_lat(String point_lat) {
            this.point_lat = point_lat;
        }

        public String getPoint_lng() {
            return point_lng;
        }

        public void setPoint_lng(String point_lng) {
            this.point_lng = point_lng;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getPlanning() {
            return planning;
        }

        public void setPlanning(String planning) {
            this.planning = planning;
        }

        public String getShape() {
            return shape;
        }

        public void setShape(String shape) {
            this.shape = shape;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getGisId() {
            return gisId;
        }

        public void setGisId(String gisId) {
            this.gisId = gisId;
        }


    }

}
