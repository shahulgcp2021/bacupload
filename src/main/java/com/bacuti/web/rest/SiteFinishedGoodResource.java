package com.bacuti.web.rest;

import com.bacuti.service.SiteFinishedGoodService;
import com.bacuti.service.dto.SiteFinishedGoodDTO;
import com.bacuti.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link com.bacuti.domain.SiteFinishedGood}.
 */
@RestController
@RequestMapping("/api/${app.api.version}/site-finished-goods")
@Transactional
public class SiteFinishedGoodResource {

    private final Logger log = LoggerFactory.getLogger(SiteFinishedGoodResource.class);

    private static final String ENTITY_NAME = "uploadServiceSiteFinishedGood";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SiteFinishedGoodService siteFinishedGoodService;

    public SiteFinishedGoodResource(SiteFinishedGoodService siteFinishedGoodService) {
        this.siteFinishedGoodService = siteFinishedGoodService;
    }

    /**
     * {@code POST  /site-finished-goods} : Create a new siteFinishedGood.
     *
     * @param siteFinishedGoodDTO the siteFinishedGood to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new siteFinishedGood, or with status {@code 400 (Bad Request)} if the siteFinishedGood has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    public ResponseEntity<SiteFinishedGoodDTO> createSiteFinishedGood(@RequestBody SiteFinishedGoodDTO siteFinishedGoodDTO)
        throws URISyntaxException {
        log.debug("REST request to save SiteFinishedGood : {}", siteFinishedGoodDTO);
        if (siteFinishedGoodDTO.getId() != null) {
            throw new BadRequestAlertException("A new siteFinishedGood cannot already have an ID", ENTITY_NAME, "idexists");
        }
        siteFinishedGoodDTO = siteFinishedGoodService.saveSiteFinishedGood(siteFinishedGoodDTO);
        return ResponseEntity.created(new URI("/api/site-finished-goods/" + siteFinishedGoodDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, siteFinishedGoodDTO.getId().toString()))
            .body(siteFinishedGoodDTO);
    }

    /**
     * {@code PUT  /site-finished-goods/:id} : Updates an existing siteFinishedGood.
     *
     * @param id                  the id of the siteFinishedGood to save.
     * @param siteFinishedGoodDTO the siteFinishedGood to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siteFinishedGood,
     * or with status {@code 400 (Bad Request)} if the siteFinishedGood is not valid,
     * or with status {@code 500 (Internal Server Error)} if the siteFinishedGood couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SiteFinishedGoodDTO> updateSiteFinishedGood(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SiteFinishedGoodDTO siteFinishedGoodDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SiteFinishedGood : {}, {}", id, siteFinishedGoodDTO);
        if (siteFinishedGoodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, siteFinishedGoodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        siteFinishedGoodDTO = siteFinishedGoodService.saveSiteFinishedGood(siteFinishedGoodDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, siteFinishedGoodDTO.getId().toString()))
            .body(siteFinishedGoodDTO);
    }

    /**
     * {@code GET  /site-finished-goods} : get all the siteFinishedGoods.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of siteFinishedGoods in body.
     */
    @GetMapping
    public ResponseEntity<Page<SiteFinishedGoodDTO>> getCapitalGoods(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "lastModifiedDate") String sortBy,
        @RequestParam(defaultValue = "desc") String direction,
        @RequestParam(defaultValue = "") String search) {
        Page<SiteFinishedGoodDTO> siteFinishedGoods = siteFinishedGoodService.getSiteFinishedGoods(page, size, sortBy, direction, search);
        return ResponseEntity.ok(siteFinishedGoods);
    }

    /**
     * {@code GET  /site-finished-goods/:id} : get the "id" siteFinishedGood.
     *
     * @param id the id of the siteFinishedGood to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the siteFinishedGood, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SiteFinishedGoodDTO> getSiteFinishedGood(@PathVariable("id") Long id) {
        log.debug("REST request to get SiteFinishedGood : {}", id);
        SiteFinishedGoodDTO siteFinishedGood = siteFinishedGoodService.getSiteFinishedGoodById(id);
        return new ResponseEntity<>(siteFinishedGood, HttpStatus.OK);
    }

    /**
     * {@code DELETE  /site-finished-goods/:id} : delete the "id" siteFinishedGood.
     *
     * @param id the id of the siteFinishedGood to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSiteFinishedGood(@PathVariable("id") Long id) {
        log.debug("REST request to delete SiteFinishedGood : {}", id);
        siteFinishedGoodService.deleteSiteFinishedGoodById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
