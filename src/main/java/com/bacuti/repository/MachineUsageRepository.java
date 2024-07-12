package com.bacuti.repository;

import com.bacuti.domain.MachineUsage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the MachineUsage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MachineUsageRepository extends JpaRepository<MachineUsage, Long> {

    @Query("SELECT mu FROM MachineUsage mu WHERE mu.site.siteName = :siteName AND mu.unitOfMeasure.name = :unitOfMeasure")
    Optional<MachineUsage> findBySiteMachineUom(String siteName, String unitOfMeasure);

}
