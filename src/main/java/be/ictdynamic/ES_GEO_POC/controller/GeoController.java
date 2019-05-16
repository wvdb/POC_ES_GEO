package be.ictdynamic.ES_GEO_POC.controller;

import be.ictdynamic.ES_GEO_POC.model.*;
import be.ictdynamic.ES_GEO_POC.service.EsPersistService;
import be.ictdynamic.ES_GEO_POC.service.GeoService;
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

@RestController
public class GeoController extends BaseController {
    @Autowired
    private EsPersistService esPersistService;

    @Autowired
    private GeoService geoService;

    @ApiOperation(value = "Method to persist railway stations.", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created. The railway stations have been created.", response = PocResponse.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = PocResponse.class) })
    @RequestMapping(value="/railwayStations",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity railwayStations (@Valid @RequestBody(required = true) RailwayStationRequest railwayStationRequest) throws IllegalArgumentException, IOException {
        esPersistService.persistRailwayStations(railwayStationRequest);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new PocResponse(String.format("Number of railway stations persisted: %06d.", railwayStationRequest.getRailwayStations().size())));
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
        esPersistService.persistCommunes(communeRequest);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new PocResponse(String.format("Number of communes persisted: %06d.", communeRequest.getCommunes().size())));
    }

    @ApiOperation(value = "Method to persist (Starbucks) retail locations.", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created. The retail locations have been created.", response = PocResponse.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = PocResponse.class) })
    @RequestMapping(value="/retailLocations",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity persistRetailLocations (@Valid @RequestBody(required = true) RetailLocationsRequest retailLocationsRequest) throws IllegalArgumentException, IOException {
        esPersistService.persistRetailLocations(retailLocationsRequest);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new PocResponse(String.format("Number of locations persisted: %06d.", retailLocationsRequest.getLocations().size())));
    }

    @ApiOperation(value = "Generic Method to retrieve objects within certain distance.", notes = "(distance is in kilometers)")
    @RequestMapping(value="/retrieveObjectsWithinDistance", method=RequestMethod.GET)
    public ResponseEntity<?> retrieveObjectsWithinDistance(
            @RequestParam(value = "index", required = true) String index,
            @RequestParam(value = "nameGeoPointField", required = true) String nameGeoPointField,
            @RequestParam(value = "lat", required = true) double lat,
            @RequestParam(value = "lon", required = true) double lon,
            @RequestParam(value = "distance", required = true) double distance,
            @RequestBody(required = false) EsQuery esQuery
    ) throws Exception {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(geoService.geoDistanceQuery(index, nameGeoPointField, lat, lon, distance, esQuery));
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
    public ResponseEntity<?> geoPolygonQuery(
            @RequestParam(value = "index", required = true) String index,
            @RequestParam(value = "nameGeoPointField", required = true) String nameGeoPointField,
            @RequestParam(value = "geoPoints", required = true) String geoPointsString,
            @RequestBody(required = false) EsQuery esQuery
    ) throws Exception {
        List<GeoPoint> geoPoints = new ArrayList<>();
        String[] geoPointsAsString = geoPointsString.split("_");

        for (String geoPointAsString : geoPointsAsString) {
            GeoPoint geoPoint = new GeoPoint(Double.parseDouble(geoPointAsString.split(",")[0]), Double.parseDouble(geoPointAsString.split(",")[1]));
            geoPoints.add(geoPoint);
        };
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(geoService.geoPolygonQuery(index, nameGeoPointField, geoPoints, esQuery));
    }

    @ApiOperation(value = "Method to retrieve distance-aggregations.", notes = "")
    @RequestMapping(value="/geoAggregation", method=RequestMethod.POST)
    public ResponseEntity<?> geoAggregation(
            @RequestParam(value = "index", required = true) String index,
            @RequestParam(value = "nameGeoPointField", required = true) String nameGeoPointField,
            @RequestBody(required = true) GeoAggregationRequest geoAggregationRequest) throws Exception {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(geoService.geoAggregation(index, nameGeoPointField, geoAggregationRequest));
    }

}
