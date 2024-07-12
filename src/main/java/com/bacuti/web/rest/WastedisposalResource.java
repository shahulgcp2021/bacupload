package com.bacuti.web.rest;

import com.bacuti.domain.Wastedisposal;
import com.bacuti.repository.WastedisposalRepository;
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
 * REST controller for managing {@link com.bacuti.domain.Wastedisposal}.
 */
@RestController
@RequestMapping("/api/wastedisposals")
@Transactional
public class WastedisposalResource {

    private final Logger log = LoggerFactory.getLogger(WastedisposalResource.class);

    private static final String ENTITY_NAME = "uploadServiceWastedisposal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WastedisposalRepository wastedisposalRepository;

    public WastedisposalResource(WastedisposalRepository wastedisposalRepository) {
        this.wastedisposalRepository = wastedisposalRepository;
    }

    /**
     * {@code POST  /wastedisposals} : Create a new wastedisposal.
     *
     * @param wastedisposal the wastedisposal to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wastedisposal, or with status {@code 400 (Bad Request)} if the wastedisposal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Wastedisposal> createWastedisposal(@RequestBody Wastedisposal wastedisposal) throws URISyntaxException {
        log.debug("REST request to save Wastedisposal : {}", wastedisposal);
        if (wastedisposal.getId() != null) {
            throw new BadRequestAlertException("A new wastedisposal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        wastedisposal = wastedisposalRepository.save(wastedisposal);
        return ResponseEntity.created(new URI("/api/wastedisposals/" + wastedisposal.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, wastedisposal.getId().toString()))
            .body(wastedisposal);
    }

    /**
     * {@code PUT  /wastedisposals/:id} : Updates an existing wastedisposal.
     *
     * @param id the id of the wastedisposal to save.
     * @param wastedisposal the wastedisposal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wastedisposal,
     * or with status {@code 400 (Bad Request)} if the wastedisposal is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wastedisposal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Wastedisposal> updateWastedisposal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Wastedisposal wastedisposal
    ) throws URISyntaxException {
        log.debug("REST request to update Wastedisposal : {}, {}", id, wastedisposal);
        if (wastedisposal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wastedisposal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wastedisposalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        wastedisposal = wastedisposalRepository.save(wastedisposal);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wastedisposal.getId().toString()))
            .body(wastedisposal);
    }

    /**
     * {@code PATCH  /wastedisposals/:id} : Partial updates given fields of an existing wastedisposal, field will ignore if it is null
     *
     * @param id the id of the wastedisposal to save.
     * @param wastedisposal the wastedisposal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wastedisposal,
     * or with status {@code 400 (Bad Request)} if the wastedisposal is not valid,
     * or with status {@code 404 (Not Found)} if the wastedisposal is not found,
     * or with status {@code 500 (Internal Server Error)} if the wastedisposal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Wastedisposal> partialUpdateWastedisposal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Wastedisposal wastedisposal
    ) throws URISyntaxException {
        log.debug("REST request to partial update Wastedisposal partially : {}, {}", id, wastedisposal);
        if (wastedisposal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wastedisposal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wastedisposalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Wastedisposal> result = wastedisposalRepository
            .findById(wastedisposal.getId())
            .map(existingWastedisposal -> {
                if (wastedisposal.getStage() != null) {
                    existingWastedisposal.setStage(wastedisposal.getStage());
                }
                if (wastedisposal.getWasteComponent() != null) {
                    existingWastedisposal.setWasteComponent(wastedisposal.getWasteComponent());
                }
                if (wastedisposal.getQuantity() != null) {
                    existingWastedisposal.setQuantity(wastedisposal.getQuantity());
                }
                if (wastedisposal.getDisposalMethod() != null) {
                    existingWastedisposal.setDisposalMethod(wastedisposal.getDisposalMethod());
                }

                return existingWastedisposal;
            })
            .map(wastedisposalRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wastedisposal.getId().toString())
        );
    }

    /**
     * {@code GET  /wastedisposals} : get all the wastedisposals.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wastedisposals in body.
     */
    @GetMapping("")
    public List<Wastedisposal> getAllWastedisposals() {
        log.debug("REST request to get all Wastedisposals");
        return wastedisposalRepository.findAll();
    }

    /**
     * {@code GET  /wastedisposals/:id} : get the "id" wastedisposal.
     *
     * @param id the id of the wastedisposal to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wastedisposal, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Wastedisposal> getWastedisposal(@PathVariable("id") Long id) {
        log.debug("REST request to get Wastedisposal : {}", id);
        Optional<Wastedisposal> wastedisposal = wastedisposalRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(wastedisposal);
    }

    /**
     * {@code DELETE  /wastedisposals/:id} : delete the "id" wastedisposal.
     *
     * @param id the id of the wastedisposal to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWastedisposal(@PathVariable("id") Long id) {
        log.debug("REST request to delete Wastedisposal : {}", id);
        wastedisposalRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
