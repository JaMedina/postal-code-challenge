package com.jorge.wcc.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jorge.wcc.config.Constants;
import com.jorge.wcc.security.utils.SecurityUtils;

public class SecurityUtilsTest {

   @Test
   public void testGetCurrentLogin() {
      SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
      securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
      SecurityContextHolder.setContext(securityContext);
      String login = SecurityUtils.getCurrentLogin();
      assertThat(login).isEqualTo("admin");
   }

   @Test
   public void testIsAuthenticated() {
      SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
      securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
      SecurityContextHolder.setContext(securityContext);
      boolean isAuthenticated = SecurityUtils.isAuthenticated();
      assertThat(isAuthenticated).isTrue();
   }

   @Test
   public void testAnonymousIsNotAuthenticated() {
      SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
      Collection<GrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority(Constants.SECURITY_ANONYMOUS));
      securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("anonymous", "anonymous", authorities));
      SecurityContextHolder.setContext(securityContext);
      boolean isAuthenticated = SecurityUtils.isAuthenticated();
      assertThat(isAuthenticated).isFalse();
   }
}
