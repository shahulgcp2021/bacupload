package com.bacuti.web.rest;

import com.bacuti.service.MachineService;
import com.bacuti.service.dto.MachineDTO;
import com.bacuti.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link com.bacuti.domain.Machine}.
 */
@RestController
@RequestMapping("/api/${app.api.version}/machines")
@Transactional
public class MachineResource {

    private final Logger log = LoggerFactory.getLogger(MachineResource.class);

    private static final String ENTITY_NAME = "uploadServiceMachine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private MachineService machineService;

    /**
     * {@code POST  /machines} : Create a new machine.
     *
     * @param machine the machine to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new machine, or with status {@code 400 (Bad Request)} if the machine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MachineDTO> createMachine(@Valid @RequestBody MachineDTO machine) throws URISyntaxException {
        log.debug("REST request to save Machine");
        if (Objects.nonNull(machine.getId()))
            throw new BadRequestAlertException("A new machine cannot already have an ID", ENTITY_NAME, "idexists");

        machine = machineService.createMachine(machine);
        return ResponseEntity.created(new URI("/api/machines/" + machine.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, machine.getId().toString()))
            .body(machine);
    }

    /**
     * {@code PUT  /machines/:id} : Updates an existing machine.
     *
     * @param id the id of the machine to save.
     * @param machine the machine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated machine,
     * or with status {@code 400 (Bad Request)} if the machine is not valid,
     * or with status {@code 500 (Internal Server Error)} if the machine couldn't be updated.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MachineDTO> updateMachine(@PathVariable(value = "id", required = true) final Long id,
        @Valid @RequestBody MachineDTO machine) {
        log.debug("REST request to update Machine");

        if (Objects.isNull(machine.getId()) || !Objects.equals(id, machine.getId())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        machine = machineService.createMachine(machine);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
            ENTITY_NAME, machine.getId().toString())).body(machine);
    }

    /**
     * {@code DELETE  /machines/:id} : delete the "id" machine.
     *
     * @param id the id of the machine to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteMachine(@PathVariable("id") Long id) {
        log.debug("REST request to delete Machine");
        machineService.deleteMachineById(id);
        return ResponseEntity.ok().body(true);
    }

    /**
     * {@code GET  /machines} : Method used to get the Machine details
     *
     * @param page - Page number to fetch data.
     * @param size - Page size of screen
     * @param sortBy - Data sorting column
     * @param sortDirection - Data sort direction
     * @param search - Machine name column.
     * @return @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the machine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("")
    public ResponseEntity<Page<MachineDTO>> getMachineDetails(
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "lastModifiedDate") String sortBy, @RequestParam(defaultValue = "desc") String sortDirection,
        @RequestParam(defaultValue = "") String search) {
        return ResponseEntity.ok(machineService.getMachineDetails(page, size, sortBy, sortDirection, search));
    }
}
