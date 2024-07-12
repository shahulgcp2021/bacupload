package com.bacuti.repository;

import com.bacuti.domain.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Site entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {

    @Query("SELECT u FROM Site u WHERE LOWER(u.siteName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Site> findBySiteNameLikeIgnoreCase(@Param("name") String name, Pageable pageable);

    Site findBySiteNameIgnoreCase(String siteName);

    @Query("SELECT s.siteName FROM Site s")
    List<String> fetchAllSiteNames();

    /**
     * Finds whether site exists with the given name
     * @param siteName name of the site
     * @return if exists , true else false
     */
    @Query("SELECT COUNT(s) > 0 FROM Site s WHERE LOWER(s.siteName) = LOWER(:siteName)")
    boolean existsBySiteName(@Param("siteName") String siteName);

    /**
     * Method to get the id of an site by its name.
     */
    String GET_SITE_ID_BY_NAME_QUERY = "SELECT s.id FROM Site s WHERE LOWER(s.siteName) = LOWER(:siteName)";
    @Query(value = GET_SITE_ID_BY_NAME_QUERY)
    Long getIdBySiteName(@Param("siteName") String siteName);

}
