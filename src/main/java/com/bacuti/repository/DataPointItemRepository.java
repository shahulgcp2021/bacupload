package com.bacuti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bacuti.domain.DataPointItem;

/**
 *  JPA repository for the DataPointItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataPointItemRepository extends JpaRepository<DataPointItem, Long> {
}
