package com.jorge.wcc.exception;

public class EmptyFieldsException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public EmptyFieldsException(String message) {
      super(message);
   }
}