package com.bacuti.web.rest;

import com.bacuti.domain.DefaultAverageEF;
import com.bacuti.repository.DefaultAverageEFRepository;
import com.bacuti.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bacuti.domain.DefaultAverageEF}.
 */
@RestController
@RequestMapping("/api/default-average-efs")
@Transactional
public class DefaultAverageEFResource {

    private final Logger log = LoggerFactory.getLogger(DefaultAverageEFResource.class);

    private static final String ENTITY_NAME = "uploadServiceDefaultAverageEf";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DefaultAverageEFRepository defaultAverageEFRepository;

    public DefaultAverageEFResource(DefaultAverageEFRepository defaultAverageEFRepository) {
        this.defaultAverageEFRepository = defaultAverageEFRepository;
    }

    /**
     * {@code POST  /default-average-efs} : Create a new defaultAverageEF.
     *
     * @param defaultAverageEF the defaultAverageEF to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new defaultAverageEF, or with status {@code 400 (Bad Request)} if the defaultAverageEF has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DefaultAverageEF> createDefaultAverageEF(@RequestBody DefaultAverageEF defaultAverageEF)
        throws URISyntaxException {
        log.debug("REST request to save DefaultAverageEF : {}", defaultAverageEF);
        if (defaultAverageEF.getId() != null) {
            throw new BadRequestAlertException("A new defaultAverageEF cannot already have an ID", ENTITY_NAME, "idexists");
        }
        defaultAverageEF = defaultAverageEFRepository.save(defaultAverageEF);
        return ResponseEntity.created(new URI("/api/default-average-efs/" + defaultAverageEF.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, defaultAverageEF.getId().toString()))
            .body(defaultAverageEF);
    }

    /**
     * {@code PUT  /default-average-efs/:id} : Updates an existing defaultAverageEF.
     *
     * @param id the id of the defaultAverageEF to save.
     * @param defaultAverageEF the defaultAverageEF to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated defaultAverageEF,
     * or with status {@code 400 (Bad Request)} if the defaultAverageEF is not valid,
     * or with status {@code 500 (Internal Server Error)} if the defaultAverageEF couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DefaultAverageEF> updateDefaultAverageEF(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DefaultAverageEF defaultAverageEF
    ) throws URISyntaxException {
        log.debug("REST request to update DefaultAverageEF : {}, {}", id, defaultAverageEF);
        if (defaultAverageEF.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, defaultAverageEF.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!defaultAverageEFRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        defaultAverageEF = defaultAverageEFRepository.save(defaultAverageEF);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, defaultAverageEF.getId().toString()))
            .body(defaultAverageEF);
    }

    /**
     * {@code PATCH  /default-average-efs/:id} : Partial updates given fields of an existing defaultAverageEF, field will ignore if it is null
     *
     * @param id the id of the defaultAverageEF to save.
     * @param defaultAverageEF the defaultAverageEF to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated defaultAverageEF,
     * or with status {@code 400 (Bad Request)} if the defaultAverageEF is not valid,
     * or with status {@code 404 (Not Found)} if the defaultAverageEF is not found,
     * or with status {@code 500 (Internal Server Error)} if the defaultAverageEF couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DefaultAverageEF> partialUpdateDefaultAverageEF(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DefaultAverageEF defaultAverageEF
    ) throws URISyntaxException {
        log.debug("REST request to partial update DefaultAverageEF partially : {}, {}", id, defaultAverageEF);
        if (defaultAverageEF.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, defaultAverageEF.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!defaultAverageEFRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DefaultAverageEF> result = defaultAverageEFRepository
            .findById(defaultAverageEF.getId())
            .map(existingDefaultAverageEF -> {
                if (defaultAverageEF.getDomain() != null) {
                    existingDefaultAverageEF.setDomain(defaultAverageEF.getDomain());
                }
                if (defaultAverageEF.getDetail() != null) {
                    existingDefaultAverageEF.setDetail(defaultAverageEF.getDetail());
                }
                if (defaultAverageEF.getCountryOrRegion() != null) {
                    existingDefaultAverageEF.setCountryOrRegion(defaultAverageEF.getCountryOrRegion());
                }
                if (defaultAverageEF.getEmissionFactor() != null) {
                    existingDefaultAverageEF.setEmissionFactor(defaultAverageEF.getEmissionFactor());
                }
                if (defaultAverageEF.getEfSource() != null) {
                    existingDefaultAverageEF.setEfSource(defaultAverageEF.getEfSource());
                }
                if (defaultAverageEF.getCode() != null) {
                    existingDefaultAverageEF.setCode(defaultAverageEF.getCode());
                }
                if (defaultAverageEF.getCodeType() != null) {
                    existingDefaultAverageEF.setCodeType(defaultAverageEF.getCodeType());
                }

                return existingDefaultAverageEF;
            })
            .map(defaultAverageEFRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, defaultAverageEF.getId().toString())
        );
    }

    /**
     * {@code GET  /default-average-efs} : get all the defaultAverageEFS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of defaultAverageEFS in body.
     */
    @GetMapping("")
    public List<DefaultAverageEF> getAllDefaultAverageEFS() {
        log.debug("REST request to get all DefaultAverageEFS");
        return defaultAverageEFRepository.findAll();
    }

    /**
     * {@code GET  /default-average-efs/:id} : get the "id" defaultAverageEF.
     *
     * @param id the id of the defaultAverageEF to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the defaultAverageEF, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DefaultAverageEF> getDefaultAverageEF(@PathVariable("id") Long id) {
        log.debug("REST request to get DefaultAverageEF : {}", id);
        Optional<DefaultAverageEF> defaultAverageEF = defaultAverageEFRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(defaultAverageEF);
    }

    /**
     * {@code DELETE  /default-average-efs/:id} : delete the "id" defaultAverageEF.
     *
     * @param id the id of the defaultAverageEF to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDefaultAverageEF(@PathVariable("id") Long id) {
        log.debug("REST request to delete DefaultAverageEF : {}", id);
        defaultAverageEFRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
