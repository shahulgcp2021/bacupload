package com.bacuti.web.rest;

import com.bacuti.service.PurchasedQtyService;
import com.bacuti.service.dto.PurchasedQtyDTO;
import com.bacuti.web.rest.errors.BadRequestAlertException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link com.bacuti.domain.PurchasedQty}.
 */
@RestController
@RequestMapping("/api/${app.api.version}/purchased-quantity")
@Transactional
public class PurchasedQtyResource {

    private final Logger log = LoggerFactory.getLogger(PurchasedQtyResource.class);

    private static final String ENTITY_NAME = "purchasedQuantity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchasedQtyService purchasedQtyService;

    public PurchasedQtyResource(PurchasedQtyService purchasedQtyService) {
        this.purchasedQtyService = purchasedQtyService;
    }

    /**
     * {@code POST  /purchased-qties} : Create a new purchasedQty.
     *
     * @param purchasedQtyDTO the purchasedQty to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchasedQty, or with status {@code 400 (Bad Request)} if the purchasedQty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PurchasedQtyDTO> createPurchasedQty(@Valid @RequestBody PurchasedQtyDTO purchasedQtyDTO) throws URISyntaxException {
        log.debug("REST request to save PurchasedQty");
        if (purchasedQtyDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchased quantity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchasedQtyDTO result = purchasedQtyService.savePurchasedQty(purchasedQtyDTO);
        return ResponseEntity.created(new URI("/api/purchased-qtys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /purchased-qties/:id} : Updates an existing purchasedQty.
     *
     * @param id           the id of the purchasedQty to save.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchasedQty,
     * or with status {@code 400 (Bad Request)} if the purchasedQty is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchasedQty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PurchasedQtyDTO> updatePurchasedQty(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PurchasedQtyDTO purchasedQtyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PurchasedQty");
        if (purchasedQtyDTO.getId() == null || !Objects.equals(id, purchasedQtyDTO.getId())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idinvalid");
        }
        PurchasedQtyDTO result = purchasedQtyService.savePurchasedQty(purchasedQtyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
     * {@code GET  /purchased-qties} : get all the purchasedQties.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchasedQties in body.
     */
    @GetMapping("")
    public ResponseEntity<Page<PurchasedQtyDTO>> getAllPurchasedQty(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(required = false, defaultValue = "lastModifiedDate") String sortBy,
                                                                    @RequestParam(required = false, defaultValue = "desc") String sortDirection) {
        log.debug("REST request to get PurchasedQty : ");
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PurchasedQtyDTO> purchasedQtys = purchasedQtyService.getAllPurchasedQties(pageable);
        return ResponseEntity.ok(purchasedQtys);
    }


    /**
     * {@code DELETE  /purchased-qties/:id} : delete the "id" purchasedQty.
     *
     * @param id the id of the purchasedQty to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePurchasedQty(@PathVariable("id") Long id) {
        log.debug("REST request to delete PurchasedQty");
        purchasedQtyService.deletePurchasedQtyById(id);
        HttpHeaders headers = HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString());
        return ResponseEntity.ok().body(true);
    }
}
