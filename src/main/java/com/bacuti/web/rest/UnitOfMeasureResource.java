package com.bacuti.web.rest;

import com.bacuti.service.UnitofMeasureService;
import com.bacuti.service.dto.UnitOfMeasureDTO;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bacuti.domain.UnitOfMeasure}.
 */
@RestController
@RequestMapping("/api/${app.api.version}/unit-of-measures")
@Transactional
public class UnitOfMeasureResource {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasureResource.class);

    private static final String ENTITY_NAME = "uploadServiceUnitOfMeasure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnitofMeasureService unitofMeasureService;

    public UnitOfMeasureResource(UnitofMeasureService unitofMeasureService) {
        this.unitofMeasureService = unitofMeasureService;
    }


    /**
     * {@code GET  /unit-of-measures} : get all the unitOfMeasures.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unitOfMeasures in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UnitOfMeasureDTO>> getAllUnitOfMeasures() {
        log.debug("REST request to get all UnitOfMeasures");
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(unitofMeasureService.findAll()));
    }

}
