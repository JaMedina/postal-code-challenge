package com.jorge.wcc.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "PostalCodeLog")
@Table(name = "T_POSTALCODELOG")
@XmlRootElement(name = "postal-code-log")
@XmlAccessorType(XmlAccessType.FIELD)
public class PostalCodeLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonIgnore
  @XmlTransient
  private Long id;

  @Column(name = "distance", precision = 10, scale = 2)
  private BigDecimal distance;

  @Column(name = "unit")
  private String unit;

  @ManyToOne
  private PostalCode origin;

  @ManyToOne
  private PostalCode destination;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getDistance() {
    return distance;
  }

  public void setDistance(BigDecimal distance) {
    this.distance = distance;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public PostalCode getOrigin() {
    return origin;
  }

  public void setOrigin(PostalCode postalCode) {
    this.origin = postalCode;
  }

  public PostalCode getDestination() {
    return destination;
  }

  public void setDestination(PostalCode postalCode) {
    this.destination = postalCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PostalCodeLog postalCodeD = (PostalCodeLog) o;

    if (!Objects.equals(id, postalCodeD.id))
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "PostalCodeD{" + "id=" + id + ", distance='" + distance + "'" + ", unit='" + unit + "'" + '}';
  }

}
