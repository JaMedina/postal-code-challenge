package com.jorge.wcc.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jorge.wcc.domain.PostalCode;
import com.jorge.wcc.domain.PostalCodeCount;
import com.jorge.wcc.domain.PostalCodeLog;
import com.jorge.wcc.domain.Report;
import com.jorge.wcc.exception.PostalCodeNotFoundException;
import com.jorge.wcc.repository.PostalCodeLogRepository;
import com.jorge.wcc.repository.PostalCodeRepository;
import com.jorge.wcc.web.rest.util.PaginationUtil;

@Service
public class PostalCodeService {
  private static final double EARTH_RADIUS = 6371; // radius in kilometers
  private static final String UNIT = "km";

  @Autowired
  private PostalCodeRepository postalCodeRepository;
  @Autowired
  private PostalCodeLogRepository postalCodeLogRepository;

  public PostalCodeLog calculateDistance(String postalCodeOrigin, String postalCodeDestination) {
    PostalCode origin = postalCodeRepository.findByPostalCode(postalCodeOrigin.toUpperCase());
    if (origin == null)
      throw new PostalCodeNotFoundException(postalCodeOrigin);

    PostalCode destination = postalCodeRepository.findByPostalCode(postalCodeDestination.toUpperCase());
    if (destination == null)
      throw new PostalCodeNotFoundException(postalCodeDestination);

    BigDecimal distance = getDistance(origin, destination);

    PostalCodeLog postalCodeLog = new PostalCodeLog();
    postalCodeLog.setOrigin(origin);
    postalCodeLog.setDestination(destination);
    postalCodeLog.setDistance(distance);
    postalCodeLog.setUnit(UNIT);

    postalCodeLogRepository.save(postalCodeLog);

    return postalCodeLog;
  }

  public Report generateReport(Integer top) {
    List<PostalCodeLog> longestDistance = postalCodeLogRepository.getLongestDistanceEntries(PaginationUtil.generatePageRequest(PaginationUtil.MIN_OFFSET, top));
    List<PostalCodeLog> shortestDistance = postalCodeLogRepository.getShortestDistanceEntries(PaginationUtil.generatePageRequest(PaginationUtil.MIN_OFFSET, top));
    List<PostalCodeCount> originRequest = postalCodeLogRepository.getMostRequestedOrigin(PaginationUtil.generatePageRequest(PaginationUtil.MIN_OFFSET, top));
    List<PostalCodeCount> destinationRequest = postalCodeLogRepository.getMostRequestedDestination(PaginationUtil.generatePageRequest(PaginationUtil.MIN_OFFSET, top));
    List<PostalCodeCount> pairRequest = postalCodeLogRepository.getMostRequestedPair(PaginationUtil.generatePageRequest(PaginationUtil.MIN_OFFSET, top));

    Report report = new Report();
    report.setLongestDistances(longestDistance);
    report.setShortestDistances(shortestDistance);
    report.setMostRequestedOrigins(originRequest);
    report.setMostRequestedDestinations(destinationRequest);
    report.setMostRequestedPairs(pairRequest);
    return report;
  }


  private BigDecimal getDistance(PostalCode origin, PostalCode destination) {
    // Using Haversine formula! See Wikipedia.
    double lon1Radians = Math.toRadians(origin.getLatitude().doubleValue());
    double lon2Radians = Math.toRadians(destination.getLatitude().doubleValue());
    double lat1Radians = Math.toRadians(origin.getLongitude().doubleValue());
    double lat2Radians = Math.toRadians(destination.getLongitude().doubleValue());

    double a = haversine(lat1Radians, lat2Radians) + Math.cos(lat1Radians) * Math.cos(lat2Radians) * haversine(lon1Radians, lon2Radians);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return new BigDecimal(EARTH_RADIUS * c).setScale(2, RoundingMode.HALF_UP);
  }

  private double haversine(double deg1, double deg2) {
    return square(Math.sin((deg1 - deg2) / 2.0));
  }

  private double square(double x) {
    return x * x;
  }
}
