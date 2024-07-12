package com.bacuti.repository;

import com.bacuti.domain.Machine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Machine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {

    Machine findByMachineName(String machineName);

    @Query("SELECT u FROM Machine u WHERE LOWER(u.machineName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Machine> findByMachineNameContaining(@Param("name")String name, Pageable pageable);
}
