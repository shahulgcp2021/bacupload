package com.bacuti.repository;

import com.bacuti.domain.AggregateEnergyUsage;
import com.bacuti.domain.enumeration.EnergyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the AggregateEnergyUsage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AggregateEnergyUsageRepository extends JpaRepository<AggregateEnergyUsage, Long> {

    @Query("SELECT ae FROM AggregateEnergyUsage ae WHERE ae.site.id = :siteName " +
        "AND ae.energySource.id = :energyType AND ae.unitOfMeasure.id = :unitOfMeasure")
    Optional<AggregateEnergyUsage> findBySiteEnergyTypeUom(Long siteName, Long energyType, Long unitOfMeasure);

    @Query("SELECT aeu FROM AggregateEnergyUsage aeu JOIN aeu.site s WHERE s.siteName = :name")
    Page<AggregateEnergyUsage> findBySiteNameLikeIgnoreCase(@Param("name") String name, Pageable pageable);
}
