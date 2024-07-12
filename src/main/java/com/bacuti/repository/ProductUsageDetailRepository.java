package com.bacuti.repository;

import com.bacuti.domain.ProductUsageDetail;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductUsageDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductUsageDetailRepository extends JpaRepository<ProductUsageDetail, Long> {}
