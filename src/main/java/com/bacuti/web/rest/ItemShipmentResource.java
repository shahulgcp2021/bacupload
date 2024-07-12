package com.bacuti.web.rest;

import com.bacuti.domain.ItemShipment;
import com.bacuti.repository.ItemShipmentRepository;
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
 * REST controller for managing {@link com.bacuti.domain.ItemShipment}.
 */
@RestController
@RequestMapping("/api/item-shipments")
@Transactional
public class ItemShipmentResource {

    private final Logger log = LoggerFactory.getLogger(ItemShipmentResource.class);

    private static final String ENTITY_NAME = "uploadServiceItemShipment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemShipmentRepository itemShipmentRepository;

    public ItemShipmentResource(ItemShipmentRepository itemShipmentRepository) {
        this.itemShipmentRepository = itemShipmentRepository;
    }

    /**
     * {@code POST  /item-shipments} : Create a new itemShipment.
     *
     * @param itemShipment the itemShipment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemShipment, or with status {@code 400 (Bad Request)} if the itemShipment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ItemShipment> createItemShipment(@RequestBody ItemShipment itemShipment) throws URISyntaxException {
        log.debug("REST request to save ItemShipment : {}", itemShipment);
        if (itemShipment.getId() != null) {
            throw new BadRequestAlertException("A new itemShipment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        itemShipment = itemShipmentRepository.save(itemShipment);
        return ResponseEntity.created(new URI("/api/item-shipments/" + itemShipment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, itemShipment.getId().toString()))
            .body(itemShipment);
    }

    /**
     * {@code PUT  /item-shipments/:id} : Updates an existing itemShipment.
     *
     * @param id the id of the itemShipment to save.
     * @param itemShipment the itemShipment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemShipment,
     * or with status {@code 400 (Bad Request)} if the itemShipment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemShipment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ItemShipment> updateItemShipment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ItemShipment itemShipment
    ) throws URISyntaxException {
        log.debug("REST request to update ItemShipment : {}, {}", id, itemShipment);
        if (itemShipment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemShipment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemShipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        itemShipment = itemShipmentRepository.save(itemShipment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemShipment.getId().toString()))
            .body(itemShipment);
    }

    /**
     * {@code PATCH  /item-shipments/:id} : Partial updates given fields of an existing itemShipment, field will ignore if it is null
     *
     * @param id the id of the itemShipment to save.
     * @param itemShipment the itemShipment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemShipment,
     * or with status {@code 400 (Bad Request)} if the itemShipment is not valid,
     * or with status {@code 404 (Not Found)} if the itemShipment is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemShipment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ItemShipment> partialUpdateItemShipment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ItemShipment itemShipment
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemShipment partially : {}, {}", id, itemShipment);
        if (itemShipment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemShipment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemShipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemShipment> result = itemShipmentRepository
            .findById(itemShipment.getId())
            .map(existingItemShipment -> {
                if (itemShipment.getShipmentdate() != null) {
                    existingItemShipment.setShipmentdate(itemShipment.getShipmentdate());
                }
                if (itemShipment.getShipper() != null) {
                    existingItemShipment.setShipper(itemShipment.getShipper());
                }
                if (itemShipment.getUpstream() != null) {
                    existingItemShipment.setUpstream(itemShipment.getUpstream());
                }
                if (itemShipment.getQuantityShipped() != null) {
                    existingItemShipment.setQuantityShipped(itemShipment.getQuantityShipped());
                }
                if (itemShipment.getWeightInKg() != null) {
                    existingItemShipment.setWeightInKg(itemShipment.getWeightInKg());
                }

                return existingItemShipment;
            })
            .map(itemShipmentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemShipment.getId().toString())
        );
    }

    /**
     * {@code GET  /item-shipments} : get all the itemShipments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemShipments in body.
     */
    @GetMapping("")
    public List<ItemShipment> getAllItemShipments() {
        log.debug("REST request to get all ItemShipments");
        return itemShipmentRepository.findAll();
    }

    /**
     * {@code GET  /item-shipments/:id} : get the "id" itemShipment.
     *
     * @param id the id of the itemShipment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemShipment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ItemShipment> getItemShipment(@PathVariable("id") Long id) {
        log.debug("REST request to get ItemShipment : {}", id);
        Optional<ItemShipment> itemShipment = itemShipmentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(itemShipment);
    }

    /**
     * {@code DELETE  /item-shipments/:id} : delete the "id" itemShipment.
     *
     * @param id the id of the itemShipment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemShipment(@PathVariable("id") Long id) {
        log.debug("REST request to delete ItemShipment : {}", id);
        itemShipmentRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
