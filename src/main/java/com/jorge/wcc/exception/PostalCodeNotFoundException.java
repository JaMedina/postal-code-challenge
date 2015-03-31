package com.jorge.wcc.exception;

public class PostalCodeNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public PostalCodeNotFoundException(String postalCode) {
    super("The postalCode " + postalCode + " was not found in the database.");
  }
}
