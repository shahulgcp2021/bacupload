package com.bacuti.web.rest;

import com.bacuti.domain.ShipmentLeg;
import com.bacuti.repository.ShipmentLegRepository;
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
 * REST controller for managing {@link com.bacuti.domain.ShipmentLeg}.
 */
@RestController
@RequestMapping("/api/shipment-legs")
@Transactional
public class ShipmentLegResource {

    private final Logger log = LoggerFactory.getLogger(ShipmentLegResource.class);

    private static final String ENTITY_NAME = "uploadServiceShipmentLeg";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentLegRepository shipmentLegRepository;

    public ShipmentLegResource(ShipmentLegRepository shipmentLegRepository) {
        this.shipmentLegRepository = shipmentLegRepository;
    }

    /**
     * {@code POST  /shipment-legs} : Create a new shipmentLeg.
     *
     * @param shipmentLeg the shipmentLeg to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentLeg, or with status {@code 400 (Bad Request)} if the shipmentLeg has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentLeg> createShipmentLeg(@RequestBody ShipmentLeg shipmentLeg) throws URISyntaxException {
        log.debug("REST request to save ShipmentLeg : {}", shipmentLeg);
        if (shipmentLeg.getId() != null) {
            throw new BadRequestAlertException("A new shipmentLeg cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentLeg = shipmentLegRepository.save(shipmentLeg);
        return ResponseEntity.created(new URI("/api/shipment-legs/" + shipmentLeg.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentLeg.getId().toString()))
            .body(shipmentLeg);
    }

    /**
     * {@code PUT  /shipment-legs/:id} : Updates an existing shipmentLeg.
     *
     * @param id the id of the shipmentLeg to save.
     * @param shipmentLeg the shipmentLeg to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentLeg,
     * or with status {@code 400 (Bad Request)} if the shipmentLeg is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentLeg couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentLeg> updateShipmentLeg(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ShipmentLeg shipmentLeg
    ) throws URISyntaxException {
        log.debug("REST request to update ShipmentLeg : {}, {}", id, shipmentLeg);
        if (shipmentLeg.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentLeg.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentLegRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentLeg = shipmentLegRepository.save(shipmentLeg);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentLeg.getId().toString()))
            .body(shipmentLeg);
    }

    /**
     * {@code PATCH  /shipment-legs/:id} : Partial updates given fields of an existing shipmentLeg, field will ignore if it is null
     *
     * @param id the id of the shipmentLeg to save.
     * @param shipmentLeg the shipmentLeg to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentLeg,
     * or with status {@code 400 (Bad Request)} if the shipmentLeg is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentLeg is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentLeg couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentLeg> partialUpdateShipmentLeg(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ShipmentLeg shipmentLeg
    ) throws URISyntaxException {
        log.debug("REST request to partial update ShipmentLeg partially : {}, {}", id, shipmentLeg);
        if (shipmentLeg.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentLeg.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentLegRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentLeg> result = shipmentLegRepository
            .findById(shipmentLeg.getId())
            .map(existingShipmentLeg -> {
                if (shipmentLeg.getSegment() != null) {
                    existingShipmentLeg.setSegment(shipmentLeg.getSegment());
                }
                if (shipmentLeg.getCarrier() != null) {
                    existingShipmentLeg.setCarrier(shipmentLeg.getCarrier());
                }
                if (shipmentLeg.getMode() != null) {
                    existingShipmentLeg.setMode(shipmentLeg.getMode());
                }
                if (shipmentLeg.getDistance() != null) {
                    existingShipmentLeg.setDistance(shipmentLeg.getDistance());
                }
                if (shipmentLeg.getFromCoordinateLat() != null) {
                    existingShipmentLeg.setFromCoordinateLat(shipmentLeg.getFromCoordinateLat());
                }
                if (shipmentLeg.getFromCoordinateLong() != null) {
                    existingShipmentLeg.setFromCoordinateLong(shipmentLeg.getFromCoordinateLong());
                }
                if (shipmentLeg.getToCoordinateLat() != null) {
                    existingShipmentLeg.setToCoordinateLat(shipmentLeg.getToCoordinateLat());
                }
                if (shipmentLeg.getToCoordinateLong() != null) {
                    existingShipmentLeg.setToCoordinateLong(shipmentLeg.getToCoordinateLong());
                }
                if (shipmentLeg.getFromIata() != null) {
                    existingShipmentLeg.setFromIata(shipmentLeg.getFromIata());
                }
                if (shipmentLeg.getToIata() != null) {
                    existingShipmentLeg.setToIata(shipmentLeg.getToIata());
                }
                if (shipmentLeg.getEmissionsFactor() != null) {
                    existingShipmentLeg.setEmissionsFactor(shipmentLeg.getEmissionsFactor());
                }
                if (shipmentLeg.getEfSource() != null) {
                    existingShipmentLeg.setEfSource(shipmentLeg.getEfSource());
                }

                return existingShipmentLeg;
            })
            .map(shipmentLegRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentLeg.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-legs} : get all the shipmentLegs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentLegs in body.
     */
    @GetMapping("")
    public List<ShipmentLeg> getAllShipmentLegs() {
        log.debug("REST request to get all ShipmentLegs");
        return shipmentLegRepository.findAll();
    }

    /**
     * {@code GET  /shipment-legs/:id} : get the "id" shipmentLeg.
     *
     * @param id the id of the shipmentLeg to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentLeg, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentLeg> getShipmentLeg(@PathVariable("id") Long id) {
        log.debug("REST request to get ShipmentLeg : {}", id);
        Optional<ShipmentLeg> shipmentLeg = shipmentLegRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(shipmentLeg);
    }

    /**
     * {@code DELETE  /shipment-legs/:id} : delete the "id" shipmentLeg.
     *
     * @param id the id of the shipmentLeg to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentLeg(@PathVariable("id") Long id) {
        log.debug("REST request to delete ShipmentLeg : {}", id);
        shipmentLegRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
