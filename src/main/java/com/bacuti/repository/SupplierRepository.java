package com.bacuti.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bacuti.domain.Supplier;

/**
 * Spring Data JPA repository for the Supplier entity.
 *
 * @author Imran Nazir K
 * @since 2024-06-28
 */
@SuppressWarnings("unused")
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    /**
     * Gets supplier by name.
     *
     * @param supplierName name of the supplier.
     * @return supplier
     */
    Supplier findBySupplierNameIgnoreCase(String supplierName);

    /**
     * Gets all data by pagination.
     *
     * @param searchKey to fetch data.
     * @param pageable for pagination
     * @return list of supplier.
     */
    @Query("SELECT s FROM Supplier s WHERE LOWER(s.supplierName) LIKE LOWER(CONCAT('%', :searchKey, '%'))")
    Page<Supplier> findBySupplierNameLikeIgnoreCase(@Param("searchKey") String searchKey, Pageable pageable);

    @Query("SELECT s.supplierName FROM Supplier s")
    List<String> fetchAllSupplierNames();
}
