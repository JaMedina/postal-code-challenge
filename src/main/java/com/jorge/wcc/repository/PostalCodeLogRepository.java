package com.jorge.wcc.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jorge.wcc.domain.PostalCode;
import com.jorge.wcc.domain.PostalCodeCount;
import com.jorge.wcc.domain.PostalCodeLog;

public interface PostalCodeLogRepository extends JpaRepository<PostalCodeLog, Long> {
  List<PostalCodeLog> findByOrigin(PostalCode origin);

  List<PostalCodeLog> findByDestination(PostalCode destination);

  List<PostalCodeLog> findByOriginAndDestination(PostalCode origin, PostalCode destination);

  @Query("SELECT p FROM PostalCodeLog p WHERE p.distance = (SELECT MAX(d.distance) FROM PostalCodeLog d) GROUP BY p.id, p.origin, p.destination")
  List<PostalCodeLog> getLongestDistanceEntries(Pageable pageable);

  @Query("SELECT p FROM PostalCodeLog p WHERE p.distance = (SELECT MIN(d.distance) FROM PostalCodeLog d) GROUP BY p.id, p.origin, p.destination")
  List<PostalCodeLog> getShortestDistanceEntries(Pageable pageable);

  @Query("SELECT p.origin, COUNT(*) FROM PostalCodeLog p GROUP BY p.origin ORDER BY COUNT(*) DESC")
  List<PostalCodeCount> getMostRequestedOrigin(Pageable pageable);

  @Query("SELECT p.destination, COUNT(*) FROM PostalCodeLog p GROUP BY p.destination ORDER BY COUNT(*) DESC")
  List<PostalCodeCount> getMostRequestedDestination(Pageable pageable);

  @Query("SELECT p.origin, p.destination, COUNT(*) FROM PostalCodeLog p GROUP BY p.origin, p.destination ORDER BY COUNT(*) DESC")
  List<PostalCodeCount> getMostRequestedPair(Pageable pageable);
}
