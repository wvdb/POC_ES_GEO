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

    public static class Location {
        private String id;
        private String naam;
        private String point_lat;
        private String point_lng;
        private String status;
        private String niveau;
        private String planning;
        private String shape;
        private String objectid;
        private String gisid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNaam() {
            return naam;
        }

        public void setNaam(String naam) {
            this.naam = naam;
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

        public String getNiveau() {
            return niveau;
        }

        public void setNiveau(String niveau) {
            this.niveau = niveau;
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

        public String getObjectid() {
            return objectid;
        }

        public void setObjectid(String objectid) {
            this.objectid = objectid;
        }

        public String getGisid() {
            return gisid;
        }

        public void setGisid(String gisid) {
            this.gisid = gisid;
        }
    }

}
