package com.bacuti.repository;

import com.bacuti.domain.CapitalGood;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CapitalGood entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CapitalGoodRepository extends JpaRepository<CapitalGood, Long> {}
