package com.bacuti.repository;

import com.bacuti.domain.ShipmentLeg;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShipmentLeg entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentLegRepository extends JpaRepository<ShipmentLeg, Long> {}
