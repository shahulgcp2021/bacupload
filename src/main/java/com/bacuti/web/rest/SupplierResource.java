package com.bacuti.web.rest;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import com.bacuti.service.SupplierService;
import com.bacuti.service.dto.SupplierDTO;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bacuti.domain.Supplier}.
 *
 * @author Imran Nazir K
 * @since 2024-06-28
 */
@RestController
@RequestMapping("/api/${app.api.version}/suppliers")
@Transactional
public class SupplierResource {

    private static final String ENTITY_NAME = "uploadServiceSupplier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private SupplierService supplierService;

    /**
     * {@code POST  /suppliers} : Create a new supplier.
     *
     * @param supplier the supplier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplier, or with status {@code 400 (Bad Request)} if the supplier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SupplierDTO> createSupplier(@Valid @RequestBody SupplierDTO supplier) throws URISyntaxException {
        supplier = supplierService.save(supplier);
        return ResponseEntity.created(new URI("/api/suppliers/" + supplier.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, supplier.getId().toString()))
            .body(supplier);
    }

    /**
     * {@code PUT  /suppliers/:id} : Updates an existing supplier.
     *
     * @param id          the id of the supplier to save.
     * @param supplierDTO the supplier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplier,
     * or with status {@code 400 (Bad Request)} if the supplier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> updateSupplier(@PathVariable(value = "id") Long id,
                                                      @Valid @RequestBody SupplierDTO supplierDTO
    ) throws URISyntaxException {
        supplierDTO = supplierService.update(id, supplierDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                supplierDTO.getId().toString()))
            .body(supplierDTO);
    }

    /**
     * {@code GET  /suppliers} : get all the suppliers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of suppliers in body.
     */
    @GetMapping("/paged")
    public Page<SupplierDTO> getAllSuppliersPaged(@RequestParam(defaultValue = "0") int start,
                                                  @RequestParam(defaultValue = "10") int limit,
                                                  @RequestParam(required = false, defaultValue = "lastModifiedDate") String sortBy,
                                                  @RequestParam(required = false, defaultValue = "desc") String sortingOrder,
                                                  @RequestParam(defaultValue = "") String searchKey) {
        return supplierService.getAllSuppliersPaged(start, limit, sortBy, sortingOrder, searchKey);
    }

    /**
     * Gets all Suppliers as supplierDTOs.
     */
    @GetMapping("")
    public ResponseEntity<List<SupplierDTO>> getAllSuppliers() {
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(supplierService.findAll()));
    }

    /**
     * {@code DELETE  /suppliers/:id} : delete the "id" supplier.
     *
     * @param id the id of the supplier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable("id") Long id) {
        supplierService.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /all-supplier} : get all supplier name from supplier entity
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of items in body.
     */
    @GetMapping("/all-supplier")
    public ResponseEntity<List<String>> getAllSupplierNames() {
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(supplierService.fetchAllSupplierNames()));
    }
}
