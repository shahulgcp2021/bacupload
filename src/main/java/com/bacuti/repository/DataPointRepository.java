package com.bacuti.repository;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bacuti.domain.DataPoint;

/**
 * JPA repository for the DataPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataPointRepository extends JpaRepository<DataPoint, Long> {

    /**
     * Gets all Data point based on order by id.
     *
     * @return list of DataPoint.
     */
    List<DataPoint> findAllByOrderByIdAsc();

    /**
     * Update the isSelected field of the selected data upload operation.
     *
     * @param operationName name of the data upload operation to be updated.
     * @param isSelected boolean to be updated.
     */
    @Modifying
    @Transactional
    @Query("UPDATE DataPoint dp SET dp.isSelected = :isSelected WHERE dp.operation = :operationName")
    int updateIsSelectedByOperation(String operationName, Boolean isSelected);
}
