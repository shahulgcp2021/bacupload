package com.bacuti.repository;

import com.bacuti.domain.CarbonPricePayment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CarbonPricePayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarbonPricePaymentRepository extends JpaRepository<CarbonPricePayment, Long> {}
