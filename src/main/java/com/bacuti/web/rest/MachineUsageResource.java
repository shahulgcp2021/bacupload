package com.bacuti.web.rest;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.service.MachineUsageService;
import com.bacuti.service.dto.MachineUsageDTO;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link com.bacuti.domain.MachineUsage}.
 */
@RestController
@RequestMapping("/api/machine-usages")
@Transactional
public class MachineUsageResource {

    private final Logger log = LoggerFactory.getLogger(MachineUsageResource.class);

    private static final String ENTITY_NAME = "uploadServiceMachineUsage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    private final MachineUsageService machineUsageService;

    public MachineUsageResource(MachineUsageService machineUsageService) {
        this.machineUsageService = machineUsageService;
    }

    /**
     * {@code POST  /machine-usages} : Create a new machineUsage.
     *
     * @param machineUsageDTO the machineUsage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new machineUsage, or with status {@code 400 (Bad Request)} if the machineUsage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MachineUsageDTO> createMachineUsage(@RequestBody MachineUsageDTO machineUsageDTO) throws URISyntaxException {
        log.debug("REST request to save Machine usage");
        if (machineUsageDTO.getId() != null) {
            throw new BusinessException("A new energy usage cannot already have an ID", HttpStatus.BAD_REQUEST.value());
        }
        machineUsageDTO = machineUsageService.saveMachineUsage(machineUsageDTO);
        return ResponseEntity.created(new URI("/api/aggregate-energy-usages" + machineUsageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, machineUsageDTO.getId().toString()))
            .body(machineUsageDTO);
    }

    /**
     * {@code PUT  /machine-usages/:id} : Updates an existing machineUsage.
     *
     * @param id the id of the machineUsage to save.
     * @param machineUsageDTO the machineUsage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated machineUsage,
     * or with status {@code 400 (Bad Request)} if the machineUsage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the machineUsage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MachineUsageDTO> updateMachineUsage(@PathVariable(value = "id") final Long id,
                                                           @Valid @RequestBody MachineUsageDTO machineUsageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update machine usage resource");
        if (machineUsageDTO.getId() == null || !Objects.equals(id, machineUsageDTO.getId())) {
            throw new BusinessException("Invalid id", HttpStatus.NOT_FOUND.value());
        }

        machineUsageDTO = machineUsageService.saveMachineUsage(machineUsageDTO);
        return ResponseEntity.created(new URI("/api/aggregate-energy-usages" + machineUsageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, machineUsageDTO.getId().toString()))
            .body(machineUsageDTO);
    }

    /**
     * {@code GET  /machine-usages} : get all the machineUsages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of machineUsages in body.
     */
    @GetMapping("")
    public ResponseEntity<Page<MachineUsageDTO>> getAllMachineUsages(@RequestParam(defaultValue = "0") int pageNo,
                                                                             @RequestParam(defaultValue = "10") int pageSize,
                                                                             @RequestParam(required = false, defaultValue = "lastModifiedDate")String sortBy,
                                                                             @RequestParam(required = false, defaultValue = "asc")String sortDirection) {
        log.debug("REST request to get all Machine usages");
        Page<MachineUsageDTO> machineUsageDTOS = machineUsageService.getAllMachineUsage(pageNo, pageSize, sortBy, sortDirection);
        return ResponseEntity.ok(machineUsageDTOS);
    }

    /**
     * {@code DELETE  /machine-usages/:id} : delete the "id" machineUsage.
     *
     * @param id the id of the machineUsage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteMachineUsage(@PathVariable("id") Long id) {
        log.debug("REST request to delete Machine usage : {}", id);
        machineUsageService.deleteMachineUsageById(id);
        return ResponseEntity.ok().body(true);
    }
}
