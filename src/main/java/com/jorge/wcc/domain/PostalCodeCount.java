package com.jorge.wcc.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "postalcod-count")
@XmlAccessorType(XmlAccessType.FIELD)
public class PostalCodeCount implements Serializable {
  private static final long serialVersionUID = 1L;

  private PostalCode postalCode;
  private Integer count;

  public PostalCode getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(PostalCode postalCode) {
    this.postalCode = postalCode;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

}
