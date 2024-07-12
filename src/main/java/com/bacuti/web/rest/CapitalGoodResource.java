package com.bacuti.web.rest;

import com.bacuti.domain.CapitalGood;
import com.bacuti.repository.CapitalGoodRepository;
import com.bacuti.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.bacuti.domain.CapitalGood}.
 */
@RestController
@RequestMapping("/api/capital-goods")
@Transactional
public class CapitalGoodResource {

    private final Logger log = LoggerFactory.getLogger(CapitalGoodResource.class);

    private static final String ENTITY_NAME = "uploadServiceCapitalGood";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CapitalGoodRepository capitalGoodRepository;

    public CapitalGoodResource(CapitalGoodRepository capitalGoodRepository) {
        this.capitalGoodRepository = capitalGoodRepository;
    }

    /**
     * {@code POST  /capital-goods} : Create a new capitalGood.
     *
     * @param capitalGood the capitalGood to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new capitalGood, or with status {@code 400 (Bad Request)} if the capitalGood has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CapitalGood> createCapitalGood(@Valid @RequestBody CapitalGood capitalGood) throws URISyntaxException {
        log.debug("REST request to save CapitalGood : {}", capitalGood);
        if (capitalGood.getId() != null) {
            throw new BadRequestAlertException("A new capitalGood cannot already have an ID", ENTITY_NAME, "idexists");
        }
        capitalGood = capitalGoodRepository.save(capitalGood);
        return ResponseEntity.created(new URI("/api/capital-goods/" + capitalGood.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, capitalGood.getId().toString()))
            .body(capitalGood);
    }

    /**
     * {@code PUT  /capital-goods/:id} : Updates an existing capitalGood.
     *
     * @param id the id of the capitalGood to save.
     * @param capitalGood the capitalGood to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capitalGood,
     * or with status {@code 400 (Bad Request)} if the capitalGood is not valid,
     * or with status {@code 500 (Internal Server Error)} if the capitalGood couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CapitalGood> updateCapitalGood(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CapitalGood capitalGood
    ) throws URISyntaxException {
        log.debug("REST request to update CapitalGood : {}, {}", id, capitalGood);
        if (capitalGood.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capitalGood.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!capitalGoodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        capitalGood = capitalGoodRepository.save(capitalGood);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, capitalGood.getId().toString()))
            .body(capitalGood);
    }

    /**
     * {@code PATCH  /capital-goods/:id} : Partial updates given fields of an existing capitalGood, field will ignore if it is null
     *
     * @param id the id of the capitalGood to save.
     * @param capitalGood the capitalGood to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capitalGood,
     * or with status {@code 400 (Bad Request)} if the capitalGood is not valid,
     * or with status {@code 404 (Not Found)} if the capitalGood is not found,
     * or with status {@code 500 (Internal Server Error)} if the capitalGood couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CapitalGood> partialUpdateCapitalGood(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CapitalGood capitalGood
    ) throws URISyntaxException {
        log.debug("REST request to partial update CapitalGood partially : {}, {}", id, capitalGood);
        if (capitalGood.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capitalGood.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!capitalGoodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CapitalGood> result = capitalGoodRepository
            .findById(capitalGood.getId())
            .map(existingCapitalGood -> {
                if (capitalGood.getAssetName() != null) {
                    existingCapitalGood.setAssetName(capitalGood.getAssetName());
                }
                if (capitalGood.getDescription() != null) {
                    existingCapitalGood.setDescription(capitalGood.getDescription());
                }
                if (capitalGood.getSupplier() != null) {
                    existingCapitalGood.setSupplier(capitalGood.getSupplier());
                }
                if (capitalGood.getPurchaseDate() != null) {
                    existingCapitalGood.setPurchaseDate(capitalGood.getPurchaseDate());
                }
                if (capitalGood.getPurchasePrice() != null) {
                    existingCapitalGood.setPurchasePrice(capitalGood.getPurchasePrice());
                }
                if (capitalGood.getUsefulLife() != null) {
                    existingCapitalGood.setUsefulLife(capitalGood.getUsefulLife());
                }
                if (capitalGood.getIntensityUnits() != null) {
                    existingCapitalGood.setIntensityUnits(capitalGood.getIntensityUnits());
                }
                if (capitalGood.getScalingFactor() != null) {
                    existingCapitalGood.setScalingFactor(capitalGood.getScalingFactor());
                }

                return existingCapitalGood;
            })
            .map(capitalGoodRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, capitalGood.getId().toString())
        );
    }

    /**
     * {@code GET  /capital-goods} : get all the capitalGoods.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of capitalGoods in body.
     */
    @GetMapping("")
    public List<CapitalGood> getAllCapitalGoods() {
        log.debug("REST request to get all CapitalGoods");
        return capitalGoodRepository.findAll();
    }

    /**
     * {@code GET  /capital-goods/:id} : get the "id" capitalGood.
     *
     * @param id the id of the capitalGood to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the capitalGood, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CapitalGood> getCapitalGood(@PathVariable("id") Long id) {
        log.debug("REST request to get CapitalGood : {}", id);
        Optional<CapitalGood> capitalGood = capitalGoodRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(capitalGood);
    }

    /**
     * {@code DELETE  /capital-goods/:id} : delete the "id" capitalGood.
     *
     * @param id the id of the capitalGood to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCapitalGood(@PathVariable("id") Long id) {
        log.debug("REST request to delete CapitalGood : {}", id);
        capitalGoodRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
