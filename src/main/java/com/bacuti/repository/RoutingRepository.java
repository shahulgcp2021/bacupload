package com.bacuti.repository;

import com.bacuti.domain.Routing;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Routing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoutingRepository extends JpaRepository<Routing, Long> {}
