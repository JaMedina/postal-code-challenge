package com.jorge.wcc.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.jorge.wcc.security.entrypoint.Http401UnauthorizedEntryPoint;
import com.jorge.wcc.security.handler.AjaxLogoutSuccessHandler;

@Configuration
public class OAuth2ServerConfiguration {

   @Configuration
   @EnableResourceServer
   protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
      @Autowired
      private Http401UnauthorizedEntryPoint authenticationEntryPoint;
      @Autowired
      private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

      @Override
      public void configure(HttpSecurity http) throws Exception {
         http//
         .exceptionHandling()//
               .authenticationEntryPoint(authenticationEntryPoint)//
               .and()//
               .logout()//
               .logoutUrl("/stlr/logout")//
               .logoutSuccessHandler(ajaxLogoutSuccessHandler)//
               .and()//
               .csrf()//
               .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))//
               .disable()//
               .headers()//
               .frameOptions().disable()//
               .sessionManagement()//
               .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
               .and()//
               .authorizeRequests()//
               .antMatchers("/public/**").permitAll()//
               .antMatchers("/wcc/rest/logs/**").hasAnyAuthority(Constants.SECURITY_ADMIN)//
               .antMatchers("/wcc/**").authenticated()//
               .antMatchers("/protected/**").authenticated();//

      }
   }

   @Configuration
   @EnableAuthorizationServer
   protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter implements EnvironmentAware {
      private static final String ENV_OAUTH = "authentication.oauth.";
      private static final String PROP_CLIENTID = "clientid";
      private static final String PROP_SECRET = "secret";
      private static final String PROP_TOKEN_VALIDITY_SECONDS = "tokenValidityInSeconds";

      private RelaxedPropertyResolver propertyResolver;

      @Autowired
      private DataSource dataSource;
      @Autowired
      @Qualifier("authenticationManagerBean")
      private AuthenticationManager authenticationManager;

      @Bean
      public TokenStore tokenStore() {
         return new JdbcTokenStore(dataSource);
      }

      @Override
      public void setEnvironment(Environment environment) {
         this.propertyResolver = new RelaxedPropertyResolver(environment, ENV_OAUTH);
      }

      @Override
      public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
         clients//
               .inMemory()//
               .withClient(propertyResolver.getProperty(PROP_CLIENTID))//
               .scopes("read", "write")//
               .authorities(Constants.SECURITY_ADMIN, Constants.SECURITY_USER)//
               .authorizedGrantTypes("password", "refresh_token")//
               .secret(propertyResolver.getProperty(PROP_SECRET))//
               .accessTokenValiditySeconds(propertyResolver.getProperty(PROP_TOKEN_VALIDITY_SECONDS, Integer.class, 1800));
      }

      @Override
      public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

         endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager);
      }
   }

}
