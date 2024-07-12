package com.bacuti.web.rest;

import com.bacuti.domain.ProductUsageDetail;
import com.bacuti.repository.ProductUsageDetailRepository;
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
 * REST controller for managing {@link com.bacuti.domain.ProductUsageDetail}.
 */
@RestController
@RequestMapping("/api/product-usage-details")
@Transactional
public class ProductUsageDetailResource {

    private final Logger log = LoggerFactory.getLogger(ProductUsageDetailResource.class);

    private static final String ENTITY_NAME = "uploadServiceProductUsageDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductUsageDetailRepository productUsageDetailRepository;

    public ProductUsageDetailResource(ProductUsageDetailRepository productUsageDetailRepository) {
        this.productUsageDetailRepository = productUsageDetailRepository;
    }

    /**
     * {@code POST  /product-usage-details} : Create a new productUsageDetail.
     *
     * @param productUsageDetail the productUsageDetail to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productUsageDetail, or with status {@code 400 (Bad Request)} if the productUsageDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductUsageDetail> createProductUsageDetail(@RequestBody ProductUsageDetail productUsageDetail)
        throws URISyntaxException {
        log.debug("REST request to save ProductUsageDetail : {}", productUsageDetail);
        if (productUsageDetail.getId() != null) {
            throw new BadRequestAlertException("A new productUsageDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productUsageDetail = productUsageDetailRepository.save(productUsageDetail);
        return ResponseEntity.created(new URI("/api/product-usage-details/" + productUsageDetail.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productUsageDetail.getId().toString()))
            .body(productUsageDetail);
    }

    /**
     * {@code PUT  /product-usage-details/:id} : Updates an existing productUsageDetail.
     *
     * @param id the id of the productUsageDetail to save.
     * @param productUsageDetail the productUsageDetail to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productUsageDetail,
     * or with status {@code 400 (Bad Request)} if the productUsageDetail is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productUsageDetail couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductUsageDetail> updateProductUsageDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductUsageDetail productUsageDetail
    ) throws URISyntaxException {
        log.debug("REST request to update ProductUsageDetail : {}, {}", id, productUsageDetail);
        if (productUsageDetail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productUsageDetail.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productUsageDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productUsageDetail = productUsageDetailRepository.save(productUsageDetail);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productUsageDetail.getId().toString()))
            .body(productUsageDetail);
    }

    /**
     * {@code PATCH  /product-usage-details/:id} : Partial updates given fields of an existing productUsageDetail, field will ignore if it is null
     *
     * @param id the id of the productUsageDetail to save.
     * @param productUsageDetail the productUsageDetail to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productUsageDetail,
     * or with status {@code 400 (Bad Request)} if the productUsageDetail is not valid,
     * or with status {@code 404 (Not Found)} if the productUsageDetail is not found,
     * or with status {@code 500 (Internal Server Error)} if the productUsageDetail couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductUsageDetail> partialUpdateProductUsageDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductUsageDetail productUsageDetail
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductUsageDetail partially : {}, {}", id, productUsageDetail);
        if (productUsageDetail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productUsageDetail.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productUsageDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductUsageDetail> result = productUsageDetailRepository
            .findById(productUsageDetail.getId())
            .map(existingProductUsageDetail -> {
                if (productUsageDetail.getUsefulLifeYrs() != null) {
                    existingProductUsageDetail.setUsefulLifeYrs(productUsageDetail.getUsefulLifeYrs());
                }
                if (productUsageDetail.getEmissionSource() != null) {
                    existingProductUsageDetail.setEmissionSource(productUsageDetail.getEmissionSource());
                }
                if (productUsageDetail.getDetail() != null) {
                    existingProductUsageDetail.setDetail(productUsageDetail.getDetail());
                }
                if (productUsageDetail.getAvgQuantityPerDay() != null) {
                    existingProductUsageDetail.setAvgQuantityPerDay(productUsageDetail.getAvgQuantityPerDay());
                }
                if (productUsageDetail.getProportion() != null) {
                    existingProductUsageDetail.setProportion(productUsageDetail.getProportion());
                }

                return existingProductUsageDetail;
            })
            .map(productUsageDetailRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productUsageDetail.getId().toString())
        );
    }

    /**
     * {@code GET  /product-usage-details} : get all the productUsageDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productUsageDetails in body.
     */
    @GetMapping("")
    public List<ProductUsageDetail> getAllProductUsageDetails() {
        log.debug("REST request to get all ProductUsageDetails");
        return productUsageDetailRepository.findAll();
    }

    /**
     * {@code GET  /product-usage-details/:id} : get the "id" productUsageDetail.
     *
     * @param id the id of the productUsageDetail to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productUsageDetail, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductUsageDetail> getProductUsageDetail(@PathVariable("id") Long id) {
        log.debug("REST request to get ProductUsageDetail : {}", id);
        Optional<ProductUsageDetail> productUsageDetail = productUsageDetailRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productUsageDetail);
    }

    /**
     * {@code DELETE  /product-usage-details/:id} : delete the "id" productUsageDetail.
     *
     * @param id the id of the productUsageDetail to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductUsageDetail(@PathVariable("id") Long id) {
        log.debug("REST request to delete ProductUsageDetail : {}", id);
        productUsageDetailRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
