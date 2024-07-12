package com.bacuti.web.rest;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.service.AggregateEnergyUsageService;
import com.bacuti.service.dto.AggregateEnergyUsageDTO;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import com.bacuti.service.dto.ErrorDetailDTO;
import jakarta.validation.Valid;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link com.bacuti.domain.AggregateEnergyUsage}.
 */
@RestController
@RequestMapping("/api/${app.api.version}/aggregate-energy-usages")
//@Transactional
public class AggregateEnergyUsageResource {

    private final Logger log = LoggerFactory.getLogger(AggregateEnergyUsageResource.class);

    private static final String ENTITY_NAME = "uploadServiceAggregateEnergyUsage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AggregateEnergyUsageService aggregateEnergyUsageService;

    public AggregateEnergyUsageResource(AggregateEnergyUsageService aggregateEnergyUsageService) {
        this.aggregateEnergyUsageService = aggregateEnergyUsageService;
    }

    /**
     * {@code POST  /aggregate-energy-usages} : Create a new aggregateEnergyUsage.
     *
     * @param aggregateEnergyUsageDTO the aggregateEnergyUsage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aggregateEnergyUsage, or with status {@code 400 (Bad Request)} if the aggregateEnergyUsage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AggregateEnergyUsageDTO> createAggregateEnergyUsage(@RequestBody AggregateEnergyUsageDTO aggregateEnergyUsageDTO)
        throws URISyntaxException {
        log.debug("REST request to save aggregate energy usage");
        if (aggregateEnergyUsageDTO.getId() != null) {
            throw new BusinessException("A new aggregate energy usage cannot already have an ID", HttpStatus.BAD_REQUEST.value());
        }
        aggregateEnergyUsageDTO = aggregateEnergyUsageService.saveAggregateEnergyUsage(aggregateEnergyUsageDTO);
        return ResponseEntity.created(new URI("/api/aggregate-energy-usages" + aggregateEnergyUsageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, aggregateEnergyUsageDTO.getId().toString()))
            .body(aggregateEnergyUsageDTO);
    }

    /**
     * {@code PUT  /aggregate-energy-usages/:id} : Updates an existing aggregateEnergyUsage.
     *
     * @param id the id of the aggregateEnergyUsage to save.
     * @param aggregateEnergyUsageDTO the aggregateEnergyUsage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aggregateEnergyUsage,
     * or with status {@code 400 (Bad Request)} if the aggregateEnergyUsage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aggregateEnergyUsage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AggregateEnergyUsageDTO> updateAggregateEnergyUsage(@PathVariable(value = "id") final Long id,
                                                                           @Valid @RequestBody AggregateEnergyUsageDTO aggregateEnergyUsageDTO) {
        log.debug("REST request to update aggregate energy resource");
        if (aggregateEnergyUsageDTO.getId() == null || !Objects.equals(id, aggregateEnergyUsageDTO.getId())) {
            throw new BusinessException("Invalid id", HttpStatus.PRECONDITION_FAILED.value());
        }

        aggregateEnergyUsageDTO = aggregateEnergyUsageService.saveAggregateEnergyUsage(aggregateEnergyUsageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, aggregateEnergyUsageDTO.getId().toString()))
            .body(aggregateEnergyUsageDTO);
    }

    /**
     * {@code GET  /aggregate-energy-usages} : get all the aggregateEnergyUsages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aggregateEnergyUsages in body.
     */
    @GetMapping("")
    public ResponseEntity<Page<AggregateEnergyUsageDTO>> getAllAggregateEnergyUsages(@RequestParam(defaultValue = "0") int pageNo,
                                                                     @RequestParam(defaultValue = "10") int pageSize,
                                                                     @RequestParam(required = false, defaultValue = "lastModifiedDate") String sortBy,
                                                                     @RequestParam(required = false, defaultValue = "asc") String sortDirection,
                                                                     @RequestParam(required = false, defaultValue = "") String search) {
        log.debug("REST request to get all AggregateEnergyUsages");
        Page<AggregateEnergyUsageDTO> aggregateEnergyUsageDTO = aggregateEnergyUsageService.getAllEnergyUsage(pageNo, pageSize, sortBy, sortDirection, search);
        return ResponseEntity.ok(aggregateEnergyUsageDTO);
    }

    /**
     * {@code DELETE  /aggregate-energy-usages/:id} : delete the "id" aggregateEnergyUsage.
     *
     * @param id the id of the aggregateEnergyUsage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteAggregateEnergyUsage(@PathVariable("id") Long id) {
        log.debug("REST request to delete AggregateEnergyUsage : {}", id);
        aggregateEnergyUsageService.deleteEnergyUsageById(id);
        return ResponseEntity.ok().body(true);
    }

    /**
     * Upload aggregate energy usage data from excel file
     * @param file excel file
     * @return response entity
     */
    @PostMapping("/upload-aggregate-energy-usage")
    public ResponseEntity<?> uploadAggregateEnergyUsage(@RequestParam("file") MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            List<ErrorDetailDTO> errorFile = aggregateEnergyUsageService.validateAndSave(workbook.getSheetAt(0));
            if (Objects.isNull(errorFile) || errorFile.isEmpty()) {
                return new ResponseEntity<>("File processed successfully", HttpStatus.OK);
            }
            return new ResponseEntity<>(errorFile, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
