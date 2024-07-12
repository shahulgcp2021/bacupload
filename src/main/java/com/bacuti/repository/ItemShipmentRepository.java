package com.bacuti.repository;

import com.bacuti.domain.ItemShipment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ItemShipment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemShipmentRepository extends JpaRepository<ItemShipment, Long> {}
