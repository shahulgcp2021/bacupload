package com.bacuti.repository;

import com.bacuti.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT u FROM Customer u WHERE LOWER(u.customerName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Customer> findByCustomerNameContaining(@Param("name")String name, Pageable pageable);

    /**
     * Gets Customer by name.
     *
     * @param customerName of the Customer.
     * @return Customer as optional.
     */
    Optional<Customer> findByCustomerNameIgnoreCase(String customerName);
}
