package com.jorge.wcc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

import com.jorge.wcc.security.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
   @Autowired
   private UserDetailsService userDetailsService;
   @Autowired
   private Environment environment;

   @Autowired
   public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(environment, "spring.security.");
      if (propertyResolver.getProperty("password-strenght") == null) {
         return new BCryptPasswordEncoder();
      }
      else {
         int strenght = Integer.parseInt(propertyResolver.getProperty("password-strenght"));
         return new BCryptPasswordEncoder(strenght);
      }
   }

   @Override
   public void configure(WebSecurity web) throws Exception {
      web.ignoring()//
            .antMatchers("/bower_components/**")//
            .antMatchers("/fonts/**")//
            .antMatchers("/images/**")//
            .antMatchers("/scripts/**")//
            .antMatchers("/styles/**")//
            .antMatchers("/assets/**")//
            .antMatchers("/views/**")//
            .antMatchers("/public/**")//
            .antMatchers("/console/**");
   }

   @Override
   @Bean
   public AuthenticationManager authenticationManagerBean() throws Exception {
      return super.authenticationManagerBean();
   }

   @EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
   private static class GlobalSecurityConfiguration extends GlobalMethodSecurityConfiguration {

      @Override
      protected MethodSecurityExpressionHandler createExpressionHandler() {
         return new OAuth2MethodSecurityExpressionHandler();
      }
   }
}
