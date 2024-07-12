package com.bacuti.repository;

import com.bacuti.domain.ProductionQty;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductionQty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductionQtyRepository extends JpaRepository<ProductionQty, Long> {}
