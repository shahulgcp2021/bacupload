package com.bacuti.repository;

import com.bacuti.domain.EnergySource;
import com.bacuti.domain.Site;
import com.bacuti.domain.enumeration.EnergyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the EnergySource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnergySourceRepository extends JpaRepository<EnergySource, Long> {

    EnergySource findByEnergyType(EnergyType energyType);

    @Query("SELECT u FROM EnergySource u WHERE LOWER(u.energyType) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<EnergySource> findByEnergyTypeContaining(@Param("name")String name, Pageable pageable);

    @Query("SELECT u FROM EnergySource u WHERE LOWER(u.site.siteName) LIKE LOWER(CONCAT('%', :siteName, '%')) " +
        "and LOWER(u.energyType) LIKE LOWER(CONCAT('%', :energyType, '%'))")
    EnergySource findByEnergyTypeAndSite(String energyType, String siteName);

    @Query("SELECT es FROM EnergySource es WHERE es.energyType = :energyType AND es.site = :site AND LOWER(es.supplier) = LOWER(:supplier)")
    List<EnergySource> findBy(@Param("energyType") EnergyType energyType, @Param("site") Site site, @Param("supplier") String supplier);

    @Query("SELECT es FROM EnergySource es WHERE es.energyType = :energyType AND es.site.id = :siteId")
    Optional<EnergySource> findByEnergyTypeAndSiteId(@Param("energyType") EnergyType energyType, Long siteId);
}
