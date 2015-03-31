package com.jorge.wcc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jorge.wcc.config.Constants;
import com.jorge.wcc.domain.Role;
import com.jorge.wcc.domain.User;
import com.jorge.wcc.exception.EmptyFieldsException;
import com.jorge.wcc.exception.UserAlreadyExistsException;
import com.jorge.wcc.repository.RoleRepository;
import com.jorge.wcc.repository.UserRepository;
import com.jorge.wcc.security.utils.SecurityUtils;

@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private RoleRepository roleRepository;

  public User createUser(String username, String name, String password) {
    if (username == null || "".equals(username.trim()) || password == null || "".equals(password.trim())) {
      throw new EmptyFieldsException("Some of the requiered fields are empty");
    }

    Role role = roleRepository.findByRoleName(Constants.SECURITY_USER);

    if (userRepository.findByUsername(username) != null)
      throw new UserAlreadyExistsException("The username: " + username + " already exists.");

    User user = new User();
    user.setUsername(username.toLowerCase());
    user.setName(name);
    user.setPassword(passwordEncoder.encode(password));
    user.getRoles().add(role);
    userRepository.save(user);

    log.info("Created Information for User: {}", user);
    return user;
  }

  @Transactional(readOnly = true)
  public User getUserWithPermissions() {
    User currentLogin = userRepository.findByUsername(SecurityUtils.getCurrentLogin());
    // eagerly load the association
    currentLogin.getRoles().size();
    return currentLogin;
  }
}
