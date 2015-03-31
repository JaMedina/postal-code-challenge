package com.jorge.wcc.security.utils;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.jorge.wcc.config.Constants;

public class SecurityUtils {
   private SecurityUtils() {
   }

   /**
    * Get the login of the current user.
    */
   public static String getCurrentLogin() {
      SecurityContext securityContext = SecurityContextHolder.getContext();
      Authentication authentication = securityContext.getAuthentication();
      UserDetails springSecurityUser = null;
      String userName = null;

      if (authentication != null) {
         if (authentication.getPrincipal() instanceof UserDetails) {
            springSecurityUser = (UserDetails) authentication.getPrincipal();
            userName = springSecurityUser.getUsername();
         }
         else if (authentication.getPrincipal() instanceof String) {
            userName = (String) authentication.getPrincipal();
         }
      }

      return userName;
   }

   /**
    * Check if a user is authenticated.
    *
    * @return true if the user is authenticated, false otherwise
    */
   public static boolean isAuthenticated() {
      SecurityContext securityContext = SecurityContextHolder.getContext();

      final Collection<? extends GrantedAuthority> authorities = securityContext.getAuthentication().getAuthorities();

      if (authorities != null) {
         for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(Constants.SECURITY_ANONYMOUS)) {
               return false;
            }
         }
      }

      return true;
   }
}