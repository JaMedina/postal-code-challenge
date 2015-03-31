package com.jorge.wcc.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jorge.wcc.domain.User;
import com.jorge.wcc.service.UserService;

@RestController
@RequestMapping(value = "/public/rest", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class PublicActionsController {
  @Autowired
  private UserService userService;

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public ResponseEntity<User> createUser(@RequestParam(value = "username") String username, @RequestParam(value = "name") String name, @RequestParam(value = "password") String password) {
    User user = userService.createUser(username, name, password);
    return new ResponseEntity<>(user, HttpStatus.CREATED);
  }
}
