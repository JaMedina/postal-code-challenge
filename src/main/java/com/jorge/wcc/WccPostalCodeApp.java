package com.jorge.wcc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import com.jorge.wcc.config.Constants;

@ComponentScan
@EnableAutoConfiguration
public class WccPostalCodeApp {
   private static final Logger log = LoggerFactory.getLogger(WccPostalCodeApp.class);

   @Autowired
   private Environment env;

   public static void main(String... args) throws UnknownHostException {
      SpringApplication app = new SpringApplication(WccPostalCodeApp.class);
      app.setShowBanner(false);

      SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
      // Check if the selected profile has been set as argument.
      // if not the development profile will be added
      addDefaultProfile(app, source);
      Environment env = app.run(args).getEnvironment();

      log.info("Access URLs:\n----------------------------------------------------------\n\t" + //
            "Local: \t\thttp://127.0.0.1:{}\n\t" + //
            "External: \thttp://{}:{}\n----------------------------------------------------------",//
            env.getProperty("server.port"),//
            InetAddress.getLocalHost().getHostAddress(),//
            env.getProperty("server.port"));
   }

   private static void addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
      if (!source.containsProperty("spring.profiles.active")) {
         app.setAdditionalProfiles(Constants.SPRING_PROFILE_DEVELOPMENT);
      }
   }

   @PostConstruct
   public void initApplication() throws IOException {
      if (env.getActiveProfiles().length == 0) {
         log.warn("No Spring profile configured, running with default configuration");
      }
      else {
         log.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
      }
   }
}
