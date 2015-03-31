package com.jorge.wcc.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "T_POSTALCODE")
@XmlRootElement(name = "postal-code")
@XmlAccessorType(XmlAccessType.FIELD)
public class PostalCode implements Serializable {
   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   @NotNull
   @Column(name = "postal_code", nullable = false)
   private String postalCode;

   @NotNull
   @Column(name = "latitude", precision = 10, scale = 2, nullable = false)
   private BigDecimal latitude;

   @NotNull
   @Column(name = "longitude", precision = 10, scale = 2, nullable = false)
   private BigDecimal longitude;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getPostalCode() {
      return postalCode;
   }

   public void setPostalCode(String postalCode) {
      this.postalCode = postalCode;
   }

   public BigDecimal getLatitude() {
      return latitude;
   }

   public void setLatitude(BigDecimal latitude) {
      this.latitude = latitude;
   }

   public BigDecimal getLongitude() {
      return longitude;
   }

   public void setLongitude(BigDecimal longitude) {
      this.longitude = longitude;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      PostalCode postalCode = (PostalCode) o;

      if (!Objects.equals(id, postalCode.id))
         return false;

      return true;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(id);
   }

   @Override
   public String toString() {
      return "PostalCode{" + //
            "id=" + id + //
            ", postalCode='" + postalCode + "'" + //
            ", latitude='" + latitude + "'" + //
            ", longitude='" + longitude + "'" + //
            '}';
   }
}