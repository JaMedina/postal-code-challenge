package com.jorge.wcc.exception;

public class UsernameNotFoundException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public UsernameNotFoundException(String username) {
      super("The user " + username + " does not exist.");
   }
}