package com.bacuti.web.rest;

import com.bacuti.domain.ShipmentLane;
import com.bacuti.repository.ShipmentLaneRepository;
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
 * REST controller for managing {@link com.bacuti.domain.ShipmentLane}.
 */
@RestController
@RequestMapping("/api/shipment-lanes")
@Transactional
public class ShipmentLaneResource {

    private final Logger log = LoggerFactory.getLogger(ShipmentLaneResource.class);

    private static final String ENTITY_NAME = "uploadServiceShipmentLane";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentLaneRepository shipmentLaneRepository;

    public ShipmentLaneResource(ShipmentLaneRepository shipmentLaneRepository) {
        this.shipmentLaneRepository = shipmentLaneRepository;
    }

    /**
     * {@code POST  /shipment-lanes} : Create a new shipmentLane.
     *
     * @param shipmentLane the shipmentLane to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentLane, or with status {@code 400 (Bad Request)} if the shipmentLane has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentLane> createShipmentLane(@Valid @RequestBody ShipmentLane shipmentLane) throws URISyntaxException {
        log.debug("REST request to save ShipmentLane : {}", shipmentLane);
        if (shipmentLane.getId() != null) {
            throw new BadRequestAlertException("A new shipmentLane cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentLane = shipmentLaneRepository.save(shipmentLane);
        return ResponseEntity.created(new URI("/api/shipment-lanes/" + shipmentLane.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentLane.getId().toString()))
            .body(shipmentLane);
    }

    /**
     * {@code PUT  /shipment-lanes/:id} : Updates an existing shipmentLane.
     *
     * @param id the id of the shipmentLane to save.
     * @param shipmentLane the shipmentLane to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentLane,
     * or with status {@code 400 (Bad Request)} if the shipmentLane is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentLane couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentLane> updateShipmentLane(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentLane shipmentLane
    ) throws URISyntaxException {
        log.debug("REST request to update ShipmentLane : {}, {}", id, shipmentLane);
        if (shipmentLane.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentLane.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentLaneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentLane = shipmentLaneRepository.save(shipmentLane);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentLane.getId().toString()))
            .body(shipmentLane);
    }

    /**
     * {@code PATCH  /shipment-lanes/:id} : Partial updates given fields of an existing shipmentLane, field will ignore if it is null
     *
     * @param id the id of the shipmentLane to save.
     * @param shipmentLane the shipmentLane to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentLane,
     * or with status {@code 400 (Bad Request)} if the shipmentLane is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentLane is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentLane couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentLane> partialUpdateShipmentLane(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentLane shipmentLane
    ) throws URISyntaxException {
        log.debug("REST request to partial update ShipmentLane partially : {}, {}", id, shipmentLane);
        if (shipmentLane.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentLane.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentLaneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentLane> result = shipmentLaneRepository
            .findById(shipmentLane.getId())
            .map(existingShipmentLane -> {
                if (shipmentLane.getLane() != null) {
                    existingShipmentLane.setLane(shipmentLane.getLane());
                }
                if (shipmentLane.getDescription() != null) {
                    existingShipmentLane.setDescription(shipmentLane.getDescription());
                }

                return existingShipmentLane;
            })
            .map(shipmentLaneRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentLane.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-lanes} : get all the shipmentLanes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentLanes in body.
     */
    @GetMapping("")
    public List<ShipmentLane> getAllShipmentLanes() {
        log.debug("REST request to get all ShipmentLanes");
        return shipmentLaneRepository.findAll();
    }

    /**
     * {@code GET  /shipment-lanes/:id} : get the "id" shipmentLane.
     *
     * @param id the id of the shipmentLane to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentLane, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentLane> getShipmentLane(@PathVariable("id") Long id) {
        log.debug("REST request to get ShipmentLane : {}", id);
        Optional<ShipmentLane> shipmentLane = shipmentLaneRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(shipmentLane);
    }

    /**
     * {@code DELETE  /shipment-lanes/:id} : delete the "id" shipmentLane.
     *
     * @param id the id of the shipmentLane to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentLane(@PathVariable("id") Long id) {
        log.debug("REST request to delete ShipmentLane : {}", id);
        shipmentLaneRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
