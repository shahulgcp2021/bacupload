package com.bacuti.service;

import com.bacuti.service.dto.SiteDTO;
import org.springframework.data.domain.Page;
import java.util.List;

public interface SiteService  extends UploadService {
    SiteDTO saveSite(SiteDTO siteDTO);
    Page<SiteDTO> getSites(int page, int size, String sortBy, String direction, String searchString);
    SiteDTO findById(Long id);
    Boolean deleteById(Long id);

    /**
     * Fetch All Site name
     *
     * @return - Returns list of site name.
     */
    List<String> fetchAllSiteNames();
    SiteDTO getSiteBySiteName(String siteName);
}
