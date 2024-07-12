package com.bacuti.repository;

import com.bacuti.domain.ItemSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ItemSupplier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemSupplierRepository extends JpaRepository<ItemSupplier, Long> {

    @Query("SELECT u FROM ItemSupplier u WHERE LOWER(u.supplierOwnItem) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<ItemSupplier> findBySupplierOwnItemContaining(@Param("name")String name, Pageable pageable);

    ItemSupplier findBySupplierIdAndItemId(Long supplierId, Long itemId);

    ItemSupplier findBySupplierOwnItem(String supplierOwnItem);
}
