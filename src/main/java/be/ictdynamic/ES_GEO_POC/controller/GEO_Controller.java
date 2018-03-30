package be.ictdynamic.ES_GEO_POC.controller;

import be.ictdynamic.ES_GEO_POC.model.LocationRequest;
import be.ictdynamic.ES_GEO_POC.model.LocationResponse;
import be.ictdynamic.ES_GEO_POC.service.LocationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Set;

@Controller
public class GEO_Controller extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(GEO_Controller.class);

	@Autowired
	private LocationService locationService;

    @ApiOperation(value = "Method to process array of locations.", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created. The locations have been created.", response = LocationResponse.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = LocationResponse.class) })
    @RequestMapping(value="/locations",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity createEvent (@Valid @RequestBody(required = true) LocationRequest locationRequest) throws IllegalArgumentException, IOException {
        locationService.processLocations(locationRequest);
        LocationResponse locationResponse = new LocationResponse(String.format("Number of locations created: %06d.", locationRequest.getLocations().size()));
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(locationResponse);
    }

    @ApiOperation(value = "Method to retrieve locations.", notes = "")
    @RequestMapping(value="/locations", method=RequestMethod.GET)
    public ResponseEntity<Set<LocationRequest.Location>> retrieveEvents (
            @RequestParam(value = "lat", required = true) double lat,
            @RequestParam(value = "lon", required = true) double lon,
            @RequestParam(value = "distance", required = true) int distance
    ) throws Exception {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(locationService.geoDistanceQuery(lat, lon, distance));
    }

}
