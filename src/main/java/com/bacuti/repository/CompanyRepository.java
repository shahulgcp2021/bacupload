package com.bacuti.repository;

import com.bacuti.domain.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Company entity.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    /**
     * Find an company by its name.
     *
     * @param companyName - Company to search.
     * @return - Returns list of Company.
     */
    Company findByName(String companyName);

    /**
     * Finds whether company exists with the given name
     * @param companyName
     * @return if exists , true else false
     */
    boolean existsByName(String companyName);

    /**
     * Find list of companies based on condition
     * @param name company name to search with
     * @param pageable pagination and sorting
     * @return List of companies
     */
    @Query("SELECT u FROM Company u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Company> findByNameContaining(@Param("name")String name, Pageable pageable);

    /**
     * Method to get the id of an company by its name.
     */
    String GET_COMPANY_ID_BY_NAME_QUERY = "SELECT id FROM company WHERE name = :name";
    @Query(value = GET_COMPANY_ID_BY_NAME_QUERY, nativeQuery = true)
    Long getIdByName(@Param("name") String name);

    /**
     * True if the domain exists
     * @param domain domain name to search
     * @return True if it exists
     */
    boolean existsByDomain(String domain);

}
