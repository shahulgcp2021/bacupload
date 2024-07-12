package com.bacuti.repository;

import com.bacuti.domain.EmployeeTravelAvg;
import com.bacuti.domain.enumeration.TravelType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmployeeTravelAvg entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeTravelAvgRepository extends JpaRepository<EmployeeTravelAvg, Long> {


    Page<EmployeeTravelAvg> findByTravelType(Pageable pageable, TravelType travelType);

    @Query("SELECT e FROM EmployeeTravelAvg e JOIN e.site s WHERE e.travelType = :travelType AND s.siteName LIKE %:siteName%")
    Page<EmployeeTravelAvg> findByTravelTypeAndSiteContaining(Pageable pageable, TravelType travelType, String siteName);
}
