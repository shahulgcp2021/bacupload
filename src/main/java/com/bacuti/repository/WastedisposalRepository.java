package com.bacuti.repository;

import com.bacuti.domain.Wastedisposal;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Wastedisposal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WastedisposalRepository extends JpaRepository<Wastedisposal, Long> {}
