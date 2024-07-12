package com.bacuti.repository;

import com.bacuti.domain.ShipmentLane;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShipmentLane entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentLaneRepository extends JpaRepository<ShipmentLane, Long> {}
