package com.bacuti.web.rest;

import com.bacuti.domain.enumeration.EfUnits;
import com.bacuti.domain.enumeration.EnergyType;
import com.bacuti.service.EnergySourceService;
import com.bacuti.service.dto.EnergySourceDTO;
import com.bacuti.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bacuti.domain.EnergySource}.
 */
@RestController
@RequestMapping("/api/${app.api.version}/energy-sources")
@Transactional
public class EnergySourceResource {

    private final Logger log = LoggerFactory.getLogger(EnergySourceResource.class);

    private static final String ENTITY_NAME = "uploadServiceEnergySource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private EnergySourceService energySourceService;

    /**
     * {@code POST  /energy-sources} : Create a new energySource.
     *
     * @param energySource the energySource to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new energySource, or with status {@code 400 (Bad Request)} if the energySource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EnergySourceDTO> createEnergySource(@RequestBody EnergySourceDTO energySource) throws URISyntaxException {
        log.debug("REST request to save EnergySource");
        if (Objects.nonNull(energySource.getId())) {
            throw new BadRequestAlertException("A new energySource cannot already have an ID", ENTITY_NAME, "Id Exist");
        }
        energySource = energySourceService.createEnergySource(energySource);
        return ResponseEntity.created(new URI("/api/energy-sources/" + energySource.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, energySource.getId().toString()))
            .body(energySource);
    }

    /**
     * {@code PUT  /energy-sources/:id} : Updates an existing energySource.
     *
     * @param id the id of the energySource to save.
     * @param energySourceDTO the energySource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated energySource,
     * or with status {@code 400 (Bad Request)} if the energySource is not valid,
     * or with status {@code 500 (Internal Server Error)} if the energySource couldn't be updated.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EnergySourceDTO> updateEnergySource(
        @PathVariable(value = "id", required = true) final Long id, @RequestBody EnergySourceDTO energySourceDTO) {
        log.debug("REST request to update EnergySourceDTO");

        if (Objects.isNull(energySourceDTO.getId()) || !Objects.equals(id, energySourceDTO.getId()))
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "Id Null or Wrong");

        energySourceDTO = energySourceService.createEnergySource(energySourceDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName,
            true, ENTITY_NAME, energySourceDTO.getId().toString())).body(energySourceDTO);
    }

    /**
     * {@code GET  /energy-sources/:id} : get the "id" energySource.
     *
     * @param page - Page number to fetch data.
     * @param size - Page size of screen
     * @param sortBy - Data sorting column
     * @param sortDirection - Data sort direction
     * @param search - item column.
     * @return Returns Item supplier dto
     */
    @GetMapping("")
    public ResponseEntity<Page<EnergySourceDTO>> getItemSupplier(
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "lastModifiedDate") String sortBy, @RequestParam(defaultValue = "desc") String sortDirection,
        @RequestParam(defaultValue = "") String search) {
        return ResponseEntity.ok(energySourceService.getEnergySource(page, size, sortBy, sortDirection, search));
    }

    /**
     * {@code DELETE  /energy-sources/:id} : delete the "id" energySource.
     *
     * @param id the id of the energySource to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteEnergySource(@PathVariable("id") Long id) {
        log.debug("REST request to delete EnergySource");
        energySourceService.deleteById(id);
        return ResponseEntity.ok().body(true);
    }

    /**
     * {@code GET  /energy-sources/:id} : get the "id" energySource.
     *
     * @param page - Page number to fetch data.
     * @param size - Page size of screen
     * @return Returns Item supplier dto
     */
    @GetMapping("/type")
    public ResponseEntity<Page<EnergySourceDTO>> getEnergySourceTypes() {
        log.debug("REST request to get all EnergySources");

        return ResponseEntity.ok(energySourceService.getEnergySourceTypes());
    }

    /**
     * {@code GET  /items/energyType} : get the energy type .
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the energyType,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/energyType")
    public ResponseEntity<List<EnergyType>> getEnergySourceByEnergyType() {
        return ResponseUtil.wrapOrNotFound(Optional.of(Arrays.stream(EnergyType.values()).toList()));
    }

    /**
     * {@code GET  /items/efUnits} : get the ef units type .
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the energyType,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/efUnits")
    public ResponseEntity<List<EfUnits>> getEFUnits() {
        return ResponseUtil.wrapOrNotFound(Optional.of(Arrays.stream(EfUnits.values()).toList()));
    }
}
