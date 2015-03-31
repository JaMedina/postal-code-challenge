package com.jorge.wcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jorge.wcc.domain.PostalCode;

/**
 * Spring Data JPA repository for the PostalCode entity.
 */
public interface PostalCodeRepository extends JpaRepository<PostalCode, Long> {
  PostalCode findByPostalCode(String postalCode);
}
