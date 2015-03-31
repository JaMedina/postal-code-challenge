package com.jorge.wcc.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jorge.wcc.domain.PostalCodeLog;
import com.jorge.wcc.domain.Report;
import com.jorge.wcc.service.PostalCodeService;


@RestController
@RequestMapping(value = "/wcc/rest", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class PostalCodeActionsController {
  private final Logger log = LoggerFactory.getLogger(PostalCodeActionsController.class);

  @Autowired
  private PostalCodeService postalCodeService;

  @RequestMapping(value = "/calculate", method = RequestMethod.POST)
  public ResponseEntity<PostalCodeLog> create(@RequestParam(value = "origin") String origin, @RequestParam(value = "destination") String destination) {
    PostalCodeLog postalCodeLog = postalCodeService.calculateDistance(origin, destination);
    log.info("origin: {}, destination: {}, result: {}", origin, destination, postalCodeLog);
    return new ResponseEntity<>(postalCodeLog, HttpStatus.OK);
  }

  @RequestMapping(value = "/reports", method = RequestMethod.POST)
  public ResponseEntity<Report> create(@RequestParam(value = "top", required = false) Integer top) {
    Report report = postalCodeService.generateReport(top);
    log.info("Postal Code report was generated.");
    return new ResponseEntity<Report>(report, HttpStatus.OK);
  }
}
