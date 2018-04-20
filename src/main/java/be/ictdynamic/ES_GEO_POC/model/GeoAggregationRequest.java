package be.ictdynamic.ES_GEO_POC.model;

import java.util.LinkedHashSet;

public class GeoAggregationRequest {
    private double lat;
    private double lon;
    private LinkedHashSet<Range> ranges;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public LinkedHashSet<Range> getRanges() {
        return ranges;
    }

    public void setRanges(LinkedHashSet<Range> ranges) {
        this.ranges = ranges;
    }

    public static class Range{
        private String key;
        private Double from;
        private Double to;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Double getFrom() {
            return from;
        }

        public void setFrom(Double from) {
            this.from = from;
        }

        public Double getTo() {
            return to;
        }

        public void setTo(Double to) {
            this.to = to;
        }
    }
}
