package com.jorge.wcc.security;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.jorge.wcc.domain.Role;
import com.jorge.wcc.domain.User;
import com.jorge.wcc.exception.UsernameNotFoundException;
import com.jorge.wcc.repository.UserRepository;

@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
  private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(final String username) {
    log.debug("Authenticating {}", username);
    String lowercaseUsername = username.toLowerCase();

    User user = userRepository.findByUsername(lowercaseUsername);

    if (user == null) {
      throw new UsernameNotFoundException(lowercaseUsername);
    }

    Collection<GrantedAuthority> granthedRoles = new ArrayList<>();
    for (Role role : user.getRoles()) {
      GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRoleName());
      granthedRoles.add(grantedAuthority);
    }

    return new org.springframework.security.core.userdetails.User(lowercaseUsername, user.getPassword(), granthedRoles);
  }

}
