package com.bacuti.repository;

import com.bacuti.domain.CompanyPublicEmission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CompanyPublicEmission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyPublicEmissionRepository extends JpaRepository<CompanyPublicEmission, Long> {

    CompanyPublicEmission findByReportingCompany(String reportingCompany);
}
