package com.bacuti.service;

import com.bacuti.service.dto.SiteFinishedGoodDTO;
import org.springframework.data.domain.Page;

public interface SiteFinishedGoodService extends UploadService {
    /**
     * Saves SiteFinishedGood from siteFinishedGoodDTO
     *
     * @param siteFinishedGoodDTO, the dto which will saved.
     * @return SiteFinishedGoodDTO object
     */
    SiteFinishedGoodDTO saveSiteFinishedGood(SiteFinishedGoodDTO siteFinishedGoodDTO);

    /**
     * Get SiteFinishedGoodDTO by id
     *
     * @param id, to retrieve SiteFinishedGood by unique key
     * @return SiteFinishedGoodDTO object
     */
    SiteFinishedGoodDTO getSiteFinishedGoodById(long id);

    /**
     * Get SiteFinishedGoodDTO by id
     *
     * @param id, to delete SiteFinishedGood by unique key
     * @return a boolean for confirmation
     */
    boolean deleteSiteFinishedGoodById(long id);

    /**
     * Method used to fetch the SiteFinishedGood details by paginating the response.
     *
     * @param pageNo        - Page number to fetch data.
     * @param pageSize      - Page size of screen
     * @param sortBy        - Data sorting column
     * @param sortDirection - Data sort direction
     * @param searchString  - the name to be searched alike
     * @return Returns a List SiteFinishedGoods dto
     */
    Page<SiteFinishedGoodDTO> getSiteFinishedGoods(int pageNo, int pageSize, String sortBy, String sortDirection, String searchString);

}
