package com.bacuti.web.rest;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.domain.Site;
import com.bacuti.repository.SiteRepository;
import com.bacuti.service.SiteService;
import com.bacuti.service.dto.SiteDTO;
import com.bacuti.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link Site}.
 */
@RestController
@RequestMapping("/api/${app.api.version}/sites")
@Transactional
public class SiteResource {

    private final Logger log = LoggerFactory.getLogger(SiteResource.class);

    private static final String ENTITY_NAME = "uploadServiceSite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SiteService siteService;

    private final SiteRepository siteRepository;

    public SiteResource(SiteService siteService, SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
        this.siteService = siteService;
    }

    /**
     * {@code POST  /sites} : Create a new site.
     *
     * @param siteDTO the site to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new site, or with status {@code 400 (Bad Request)} if the site has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SiteDTO> createSite(@Valid @RequestBody SiteDTO siteDTO) throws URISyntaxException {
        log.debug("REST request to save Site : {}", siteDTO);
        if (siteDTO.getId() != null) {
            throw new BusinessException("A new site cannot already have an ID", HttpStatus.BAD_REQUEST.value());
        }
        siteDTO = siteService.saveSite(siteDTO);
        log.debug("The site is created");
        return ResponseEntity.created(new URI("/api/sites/" + siteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, siteDTO.getId().toString()))
            .body(siteDTO);
    }

    /**
     * {@code PUT  /sites/:id} : Updates an existing site.
     *
     * @param id the id of the site to save.
     * @param siteDTO the site to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated site,
     * or with status {@code 400 (Bad Request)} if the site is not valid,
     * or with status {@code 500 (Internal Server Error)} if the site couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SiteDTO> updateSite(@PathVariable(value = "id") final Long id, @Valid @RequestBody SiteDTO siteDTO)
        throws URISyntaxException {
        log.debug("REST request to update Site : {}, {}", id, siteDTO);
        if (siteDTO.getId() == null || !Objects.equals(id, siteDTO.getId())) {
            throw new BusinessException("Invalid id", HttpStatus.NOT_FOUND.value());
        }

        siteDTO = siteService.saveSite(siteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, siteDTO.getId().toString()))
            .body(siteDTO);
    }

    /**
     * {@code PATCH  /sites/:id} : Partial updates given fields of an existing site, field will ignore if it is null
     *
     * @param id the id of the site to save.
     * @param site the site to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated site,
     * or with status {@code 400 (Bad Request)} if the site is not valid,
     * or with status {@code 404 (Not Found)} if the site is not found,
     * or with status {@code 500 (Internal Server Error)} if the site couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Site> partialUpdateSite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Site site
    ) throws URISyntaxException {
        log.debug("REST request to partial update Site partially : {}, {}", id, site);
        if (site.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, site.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!siteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Site> result = siteRepository
            .findById(site.getId())
            .map(existingSite -> {
                if (site.getSiteName() != null) {
                    existingSite.setSiteName(site.getSiteName());
                }
                if (site.getDescription() != null) {
                    existingSite.setDescription(site.getDescription());
                }
                if (site.getManufacturingSite() != null) {
                    existingSite.setManufacturingSite(site.getManufacturingSite());
                }
                if (site.getEmployeeCount() != null) {
                    existingSite.setEmployeeCount(site.getEmployeeCount());
                }
                if (site.getCbamImpacted() != null) {
                    existingSite.setCbamImpacted(site.getCbamImpacted());
                }
                if (site.getCountry() != null) {
                    existingSite.setCountry(site.getCountry());
                }
                if (site.getState() != null) {
                    existingSite.setState(site.getState());
                }
                if (site.getAddress() != null) {
                    existingSite.setAddress(site.getAddress());
                }
                if (site.getLattitude() != null) {
                    existingSite.setLattitude(site.getLattitude());
                }
                if (site.getLongitude() != null) {
                    existingSite.setLongitude(site.getLongitude());
                }
                if (site.getUnlocode() != null) {
                    existingSite.setUnlocode(site.getUnlocode());
                }
                if (site.getDataQualityDesc() != null) {
                    existingSite.setDataQualityDesc(site.getDataQualityDesc());
                }
                if (site.getDefaultValueUsageJustfn() != null) {
                    existingSite.setDefaultValueUsageJustfn(site.getDefaultValueUsageJustfn());
                }
                if (site.getDataQAInfo() != null) {
                    existingSite.setDataQAInfo(site.getDataQAInfo());
                }
                if (site.getDefaultHeatNumber() != null) {
                    existingSite.setDefaultHeatNumber(site.getDefaultHeatNumber());
                }

                return existingSite;
            })
            .map(siteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, site.getId().toString())
        );
    }

    /**
     * {@code GET  /sites} : get all the sites.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sites in body.
     */
    @GetMapping("")
    public ResponseEntity<Page<SiteDTO>> getSites(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "lastModifiedDate") String sortBy,
        @RequestParam(defaultValue = "desc") String direction,
        @RequestParam(defaultValue = "") String search) {
        Page<SiteDTO> sites = siteService.getSites(page, size, sortBy, direction, search);
        return ResponseEntity.ok(sites);
    }

    /**
     * {@code GET  /sites/:id} : get the "id" site.
     *
     * @param id the id of the site to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the site, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SiteDTO> getSite(@PathVariable("id") Long id) {
        log.debug("REST request to get Site : {}", id);
        if (id == null) {
            log.error("Site id is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        SiteDTO siteDTO = siteService.findById(id);
        return new ResponseEntity<>(siteDTO, HttpStatus.OK);
    }

    /**
     * {@code DELETE  /sites/:id} : delete the "id" site.
     *
     * @param id the id of the site to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteSite(@PathVariable("id") Long id) {
        log.debug("REST request to delete Site : {}", id);
        siteService.deleteById(id);
        HttpHeaders headers = HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString());
        return ResponseEntity.ok().body(true);
    }

    /**
     * {@code GET  /all-site} : get all the site name from site entity
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of items in body.
     */
    @GetMapping("/all-site")
    public ResponseEntity<List<String>> getAllSites() {
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(siteService.fetchAllSiteNames()));
    }
}
