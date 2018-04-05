package be.ictdynamic.ES_GEO_POC.controller;

import be.ictdynamic.ES_GEO_POC.model.LocationRequest;
import be.ictdynamic.ES_GEO_POC.service.LocationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
public class GEO_Controller extends BaseController {

	@Autowired
	private LocationService locationService;

    @ApiOperation(value = "Method to retrieve locations.", notes = "")
    @RequestMapping(value="/locations", method=RequestMethod.GET)
    public ResponseEntity<Set<LocationRequest.Location>> retrieveLocations(
            @RequestParam(value = "lat", required = true) double lat,
            @RequestParam(value = "lon", required = true) double lon,
            @RequestParam(value = "distance", required = true) int distance
    ) throws Exception {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(locationService.geoDistanceQuery(lat, lon, distance));
    }

}
