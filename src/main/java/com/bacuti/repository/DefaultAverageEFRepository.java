package com.bacuti.repository;

import com.bacuti.domain.DefaultAverageEF;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DefaultAverageEF entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DefaultAverageEFRepository extends JpaRepository<DefaultAverageEF, Long> {

    DefaultAverageEF findByDomain(String domain);
}
