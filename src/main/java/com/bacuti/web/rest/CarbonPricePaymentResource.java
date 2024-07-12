package com.bacuti.web.rest;

import com.bacuti.domain.CarbonPricePayment;
import com.bacuti.repository.CarbonPricePaymentRepository;
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
 * REST controller for managing {@link com.bacuti.domain.CarbonPricePayment}.
 */
@RestController
@RequestMapping("/api/carbon-price-payments")
@Transactional
public class CarbonPricePaymentResource {

    private final Logger log = LoggerFactory.getLogger(CarbonPricePaymentResource.class);

    private static final String ENTITY_NAME = "uploadServiceCarbonPricePayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarbonPricePaymentRepository carbonPricePaymentRepository;

    public CarbonPricePaymentResource(CarbonPricePaymentRepository carbonPricePaymentRepository) {
        this.carbonPricePaymentRepository = carbonPricePaymentRepository;
    }

    /**
     * {@code POST  /carbon-price-payments} : Create a new carbonPricePayment.
     *
     * @param carbonPricePayment the carbonPricePayment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carbonPricePayment, or with status {@code 400 (Bad Request)} if the carbonPricePayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CarbonPricePayment> createCarbonPricePayment(@RequestBody CarbonPricePayment carbonPricePayment)
        throws URISyntaxException {
        log.debug("REST request to save CarbonPricePayment : {}", carbonPricePayment);
        if (carbonPricePayment.getId() != null) {
            throw new BadRequestAlertException("A new carbonPricePayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        carbonPricePayment = carbonPricePaymentRepository.save(carbonPricePayment);
        return ResponseEntity.created(new URI("/api/carbon-price-payments/" + carbonPricePayment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, carbonPricePayment.getId().toString()))
            .body(carbonPricePayment);
    }

    /**
     * {@code PUT  /carbon-price-payments/:id} : Updates an existing carbonPricePayment.
     *
     * @param id the id of the carbonPricePayment to save.
     * @param carbonPricePayment the carbonPricePayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carbonPricePayment,
     * or with status {@code 400 (Bad Request)} if the carbonPricePayment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carbonPricePayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CarbonPricePayment> updateCarbonPricePayment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CarbonPricePayment carbonPricePayment
    ) throws URISyntaxException {
        log.debug("REST request to update CarbonPricePayment : {}, {}", id, carbonPricePayment);
        if (carbonPricePayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carbonPricePayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carbonPricePaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        carbonPricePayment = carbonPricePaymentRepository.save(carbonPricePayment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carbonPricePayment.getId().toString()))
            .body(carbonPricePayment);
    }

    /**
     * {@code PATCH  /carbon-price-payments/:id} : Partial updates given fields of an existing carbonPricePayment, field will ignore if it is null
     *
     * @param id the id of the carbonPricePayment to save.
     * @param carbonPricePayment the carbonPricePayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carbonPricePayment,
     * or with status {@code 400 (Bad Request)} if the carbonPricePayment is not valid,
     * or with status {@code 404 (Not Found)} if the carbonPricePayment is not found,
     * or with status {@code 500 (Internal Server Error)} if the carbonPricePayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CarbonPricePayment> partialUpdateCarbonPricePayment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CarbonPricePayment carbonPricePayment
    ) throws URISyntaxException {
        log.debug("REST request to partial update CarbonPricePayment partially : {}, {}", id, carbonPricePayment);
        if (carbonPricePayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carbonPricePayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carbonPricePaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CarbonPricePayment> result = carbonPricePaymentRepository
            .findById(carbonPricePayment.getId())
            .map(existingCarbonPricePayment -> {
                if (carbonPricePayment.getPaymentDate() != null) {
                    existingCarbonPricePayment.setPaymentDate(carbonPricePayment.getPaymentDate());
                }
                if (carbonPricePayment.getAmount() != null) {
                    existingCarbonPricePayment.setAmount(carbonPricePayment.getAmount());
                }
                if (carbonPricePayment.getCurrency() != null) {
                    existingCarbonPricePayment.setCurrency(carbonPricePayment.getCurrency());
                }
                if (carbonPricePayment.getEurFxRate() != null) {
                    existingCarbonPricePayment.setEurFxRate(carbonPricePayment.getEurFxRate());
                }
                if (carbonPricePayment.getAmountInEur() != null) {
                    existingCarbonPricePayment.setAmountInEur(carbonPricePayment.getAmountInEur());
                }
                if (carbonPricePayment.getEmissionFromDate() != null) {
                    existingCarbonPricePayment.setEmissionFromDate(carbonPricePayment.getEmissionFromDate());
                }
                if (carbonPricePayment.getEmissionToDate() != null) {
                    existingCarbonPricePayment.setEmissionToDate(carbonPricePayment.getEmissionToDate());
                }
                if (carbonPricePayment.getFormOfCarbonPrice() != null) {
                    existingCarbonPricePayment.setFormOfCarbonPrice(carbonPricePayment.getFormOfCarbonPrice());
                }
                if (carbonPricePayment.getPercentEmissionByPrice() != null) {
                    existingCarbonPricePayment.setPercentEmissionByPrice(carbonPricePayment.getPercentEmissionByPrice());
                }
                if (carbonPricePayment.getFormOfRebate() != null) {
                    existingCarbonPricePayment.setFormOfRebate(carbonPricePayment.getFormOfRebate());
                }
                if (carbonPricePayment.getPercentEmissionByRebate() != null) {
                    existingCarbonPricePayment.setPercentEmissionByRebate(carbonPricePayment.getPercentEmissionByRebate());
                }

                return existingCarbonPricePayment;
            })
            .map(carbonPricePaymentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carbonPricePayment.getId().toString())
        );
    }

    /**
     * {@code GET  /carbon-price-payments} : get all the carbonPricePayments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carbonPricePayments in body.
     */
    @GetMapping("")
    public List<CarbonPricePayment> getAllCarbonPricePayments() {
        log.debug("REST request to get all CarbonPricePayments");
        return carbonPricePaymentRepository.findAll();
    }

    /**
     * {@code GET  /carbon-price-payments/:id} : get the "id" carbonPricePayment.
     *
     * @param id the id of the carbonPricePayment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carbonPricePayment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CarbonPricePayment> getCarbonPricePayment(@PathVariable("id") Long id) {
        log.debug("REST request to get CarbonPricePayment : {}", id);
        Optional<CarbonPricePayment> carbonPricePayment = carbonPricePaymentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(carbonPricePayment);
    }

    /**
     * {@code DELETE  /carbon-price-payments/:id} : delete the "id" carbonPricePayment.
     *
     * @param id the id of the carbonPricePayment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarbonPricePayment(@PathVariable("id") Long id) {
        log.debug("REST request to delete CarbonPricePayment : {}", id);
        carbonPricePaymentRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
