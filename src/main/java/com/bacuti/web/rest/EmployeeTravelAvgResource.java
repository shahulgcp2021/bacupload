package com.bacuti.web.rest;

import com.bacuti.domain.EmployeeTravelAvg;
import com.bacuti.repository.EmployeeTravelAvgRepository;
import com.bacuti.service.EmployeeTravelAvgService;
import com.bacuti.service.dto.EmployeeTravelAvgDTO;
import com.bacuti.service.dto.ErrorDetailDTO;
import com.bacuti.web.rest.errors.BadRequestAlertException;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bacuti.domain.EmployeeTravelAvg}.
 */
@RestController
@RequestMapping("/api/${app.api.version}/employee-travel-averages")
@Transactional
public class EmployeeTravelAvgResource {
    private final Logger log = LoggerFactory.getLogger(EmployeeTravelAvgResource.class);

    private static final String ENTITY_NAME = "employeeTravelAvg";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeTravelAvgService employeeTravelAvgService;

    public EmployeeTravelAvgResource(EmployeeTravelAvgService employeeTravelAvgService) {
        this.employeeTravelAvgService = employeeTravelAvgService;
    }

    /**
     * {@code POST  /employee-travel-avgs} : Create a new employeeTravelAvg.
     *
     * @param employeeTravelAvgDTO the employeeTravelAvg to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeTravelAvg, or with status {@code 400 (Bad Request)} if the employeeTravelAvg has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmployeeTravelAvgDTO> createEmployeeTravelAvg(@Valid @RequestBody EmployeeTravelAvgDTO employeeTravelAvgDTO) throws URISyntaxException {
        log.debug("REST request to save EmployeeTravelAvg");
        if (employeeTravelAvgDTO.getId() != null) {
            throw new BadRequestAlertException("A new employee travel average cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmployeeTravelAvgDTO result = employeeTravelAvgService.saveEmployeeTravelAvg(employeeTravelAvgDTO);
        return ResponseEntity.created(new URI("/api/employee-travel-avgs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employee-travel-avgs/:id} : Updates an existing employeeTravelAvg.
     *
     * @param id the id of the employeeTravelAvg to save.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeTravelAvg,
     * or with status {@code 400 (Bad Request)} if the employeeTravelAvg is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeTravelAvg couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeTravelAvgDTO> updateEmployeeTravelAvg(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmployeeTravelAvgDTO employeeTravelAvgDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EmployeeTravelAvg");
        if (employeeTravelAvgDTO.getId() == null || !Objects.equals(id, employeeTravelAvgDTO.getId())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idinvalid");
        }
        EmployeeTravelAvgDTO result = employeeTravelAvgService.saveEmployeeTravelAvg(employeeTravelAvgDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /employee-travel-avgs} : get all the employeeTravelAvgs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeTravelAvgs in body.
     */
    @GetMapping("/{travelType}")
    public ResponseEntity<Page<EmployeeTravelAvgDTO>> getAllEmployeeTravelAvgsByTravelType(@RequestParam(defaultValue = "0") int page,
                                                                                           @RequestParam(defaultValue = "10") int size,
                                                                                           @RequestParam(required = false, defaultValue = "lastModifiedDate") String sortBy,
                                                                                           @RequestParam(required = false, defaultValue = "desc") String sortDirection,
                                                                                           @RequestParam(required = false, defaultValue = "", name = "search") String siteName,
                                                                                           @PathVariable(name = "travelType",required = true) String travelType) {
        log.debug("REST request to get EmployeeTravelAvg : ");
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<EmployeeTravelAvgDTO> employeeTravelAvgs = employeeTravelAvgService.getAllEmployeeTravelAvgs(pageable, travelType, siteName);
        return ResponseEntity.ok(employeeTravelAvgs);
    }

    /**
     * {@code DELETE  /employee-travel-avgs/:id} : delete the "id" employeeTravelAvg.
     *
     * @param id the id of the employeeTravelAvg to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteEmployeeTravelAvg(@PathVariable("id") Long id) {
        log.debug("REST request to delete EmployeeTravelAvg");
        employeeTravelAvgService.deleteEmployeeTravelAvgById(id);
        HttpHeaders headers = HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString());
        return ResponseEntity.ok().body(true);
    }


    @PostMapping("/upload-employee-travel-avg")
    public ResponseEntity<?> uploadEmployeeTravelAvg(@RequestParam("file") MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            List<ErrorDetailDTO> errorFile = employeeTravelAvgService.validateAndSave(workbook.getSheetAt(0));
            if (Objects.nonNull(errorFile) && !errorFile.isEmpty()) {
                return new ResponseEntity<>(errorFile, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("File processed successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
