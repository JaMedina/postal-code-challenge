package com.jorge.wcc.web.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jorge.wcc.domain.Role;
import com.jorge.wcc.domain.User;
import com.jorge.wcc.service.UserService;
import com.jorge.wcc.web.model.UserDTO;

@RestController
@RequestMapping(value = "/wcc/rest", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class UserController {
  @Autowired
  private UserService userService;

  @RequestMapping(value = "/start", method = RequestMethod.GET)
  public ResponseEntity<UserDTO> getCurrentLogin() {
    User user = userService.getUserWithPermissions();
    List<String> permissions = user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList());
    UserDTO currentUser = new UserDTO(user.getName(), user.getPassword(), user.getUsername(), permissions);
    return new ResponseEntity<UserDTO>(currentUser, HttpStatus.OK);
  }

}
