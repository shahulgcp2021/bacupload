package com.bacuti.repository;

import com.bacuti.domain.SiteFinishedGood;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SiteFinishedGood entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SiteFinishedGoodRepository extends JpaRepository<SiteFinishedGood, Long> {
    @Query("SELECT sfg FROM SiteFinishedGood sfg JOIN sfg.site s JOIN sfg.finishedGood i WHERE LOWER(s.siteName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(i.itemName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<SiteFinishedGood> findBySiteNameOrItemNameLikeIgnoreCase(@Param("name") String name, Pageable pageable);

    @Query("SELECT COUNT(sfg) > 0 from SiteFinishedGood sfg JOIN sfg.site s JOIN sfg.finishedGood i WHERE s.id = :siteId AND i.id = :itemId")
    boolean checkSiteFinishedGoodExistsBySiteIdAndFinishedGoodId(Long siteId, Long itemId);

    @Query("SELECT COUNT(sfg) > 0 from SiteFinishedGood sfg JOIN sfg.site s JOIN sfg.finishedGood i WHERE LOWER(s.siteName) = LOWER(:siteName) AND LOWER(i.itemName) = LOWER(:itemName)")
    boolean checkSiteFinishedGoodExistsBySiteNameAndFinishedGoodName(String siteName, String itemName);
}
