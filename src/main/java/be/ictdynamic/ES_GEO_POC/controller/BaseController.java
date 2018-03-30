package be.ictdynamic.ES_GEO_POC.controller;

import be.ictdynamic.ES_GEO_POC.model.LocationResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by wvdbrand on 12/05/2017.
 */
public class BaseController {
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(new LocationResponse((ex.getMessage() == null) ? ex.toString() : ex.getMessage()), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
