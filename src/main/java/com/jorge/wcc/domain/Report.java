package com.jorge.wcc.domain;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "report")
@XmlAccessorType(XmlAccessType.FIELD)
public class Report implements Serializable {
  private static final long serialVersionUID = 1L;

  private List<PostalCodeLog> longestDistances;
  private List<PostalCodeLog> shortestDistances;
  private List<PostalCodeCount> mostRequestedOrigins;
  private List<PostalCodeCount> mostRequestedDestinations;
  private List<PostalCodeCount> mostRequestedPairs;

  public List<PostalCodeLog> getLongestDistances() {
    return longestDistances;
  }

  public void setLongestDistances(List<PostalCodeLog> longestDistances) {
    this.longestDistances = longestDistances;
  }

  public List<PostalCodeLog> getShortestDistances() {
    return shortestDistances;
  }

  public void setShortestDistances(List<PostalCodeLog> shortestDistances) {
    this.shortestDistances = shortestDistances;
  }

  public List<PostalCodeCount> getMostRequestedOrigins() {
    return mostRequestedOrigins;
  }

  public void setMostRequestedOrigins(List<PostalCodeCount> mostRequestedOrigins) {
    this.mostRequestedOrigins = mostRequestedOrigins;
  }

  public List<PostalCodeCount> getMostRequestedDestinations() {
    return mostRequestedDestinations;
  }

  public void setMostRequestedDestinations(List<PostalCodeCount> mostRequestedDestinations) {
    this.mostRequestedDestinations = mostRequestedDestinations;
  }

  public List<PostalCodeCount> getMostRequestedPairs() {
    return mostRequestedPairs;
  }

  public void setMostRequestedPairs(List<PostalCodeCount> mostRequestedPairs) {
    this.mostRequestedPairs = mostRequestedPairs;
  }
}
