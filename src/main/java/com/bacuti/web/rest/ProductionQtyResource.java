package com.bacuti.web.rest;

import com.bacuti.service.dto.ErrorDetailDTO;
import com.bacuti.service.ProductionQtyService;
import com.bacuti.service.dto.ProductionQtyDTO;
import com.bacuti.web.rest.errors.BadRequestAlertException;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import jakarta.validation.Valid;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link com.bacuti.domain.ProductionQty}.
 */
@RestController
@RequestMapping("/api/${app.api.version}/production-quantity")
@Transactional
public class ProductionQtyResource {

    private final Logger log = LoggerFactory.getLogger(ProductionQtyResource.class);

    private static final String ENTITY_NAME = "productionQuantity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductionQtyService productionQtyService;

    public ProductionQtyResource(ProductionQtyService productionQtyService) {
        this.productionQtyService = productionQtyService;
    }

    /**
     * {@code POST  /production-qties} : Create a new productionQty.
     *
     * @param productionQtyDTO the productionQty to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productionQty, or with status {@code 400 (Bad Request)} if the productionQty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductionQtyDTO> createProductionQty(@Valid @RequestBody ProductionQtyDTO productionQtyDTO) throws URISyntaxException {
        log.debug("REST request to save ProductionQty");
        if (productionQtyDTO.getId() != null) {
            throw new BadRequestAlertException("A new production quantity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductionQtyDTO result = productionQtyService.saveProductionQty(productionQtyDTO);
        return ResponseEntity.created(new URI("/api/production-qties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /production-qties/:id} : Updates an existing productionQty.
     *
     * @param id the id of the productionQty to save.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productionQty,
     * or with status {@code 400 (Bad Request)} if the productionQty is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productionQty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductionQtyDTO> updateProductionQty(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductionQtyDTO productionQtyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductionQty");
        if (productionQtyDTO.getId() == null || !Objects.equals(id, productionQtyDTO.getId())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idinvalid");
        }
        ProductionQtyDTO result = productionQtyService.saveProductionQty(productionQtyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /production-qties} : get all the productionQties.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productionQties in body.
     */
    @GetMapping("")
    public ResponseEntity<Page<ProductionQtyDTO>> getAllProductionQties(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size,
                                                                        @RequestParam(required = false, defaultValue = "lastModifiedDate") String sortBy,
                                                                        @RequestParam(required = false, defaultValue = "desc") String sortDirection) {
        log.debug("REST request to get ProductionQty : ");
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductionQtyDTO> productionQties = productionQtyService.getAllProductionQties(pageable);
        return ResponseEntity.ok(productionQties);
    }

    /**
     * {@code DELETE  /production-qties/:id} : delete the "id" productionQty.
     *
     * @param id the id of the productionQty to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProductionQty(@PathVariable("id") Long id) {
        log.debug("REST request to delete ProductionQty");
        productionQtyService.deleteProductionQtyById(id);
        HttpHeaders headers = HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString());
        return ResponseEntity.ok().body(true);
    }
}
