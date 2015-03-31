package com.jorge.wcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jorge.wcc.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
   Role findByRoleName(String roleName);
}
