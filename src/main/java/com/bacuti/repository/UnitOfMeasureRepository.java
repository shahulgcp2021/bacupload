package com.bacuti.repository;

import com.bacuti.domain.UnitOfMeasure;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UnitOfMeasure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnitOfMeasureRepository extends JpaRepository<UnitOfMeasure, Long> {
    UnitOfMeasure findByName(String name);

    UnitOfMeasure findByValueIgnoreCase(String value);
}
