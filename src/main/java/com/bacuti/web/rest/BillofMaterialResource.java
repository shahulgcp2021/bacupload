package com.bacuti.web.rest;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.service.BillofMaterialService;
import com.bacuti.service.dto.BillofMaterialDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * REST controller for managing {@link com.bacuti.domain.BillofMaterial}.
 */
@RestController
@RequestMapping("/api/${app.api.version}/billof-materials")
@Transactional
public class BillofMaterialResource {

    private final Logger log = LoggerFactory.getLogger(BillofMaterialResource.class);

    private static final String ENTITY_NAME = "uploadServiceBillofMaterial";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillofMaterialService billofMaterialService;


    public BillofMaterialResource( BillofMaterialService billofMaterialService) {
           this.billofMaterialService = billofMaterialService;
    }

    /**
     * {@code POST  /billof-materials} : Create a new billofMaterial.
     *
     * @param billofMaterial the billofMaterial to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billofMaterial, or with status {@code 400 (Bad Request)} if the billofMaterial has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BillofMaterialDTO> createBillofMaterial(@RequestBody BillofMaterialDTO billofMaterial) throws URISyntaxException {
        log.debug("REST request to save BillofMaterial : {}", billofMaterial);
        if (billofMaterial.getId() != null) {
            throw new BusinessException("A new billofMaterial cannot already have an ID", HttpStatus.BAD_REQUEST.value());
        }
        billofMaterial = billofMaterialService.saveBillofMaterial(billofMaterial);
        return ResponseEntity.created(new URI("/api/billof-materials/" + billofMaterial.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, billofMaterial.getId().toString()))
            .body(billofMaterial);
    }

    /**
     * {@code PUT  /billof-materials/:id} : Updates an existing billofMaterial.
     *
     * @param id the id of the billofMaterial to save.
     * @param billofMaterial the billofMaterial to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billofMaterial,
     * or with status {@code 400 (Bad Request)} if the billofMaterial is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billofMaterial couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @PutMapping("/{id}")
    public ResponseEntity<BillofMaterialDTO> updateBillofMaterial(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BillofMaterialDTO billofMaterial
    ) throws URISyntaxException {
        log.debug("REST request to update BillofMaterial : {}, {}", id, billofMaterial);
        if (billofMaterial.getId() == null || !Objects.equals(id, billofMaterial.getId())) {
            throw new BusinessException("Invalid id", HttpStatus.NOT_FOUND.value());
        }

        billofMaterial = billofMaterialService.saveBillofMaterial(billofMaterial);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billofMaterial.getId().toString()))
            .body(billofMaterial);
    }

    /**
      * @param search
      * @param page
      * @param size
      * @param sortBy
      * @param sortDirection
      * @return he {@link ResponseEntity} with status {@code 200 (OK)} and with body the billofMaterial, or with status {@code 404 (Not Found)}.
     */

    @GetMapping("")
    public ResponseEntity<Page<BillofMaterialDTO>> getBillofMaterials(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "lastModifiedDate") String sortBy,
        @RequestParam(defaultValue = "desc") String sortDirection,
        @RequestParam(defaultValue = "") String search) {
        Page<BillofMaterialDTO> sites = billofMaterialService.getBillofMaterials(page, size, sortBy, sortDirection, search);
        return ResponseEntity.ok(sites);
    }

    /**
     * {@code DELETE  /billof-materials/:id} : delete the "id" billofMaterial.
     *
     * @param id the id of the billofMaterial to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteBillofMaterial(@PathVariable("id") Long id) {
        log.debug("REST request to delete BillofMaterial : {}", id);
        billofMaterialService.deleteById(id);
        HttpHeaders headers = HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString());
        return ResponseEntity.ok().body(true);
    }
}
