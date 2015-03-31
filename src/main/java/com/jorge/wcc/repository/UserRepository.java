package com.jorge.wcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jorge.wcc.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByUsername(String username);
}
