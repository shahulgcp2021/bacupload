package com.bacuti.web.rest;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.service.ItemSupplierService;
import com.bacuti.service.dto.ItemSupplierDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link com.bacuti.domain.ItemSupplier}.
 */
@RestController
@RequestMapping("/api/${app.api.version}/item-suppliers")
@Transactional
public class ItemSupplierResource {

    private final Logger log = LoggerFactory.getLogger(ItemSupplierResource.class);

    private static final String ENTITY_NAME = "uploadServiceItemSupplier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private ItemSupplierService itemSupplierService;

    /**
     * {@code POST  /item-suppliers} : Create a new itemSupplier.
     *
     * @param itemSupplier the itemSupplier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemSupplier, or with status {@code 400 (Bad Request)} if the itemSupplier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ItemSupplierDTO> createItemSupplier(@RequestBody ItemSupplierDTO itemSupplier) throws URISyntaxException {
        log.debug("REST request to save ItemSupplier");
        if (itemSupplier.getId() != null) {
            throw new BusinessException("A new ItemSupplier cannot already have an ID", HttpStatus.BAD_REQUEST.value());
        }
        itemSupplier = itemSupplierService.createItemSupplier(itemSupplier);
        return ResponseEntity.created(new URI("/api/item-suppliers/" + itemSupplier.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, itemSupplier.getId().toString()))
            .body(itemSupplier);
    }

    /**
     * {@code PUT  /item-suppliers/:id} : Updates an existing itemSupplier.
     *
     * @param id the id of the itemSupplier to save.
     * @param itemSupplier the itemSupplier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemSupplier,
     * or with status {@code 400 (Bad Request)} if the itemSupplier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemSupplier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ItemSupplierDTO> updateItemSupplier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ItemSupplierDTO itemSupplier
    ) throws URISyntaxException {
        log.debug("REST request to update ItemSupplier");

        if (itemSupplier.getId() == null || !Objects.equals(id, itemSupplier.getId())) {
            throw new BusinessException("Invalid id", HttpStatus.NOT_FOUND.value());
        }
        itemSupplier = itemSupplierService.createItemSupplier(itemSupplier);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemSupplier.getId().toString()))
            .body(itemSupplier);
    }

    /**
     * {@code DELETE  /item-suppliers/:id} : delete the "id" itemSupplier.
     *
     * @param id the id of the itemSupplier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteItemSupplier(@PathVariable("id") Long id) {
        log.debug("REST request to delete ItemSupplier");
        itemSupplierService.deleteItemSupplierById(id);
        return ResponseEntity.ok().body(true);
    }

    @GetMapping("")
    public ResponseEntity<Page<ItemSupplierDTO>> getItemSupplier(
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "lastModifiedDate") String sortBy, @RequestParam(defaultValue = "desc") String sortDirection,
        @RequestParam(defaultValue = "") String search) {
        return ResponseEntity.ok(itemSupplierService.getItemSupplier(page, size, sortBy, sortDirection, search));
    }
}
