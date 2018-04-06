package be.ictdynamic.ES_GEO_POC.controller;

import be.ictdynamic.ES_GEO_POC.model.CommuneRequest;
import be.ictdynamic.ES_GEO_POC.model.LocationRequest;
import be.ictdynamic.ES_GEO_POC.model.PocResponse;
import be.ictdynamic.ES_GEO_POC.service.CommuneService;
import be.ictdynamic.ES_GEO_POC.service.GEO_Service;
import be.ictdynamic.ES_GEO_POC.service.LocationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class GEO_Controller extends BaseController {
	@Autowired
	private LocationService locationService;

    @Autowired
    private CommuneService communeService;

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
        locationService.persistLocations(locationRequest);
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
        communeService.persistCommunes(communeRequest);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new PocResponse(String.format("Number of communes persisted: %06d.", communeRequest.getCommunes().size())));
    }

    @ApiOperation(value = "Generic Method to retrieve objects within certain distance.", notes = "")
    @RequestMapping(value="/retrieveObjects", method=RequestMethod.GET)
    public ResponseEntity<?> retrieveObjects(
            @RequestParam(value = "objectType", required = true) String objectType,
            @RequestParam(value = "lat", required = true) double lat,
            @RequestParam(value = "lon", required = true) double lon,
            @RequestParam(value = "distance", required = true) int distance
    ) throws Exception {
        if (org.springframework.util.StringUtils.isEmpty(objectType)) {
            throw new IllegalArgumentException("queryParameterMap unknown");
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(geoService.geoDistanceQuery(objectType, lat, lon, distance));
    }

    @ApiOperation(value = "Method to retrieve objects within bounding box.", notes = "")
    @RequestMapping(value="/retrieveObjectsWithinBoundingBox", method=RequestMethod.GET)
    public ResponseEntity<?> retrieveObjectsWithinBoundingBox(
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
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(geoService.geoBoundingBoxQueryCommune(corners));
    }

}
