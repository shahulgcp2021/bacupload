package com.bacuti.web.rest;

import com.bacuti.domain.CompanyPublicEmission;
import com.bacuti.repository.CompanyPublicEmissionRepository;
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
 * REST controller for managing {@link com.bacuti.domain.CompanyPublicEmission}.
 */
@RestController
@RequestMapping("/api/company-public-emissions")
@Transactional
public class CompanyPublicEmissionResource {

    private final Logger log = LoggerFactory.getLogger(CompanyPublicEmissionResource.class);

    private static final String ENTITY_NAME = "uploadServiceCompanyPublicEmission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompanyPublicEmissionRepository companyPublicEmissionRepository;

    public CompanyPublicEmissionResource(CompanyPublicEmissionRepository companyPublicEmissionRepository) {
        this.companyPublicEmissionRepository = companyPublicEmissionRepository;
    }

    /**
     * {@code POST  /company-public-emissions} : Create a new companyPublicEmission.
     *
     * @param companyPublicEmission the companyPublicEmission to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyPublicEmission, or with status {@code 400 (Bad Request)} if the companyPublicEmission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CompanyPublicEmission> createCompanyPublicEmission(@RequestBody CompanyPublicEmission companyPublicEmission)
        throws URISyntaxException {
        log.debug("REST request to save CompanyPublicEmission : {}", companyPublicEmission);
        if (companyPublicEmission.getId() != null) {
            throw new BadRequestAlertException("A new companyPublicEmission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        companyPublicEmission = companyPublicEmissionRepository.save(companyPublicEmission);
        return ResponseEntity.created(new URI("/api/company-public-emissions/" + companyPublicEmission.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, companyPublicEmission.getId().toString()))
            .body(companyPublicEmission);
    }

    /**
     * {@code PUT  /company-public-emissions/:id} : Updates an existing companyPublicEmission.
     *
     * @param id the id of the companyPublicEmission to save.
     * @param companyPublicEmission the companyPublicEmission to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyPublicEmission,
     * or with status {@code 400 (Bad Request)} if the companyPublicEmission is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyPublicEmission couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompanyPublicEmission> updateCompanyPublicEmission(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompanyPublicEmission companyPublicEmission
    ) throws URISyntaxException {
        log.debug("REST request to update CompanyPublicEmission : {}, {}", id, companyPublicEmission);
        if (companyPublicEmission.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyPublicEmission.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyPublicEmissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        companyPublicEmission = companyPublicEmissionRepository.save(companyPublicEmission);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyPublicEmission.getId().toString()))
            .body(companyPublicEmission);
    }

    /**
     * {@code PATCH  /company-public-emissions/:id} : Partial updates given fields of an existing companyPublicEmission, field will ignore if it is null
     *
     * @param id the id of the companyPublicEmission to save.
     * @param companyPublicEmission the companyPublicEmission to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyPublicEmission,
     * or with status {@code 400 (Bad Request)} if the companyPublicEmission is not valid,
     * or with status {@code 404 (Not Found)} if the companyPublicEmission is not found,
     * or with status {@code 500 (Internal Server Error)} if the companyPublicEmission couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompanyPublicEmission> partialUpdateCompanyPublicEmission(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompanyPublicEmission companyPublicEmission
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompanyPublicEmission partially : {}, {}", id, companyPublicEmission);
        if (companyPublicEmission.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyPublicEmission.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyPublicEmissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompanyPublicEmission> result = companyPublicEmissionRepository
            .findById(companyPublicEmission.getId())
            .map(existingCompanyPublicEmission -> {
                if (companyPublicEmission.getReportingCompany() != null) {
                    existingCompanyPublicEmission.setReportingCompany(companyPublicEmission.getReportingCompany());
                }
                if (companyPublicEmission.getReportingYear() != null) {
                    existingCompanyPublicEmission.setReportingYear(companyPublicEmission.getReportingYear());
                }
                if (companyPublicEmission.getRevenue() != null) {
                    existingCompanyPublicEmission.setRevenue(companyPublicEmission.getRevenue());
                }
                if (companyPublicEmission.getTotalEnergyInMwh() != null) {
                    existingCompanyPublicEmission.setTotalEnergyInMwh(companyPublicEmission.getTotalEnergyInMwh());
                }
                if (companyPublicEmission.getPermanentEmployees() != null) {
                    existingCompanyPublicEmission.setPermanentEmployees(companyPublicEmission.getPermanentEmployees());
                }
                if (companyPublicEmission.getScope1() != null) {
                    existingCompanyPublicEmission.setScope1(companyPublicEmission.getScope1());
                }
                if (companyPublicEmission.getScope2Location() != null) {
                    existingCompanyPublicEmission.setScope2Location(companyPublicEmission.getScope2Location());
                }
                if (companyPublicEmission.getScope2Market() != null) {
                    existingCompanyPublicEmission.setScope2Market(companyPublicEmission.getScope2Market());
                }
                if (companyPublicEmission.getScope3() != null) {
                    existingCompanyPublicEmission.setScope3(companyPublicEmission.getScope3());
                }
                if (companyPublicEmission.getCategory1() != null) {
                    existingCompanyPublicEmission.setCategory1(companyPublicEmission.getCategory1());
                }
                if (companyPublicEmission.getCategory2() != null) {
                    existingCompanyPublicEmission.setCategory2(companyPublicEmission.getCategory2());
                }
                if (companyPublicEmission.getCategory3() != null) {
                    existingCompanyPublicEmission.setCategory3(companyPublicEmission.getCategory3());
                }
                if (companyPublicEmission.getCategory4() != null) {
                    existingCompanyPublicEmission.setCategory4(companyPublicEmission.getCategory4());
                }
                if (companyPublicEmission.getCategory5() != null) {
                    existingCompanyPublicEmission.setCategory5(companyPublicEmission.getCategory5());
                }
                if (companyPublicEmission.getCategory6() != null) {
                    existingCompanyPublicEmission.setCategory6(companyPublicEmission.getCategory6());
                }
                if (companyPublicEmission.getCategory7() != null) {
                    existingCompanyPublicEmission.setCategory7(companyPublicEmission.getCategory7());
                }
                if (companyPublicEmission.getCategory8() != null) {
                    existingCompanyPublicEmission.setCategory8(companyPublicEmission.getCategory8());
                }
                if (companyPublicEmission.getCategory9() != null) {
                    existingCompanyPublicEmission.setCategory9(companyPublicEmission.getCategory9());
                }
                if (companyPublicEmission.getCategory10() != null) {
                    existingCompanyPublicEmission.setCategory10(companyPublicEmission.getCategory10());
                }
                if (companyPublicEmission.getCategory11() != null) {
                    existingCompanyPublicEmission.setCategory11(companyPublicEmission.getCategory11());
                }
                if (companyPublicEmission.getCategory12() != null) {
                    existingCompanyPublicEmission.setCategory12(companyPublicEmission.getCategory12());
                }
                if (companyPublicEmission.getCategory13() != null) {
                    existingCompanyPublicEmission.setCategory13(companyPublicEmission.getCategory13());
                }
                if (companyPublicEmission.getCategory14() != null) {
                    existingCompanyPublicEmission.setCategory14(companyPublicEmission.getCategory14());
                }
                if (companyPublicEmission.getCategory15() != null) {
                    existingCompanyPublicEmission.setCategory15(companyPublicEmission.getCategory15());
                }
                if (companyPublicEmission.getIntensityScope1() != null) {
                    existingCompanyPublicEmission.setIntensityScope1(companyPublicEmission.getIntensityScope1());
                }
                if (companyPublicEmission.getIntensityScope1Loction() != null) {
                    existingCompanyPublicEmission.setIntensityScope1Loction(companyPublicEmission.getIntensityScope1Loction());
                }
                if (companyPublicEmission.getIntensityscope2Market() != null) {
                    existingCompanyPublicEmission.setIntensityscope2Market(companyPublicEmission.getIntensityscope2Market());
                }
                if (companyPublicEmission.getIntensityScope3() != null) {
                    existingCompanyPublicEmission.setIntensityScope3(companyPublicEmission.getIntensityScope3());
                }
                if (companyPublicEmission.getIntensityScope12() != null) {
                    existingCompanyPublicEmission.setIntensityScope12(companyPublicEmission.getIntensityScope12());
                }
                if (companyPublicEmission.getIntensityScope123() != null) {
                    existingCompanyPublicEmission.setIntensityScope123(companyPublicEmission.getIntensityScope123());
                }
                if (companyPublicEmission.getActivityLevel() != null) {
                    existingCompanyPublicEmission.setActivityLevel(companyPublicEmission.getActivityLevel());
                }
                if (companyPublicEmission.getDataSourceType() != null) {
                    existingCompanyPublicEmission.setDataSourceType(companyPublicEmission.getDataSourceType());
                }
                if (companyPublicEmission.getDisclosureType() != null) {
                    existingCompanyPublicEmission.setDisclosureType(companyPublicEmission.getDisclosureType());
                }
                if (companyPublicEmission.getDataSource() != null) {
                    existingCompanyPublicEmission.setDataSource(companyPublicEmission.getDataSource());
                }
                if (companyPublicEmission.getIndustryCodes() != null) {
                    existingCompanyPublicEmission.setIndustryCodes(companyPublicEmission.getIndustryCodes());
                }
                if (companyPublicEmission.getCodeType() != null) {
                    existingCompanyPublicEmission.setCodeType(companyPublicEmission.getCodeType());
                }
                if (companyPublicEmission.getCompanyWebsite() != null) {
                    existingCompanyPublicEmission.setCompanyWebsite(companyPublicEmission.getCompanyWebsite());
                }
                if (companyPublicEmission.getCompanyActivities() != null) {
                    existingCompanyPublicEmission.setCompanyActivities(companyPublicEmission.getCompanyActivities());
                }

                return existingCompanyPublicEmission;
            })
            .map(companyPublicEmissionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyPublicEmission.getId().toString())
        );
    }

    /**
     * {@code GET  /company-public-emissions} : get all the companyPublicEmissions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companyPublicEmissions in body.
     */
    @GetMapping("")
    public List<CompanyPublicEmission> getAllCompanyPublicEmissions() {
        log.debug("REST request to get all CompanyPublicEmissions");
        return companyPublicEmissionRepository.findAll();
    }

    /**
     * {@code GET  /company-public-emissions/:id} : get the "id" companyPublicEmission.
     *
     * @param id the id of the companyPublicEmission to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyPublicEmission, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyPublicEmission> getCompanyPublicEmission(@PathVariable("id") Long id) {
        log.debug("REST request to get CompanyPublicEmission : {}", id);
        Optional<CompanyPublicEmission> companyPublicEmission = companyPublicEmissionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(companyPublicEmission);
    }

    /**
     * {@code DELETE  /company-public-emissions/:id} : delete the "id" companyPublicEmission.
     *
     * @param id the id of the companyPublicEmission to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompanyPublicEmission(@PathVariable("id") Long id) {
        log.debug("REST request to delete CompanyPublicEmission : {}", id);
        companyPublicEmissionRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
