package be.ictdynamic.ES_GEO_POC.controller;

import be.ictdynamic.ES_GEO_POC.model.*;
import be.ictdynamic.ES_GEO_POC.service.ESPersistService;
import be.ictdynamic.ES_GEO_POC.service.GEO_Service;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class GEO_Controller extends BaseController {
    @Autowired
    private ESPersistService elastSearchPersistService;

    @Autowired
    private GEO_Service geoService;

    @ApiOperation(value = "Method to persist locations.", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created. The locations have been created.", response = PocResponse.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = PocResponse.class) })
    @RequestMapping(value="/locations",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity persistLocations (@Valid @RequestBody(required = true) LocationRequest locationRequest) throws IllegalArgumentException, IOException {
        elastSearchPersistService.persistLocations(locationRequest);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new PocResponse(String.format("Number of locations persisted: %06d.", locationRequest.getLocations().size())));
    }

    @ApiOperation(value = "Method to persist communes.", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created. The communes have been created.", response = PocResponse.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = PocResponse.class) })
    @RequestMapping(value="/communes",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity persistCommunes (@Valid @RequestBody(required = true) CommuneRequest communeRequest) throws IllegalArgumentException, IOException {
        elastSearchPersistService.persistCommunes(communeRequest);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new PocResponse(String.format("Number of communes persisted: %06d.", communeRequest.getCommunes().size())));
    }

    @ApiOperation(value = "Method to persist retail locations.", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created. The retail locations have been created.", response = PocResponse.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = PocResponse.class) })
    @RequestMapping(value="/retailLocations",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity persistRetailLocations (@Valid @RequestBody(required = true) RetailLocationsRequest retailLocationsRequest) throws IllegalArgumentException, IOException {
        elastSearchPersistService.persistRetailLocations(retailLocationsRequest);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new PocResponse(String.format("Number of locations persisted: %06d.", retailLocationsRequest.getLocations().size())));
    }

    @ApiOperation(value = "Generic Method to retrieve objects within certain distance.", notes = "")
    @RequestMapping(value="/retrieveObjects", method=RequestMethod.GET)
    public ResponseEntity<?> retrieveObjects(
            @RequestParam(value = "index", required = true) String index,
            @RequestParam(value = "nameGeoPointField", required = true) String nameGeoPointField,
            @RequestParam(value = "objectType", required = true) String objectType,
            @RequestParam(value = "lat", required = true) double lat,
            @RequestParam(value = "lon", required = true) double lon,
            @RequestParam(value = "distance", required = true) int distance
    ) throws Exception {
        if (org.springframework.util.StringUtils.isEmpty(objectType)) {
            throw new IllegalArgumentException("queryParameterMap unknown");
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(geoService.geoDistanceQuery(index, nameGeoPointField, objectType, lat, lon, distance));
    }

    @ApiOperation(value = "Method to retrieve objects within Bounding Box.", notes = "")
    @RequestMapping(value="/geoBoundingBoxQuery", method=RequestMethod.GET)
    public ResponseEntity<?> geoBoundingBoxQuery(
            @RequestParam(value = "objectType", required = true)    String objectType,
            @RequestParam(value = "top", required = true)    double top,
            @RequestParam(value = "left", required = true)   double left,
            @RequestParam(value = "bottom", required = true) double bottom,
            @RequestParam(value = "right", required = true)  double right
    ) throws Exception {
        double[] corners = new double[4];
        corners[0] = top;
        corners[1] = left;
        corners[2] = bottom;
        corners[3] = right;
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(geoService.geoBoundingBoxQuery(objectType, corners));
    }

    @ApiOperation(value = "Method to retrieve objects within Polygon.", notes = "Query parameter is a map of geoPoints(lat,lon).")
    @RequestMapping(value="/geoPolygonQuery", method=RequestMethod.GET)
    public ResponseEntity<?> geoPolygonQuery(@RequestParam Map<String, String> geoPointsAsMap) throws Exception {
        List<GeoPoint> geoPoints = new ArrayList<>();
        geoPointsAsMap.forEach((key, value) -> {
            GeoPoint geoPoint = new GeoPoint(Double.parseDouble(key), Double.parseDouble(value));
            geoPoints.add(geoPoint);
        });
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(geoService.geoPolygonQuery(geoPoints));
    }

    @ApiOperation(value = "Method to retrieve distance-aggregations.", notes = "")
    @RequestMapping(value="/geoAggregation", method=RequestMethod.POST)
    public ResponseEntity<?> geoAggregation(@RequestBody(required = true) GeoAggregationRequest geoAggregationRequest) throws Exception {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(geoService.geoAggregation(geoAggregationRequest));
    }

}
