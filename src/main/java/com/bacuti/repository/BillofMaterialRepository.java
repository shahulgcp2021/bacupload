package com.bacuti.repository;

import com.bacuti.domain.BillofMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the BillofMaterial entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillofMaterialRepository extends JpaRepository<BillofMaterial, Long> {

    @Query("SELECT u FROM BillofMaterial u WHERE LOWER(u.product.itemName) LIKE LOWER(CONCAT('%', :itemName, '%'))")
    Page<BillofMaterial> findByProductItemNameContaining(@Param("itemName")String itemName, Pageable pageable);

    @Query("SELECT b FROM BillofMaterial b WHERE LOWER(b.product.itemName) = LOWER(:productName) AND LOWER(b.component.itemName) = LOWER(:componentName)")
    Optional<BillofMaterial> findByProductAndComponentItemNames(String productName, String componentName);

}
