package com.bacuti.repository;

import com.bacuti.domain.PurchasedQty;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PurchasedQty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchasedQtyRepository extends JpaRepository<PurchasedQty, Long> {}
