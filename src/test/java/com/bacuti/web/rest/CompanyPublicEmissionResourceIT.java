package com.bacuti.web.rest;

import static com.bacuti.domain.CompanyPublicEmissionAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bacuti.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.CompanyPublicEmission;
import com.bacuti.repository.CompanyPublicEmissionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CompanyPublicEmissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanyPublicEmissionResourceIT {

    private static final String DEFAULT_REPORTING_COMPANY = "AAAAAAAAAA";
    private static final String UPDATED_REPORTING_COMPANY = "BBBBBBBBBB";

    private static final Integer DEFAULT_REPORTING_YEAR = 1;
    private static final Integer UPDATED_REPORTING_YEAR = 2;

    private static final BigDecimal DEFAULT_REVENUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_REVENUE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_ENERGY_IN_MWH = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_ENERGY_IN_MWH = new BigDecimal(2);

    private static final Integer DEFAULT_PERMANENT_EMPLOYEES = 1;
    private static final Integer UPDATED_PERMANENT_EMPLOYEES = 2;

    private static final BigDecimal DEFAULT_SCOPE_1 = new BigDecimal(1);
    private static final BigDecimal UPDATED_SCOPE_1 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SCOPE_2_LOCATION = new BigDecimal(1);
    private static final BigDecimal UPDATED_SCOPE_2_LOCATION = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SCOPE_2_MARKET = new BigDecimal(1);
    private static final BigDecimal UPDATED_SCOPE_2_MARKET = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SCOPE_3 = new BigDecimal(1);
    private static final BigDecimal UPDATED_SCOPE_3 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CATEGORY_1 = new BigDecimal(1);
    private static final BigDecimal UPDATED_CATEGORY_1 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CATEGORY_2 = new BigDecimal(1);
    private static final BigDecimal UPDATED_CATEGORY_2 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CATEGORY_3 = new BigDecimal(1);
    private static final BigDecimal UPDATED_CATEGORY_3 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CATEGORY_4 = new BigDecimal(1);
    private static final BigDecimal UPDATED_CATEGORY_4 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CATEGORY_5 = new BigDecimal(1);
    private static final BigDecimal UPDATED_CATEGORY_5 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CATEGORY_6 = new BigDecimal(1);
    private static final BigDecimal UPDATED_CATEGORY_6 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CATEGORY_7 = new BigDecimal(1);
    private static final BigDecimal UPDATED_CATEGORY_7 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CATEGORY_8 = new BigDecimal(1);
    private static final BigDecimal UPDATED_CATEGORY_8 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CATEGORY_9 = new BigDecimal(1);
    private static final BigDecimal UPDATED_CATEGORY_9 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CATEGORY_10 = new BigDecimal(1);
    private static final BigDecimal UPDATED_CATEGORY_10 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CATEGORY_11 = new BigDecimal(1);
    private static final BigDecimal UPDATED_CATEGORY_11 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CATEGORY_12 = new BigDecimal(1);
    private static final BigDecimal UPDATED_CATEGORY_12 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CATEGORY_13 = new BigDecimal(1);
    private static final BigDecimal UPDATED_CATEGORY_13 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CATEGORY_14 = new BigDecimal(1);
    private static final BigDecimal UPDATED_CATEGORY_14 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CATEGORY_15 = new BigDecimal(1);
    private static final BigDecimal UPDATED_CATEGORY_15 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INTENSITY_SCOPE_1 = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTENSITY_SCOPE_1 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INTENSITY_SCOPE_1_LOCTION = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTENSITY_SCOPE_1_LOCTION = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INTENSITYSCOPE_2_MARKET = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTENSITYSCOPE_2_MARKET = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INTENSITY_SCOPE_3 = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTENSITY_SCOPE_3 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INTENSITY_SCOPE_12 = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTENSITY_SCOPE_12 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INTENSITY_SCOPE_123 = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTENSITY_SCOPE_123 = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ACTIVITY_LEVEL = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACTIVITY_LEVEL = new BigDecimal(2);

    private static final String DEFAULT_DATA_SOURCE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DATA_SOURCE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DISCLOSURE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DISCLOSURE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_DATA_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_INDUSTRY_CODES = "AAAAAAAAAA";
    private static final String UPDATED_INDUSTRY_CODES = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_ACTIVITIES = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_ACTIVITIES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/company-public-emissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompanyPublicEmissionRepository companyPublicEmissionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyPublicEmissionMockMvc;

    private CompanyPublicEmission companyPublicEmission;

    private CompanyPublicEmission insertedCompanyPublicEmission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyPublicEmission createEntity(EntityManager em) {
        CompanyPublicEmission companyPublicEmission = new CompanyPublicEmission()
            .reportingCompany(DEFAULT_REPORTING_COMPANY)
            .reportingYear(DEFAULT_REPORTING_YEAR)
            .revenue(DEFAULT_REVENUE)
            .totalEnergyInMwh(DEFAULT_TOTAL_ENERGY_IN_MWH)
            .permanentEmployees(DEFAULT_PERMANENT_EMPLOYEES)
            .scope1(DEFAULT_SCOPE_1)
            .scope2Location(DEFAULT_SCOPE_2_LOCATION)
            .scope2Market(DEFAULT_SCOPE_2_MARKET)
            .scope3(DEFAULT_SCOPE_3)
            .category1(DEFAULT_CATEGORY_1)
            .category2(DEFAULT_CATEGORY_2)
            .category3(DEFAULT_CATEGORY_3)
            .category4(DEFAULT_CATEGORY_4)
            .category5(DEFAULT_CATEGORY_5)
            .category6(DEFAULT_CATEGORY_6)
            .category7(DEFAULT_CATEGORY_7)
            .category8(DEFAULT_CATEGORY_8)
            .category9(DEFAULT_CATEGORY_9)
            .category10(DEFAULT_CATEGORY_10)
            .category11(DEFAULT_CATEGORY_11)
            .category12(DEFAULT_CATEGORY_12)
            .category13(DEFAULT_CATEGORY_13)
            .category14(DEFAULT_CATEGORY_14)
            .category15(DEFAULT_CATEGORY_15)
            .intensityScope1(DEFAULT_INTENSITY_SCOPE_1)
            .intensityScope1Loction(DEFAULT_INTENSITY_SCOPE_1_LOCTION)
            .intensityscope2Market(DEFAULT_INTENSITYSCOPE_2_MARKET)
            .intensityScope3(DEFAULT_INTENSITY_SCOPE_3)
            .intensityScope12(DEFAULT_INTENSITY_SCOPE_12)
            .intensityScope123(DEFAULT_INTENSITY_SCOPE_123)
            .activityLevel(DEFAULT_ACTIVITY_LEVEL)
            .dataSourceType(DEFAULT_DATA_SOURCE_TYPE)
            .disclosureType(DEFAULT_DISCLOSURE_TYPE)
            .dataSource(DEFAULT_DATA_SOURCE)
            .industryCodes(DEFAULT_INDUSTRY_CODES)
            .codeType(DEFAULT_CODE_TYPE)
            .companyWebsite(DEFAULT_COMPANY_WEBSITE)
            .companyActivities(DEFAULT_COMPANY_ACTIVITIES);
        return companyPublicEmission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyPublicEmission createUpdatedEntity(EntityManager em) {
        CompanyPublicEmission companyPublicEmission = new CompanyPublicEmission()
            .reportingCompany(UPDATED_REPORTING_COMPANY)
            .reportingYear(UPDATED_REPORTING_YEAR)
            .revenue(UPDATED_REVENUE)
            .totalEnergyInMwh(UPDATED_TOTAL_ENERGY_IN_MWH)
            .permanentEmployees(UPDATED_PERMANENT_EMPLOYEES)
            .scope1(UPDATED_SCOPE_1)
            .scope2Location(UPDATED_SCOPE_2_LOCATION)
            .scope2Market(UPDATED_SCOPE_2_MARKET)
            .scope3(UPDATED_SCOPE_3)
            .category1(UPDATED_CATEGORY_1)
            .category2(UPDATED_CATEGORY_2)
            .category3(UPDATED_CATEGORY_3)
            .category4(UPDATED_CATEGORY_4)
            .category5(UPDATED_CATEGORY_5)
            .category6(UPDATED_CATEGORY_6)
            .category7(UPDATED_CATEGORY_7)
            .category8(UPDATED_CATEGORY_8)
            .category9(UPDATED_CATEGORY_9)
            .category10(UPDATED_CATEGORY_10)
            .category11(UPDATED_CATEGORY_11)
            .category12(UPDATED_CATEGORY_12)
            .category13(UPDATED_CATEGORY_13)
            .category14(UPDATED_CATEGORY_14)
            .category15(UPDATED_CATEGORY_15)
            .intensityScope1(UPDATED_INTENSITY_SCOPE_1)
            .intensityScope1Loction(UPDATED_INTENSITY_SCOPE_1_LOCTION)
            .intensityscope2Market(UPDATED_INTENSITYSCOPE_2_MARKET)
            .intensityScope3(UPDATED_INTENSITY_SCOPE_3)
            .intensityScope12(UPDATED_INTENSITY_SCOPE_12)
            .intensityScope123(UPDATED_INTENSITY_SCOPE_123)
            .activityLevel(UPDATED_ACTIVITY_LEVEL)
            .dataSourceType(UPDATED_DATA_SOURCE_TYPE)
            .disclosureType(UPDATED_DISCLOSURE_TYPE)
            .dataSource(UPDATED_DATA_SOURCE)
            .industryCodes(UPDATED_INDUSTRY_CODES)
            .codeType(UPDATED_CODE_TYPE)
            .companyWebsite(UPDATED_COMPANY_WEBSITE)
            .companyActivities(UPDATED_COMPANY_ACTIVITIES);
        return companyPublicEmission;
    }

    @BeforeEach
    public void initTest() {
        companyPublicEmission = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCompanyPublicEmission != null) {
            companyPublicEmissionRepository.delete(insertedCompanyPublicEmission);
            insertedCompanyPublicEmission = null;
        }
    }

    @Test
    @Transactional
    void createCompanyPublicEmission() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CompanyPublicEmission
        var returnedCompanyPublicEmission = om.readValue(
            restCompanyPublicEmissionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companyPublicEmission)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CompanyPublicEmission.class
        );

        // Validate the CompanyPublicEmission in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCompanyPublicEmissionUpdatableFieldsEquals(
            returnedCompanyPublicEmission,
            getPersistedCompanyPublicEmission(returnedCompanyPublicEmission)
        );

        insertedCompanyPublicEmission = returnedCompanyPublicEmission;
    }

    @Test
    @Transactional
    void createCompanyPublicEmissionWithExistingId() throws Exception {
        // Create the CompanyPublicEmission with an existing ID
        companyPublicEmission.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyPublicEmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companyPublicEmission)))
            .andExpect(status().isBadRequest());

        // Validate the CompanyPublicEmission in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompanyPublicEmissions() throws Exception {
        // Initialize the database
        insertedCompanyPublicEmission = companyPublicEmissionRepository.saveAndFlush(companyPublicEmission);

        // Get all the companyPublicEmissionList
        restCompanyPublicEmissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyPublicEmission.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingCompany").value(hasItem(DEFAULT_REPORTING_COMPANY)))
            .andExpect(jsonPath("$.[*].reportingYear").value(hasItem(DEFAULT_REPORTING_YEAR)))
            .andExpect(jsonPath("$.[*].revenue").value(hasItem(sameNumber(DEFAULT_REVENUE))))
            .andExpect(jsonPath("$.[*].totalEnergyInMwh").value(hasItem(sameNumber(DEFAULT_TOTAL_ENERGY_IN_MWH))))
            .andExpect(jsonPath("$.[*].permanentEmployees").value(hasItem(DEFAULT_PERMANENT_EMPLOYEES)))
            .andExpect(jsonPath("$.[*].scope1").value(hasItem(sameNumber(DEFAULT_SCOPE_1))))
            .andExpect(jsonPath("$.[*].scope2Location").value(hasItem(sameNumber(DEFAULT_SCOPE_2_LOCATION))))
            .andExpect(jsonPath("$.[*].scope2Market").value(hasItem(sameNumber(DEFAULT_SCOPE_2_MARKET))))
            .andExpect(jsonPath("$.[*].scope3").value(hasItem(sameNumber(DEFAULT_SCOPE_3))))
            .andExpect(jsonPath("$.[*].category1").value(hasItem(sameNumber(DEFAULT_CATEGORY_1))))
            .andExpect(jsonPath("$.[*].category2").value(hasItem(sameNumber(DEFAULT_CATEGORY_2))))
            .andExpect(jsonPath("$.[*].category3").value(hasItem(sameNumber(DEFAULT_CATEGORY_3))))
            .andExpect(jsonPath("$.[*].category4").value(hasItem(sameNumber(DEFAULT_CATEGORY_4))))
            .andExpect(jsonPath("$.[*].category5").value(hasItem(sameNumber(DEFAULT_CATEGORY_5))))
            .andExpect(jsonPath("$.[*].category6").value(hasItem(sameNumber(DEFAULT_CATEGORY_6))))
            .andExpect(jsonPath("$.[*].category7").value(hasItem(sameNumber(DEFAULT_CATEGORY_7))))
            .andExpect(jsonPath("$.[*].category8").value(hasItem(sameNumber(DEFAULT_CATEGORY_8))))
            .andExpect(jsonPath("$.[*].category9").value(hasItem(sameNumber(DEFAULT_CATEGORY_9))))
            .andExpect(jsonPath("$.[*].category10").value(hasItem(sameNumber(DEFAULT_CATEGORY_10))))
            .andExpect(jsonPath("$.[*].category11").value(hasItem(sameNumber(DEFAULT_CATEGORY_11))))
            .andExpect(jsonPath("$.[*].category12").value(hasItem(sameNumber(DEFAULT_CATEGORY_12))))
            .andExpect(jsonPath("$.[*].category13").value(hasItem(sameNumber(DEFAULT_CATEGORY_13))))
            .andExpect(jsonPath("$.[*].category14").value(hasItem(sameNumber(DEFAULT_CATEGORY_14))))
            .andExpect(jsonPath("$.[*].category15").value(hasItem(sameNumber(DEFAULT_CATEGORY_15))))
            .andExpect(jsonPath("$.[*].intensityScope1").value(hasItem(sameNumber(DEFAULT_INTENSITY_SCOPE_1))))
            .andExpect(jsonPath("$.[*].intensityScope1Loction").value(hasItem(sameNumber(DEFAULT_INTENSITY_SCOPE_1_LOCTION))))
            .andExpect(jsonPath("$.[*].intensityscope2Market").value(hasItem(sameNumber(DEFAULT_INTENSITYSCOPE_2_MARKET))))
            .andExpect(jsonPath("$.[*].intensityScope3").value(hasItem(sameNumber(DEFAULT_INTENSITY_SCOPE_3))))
            .andExpect(jsonPath("$.[*].intensityScope12").value(hasItem(sameNumber(DEFAULT_INTENSITY_SCOPE_12))))
            .andExpect(jsonPath("$.[*].intensityScope123").value(hasItem(sameNumber(DEFAULT_INTENSITY_SCOPE_123))))
            .andExpect(jsonPath("$.[*].activityLevel").value(hasItem(sameNumber(DEFAULT_ACTIVITY_LEVEL))))
            .andExpect(jsonPath("$.[*].dataSourceType").value(hasItem(DEFAULT_DATA_SOURCE_TYPE)))
            .andExpect(jsonPath("$.[*].disclosureType").value(hasItem(DEFAULT_DISCLOSURE_TYPE)))
            .andExpect(jsonPath("$.[*].dataSource").value(hasItem(DEFAULT_DATA_SOURCE)))
            .andExpect(jsonPath("$.[*].industryCodes").value(hasItem(DEFAULT_INDUSTRY_CODES)))
            .andExpect(jsonPath("$.[*].codeType").value(hasItem(DEFAULT_CODE_TYPE)))
            .andExpect(jsonPath("$.[*].companyWebsite").value(hasItem(DEFAULT_COMPANY_WEBSITE)))
            .andExpect(jsonPath("$.[*].companyActivities").value(hasItem(DEFAULT_COMPANY_ACTIVITIES)));
    }

    @Test
    @Transactional
    void getCompanyPublicEmission() throws Exception {
        // Initialize the database
        insertedCompanyPublicEmission = companyPublicEmissionRepository.saveAndFlush(companyPublicEmission);

        // Get the companyPublicEmission
        restCompanyPublicEmissionMockMvc
            .perform(get(ENTITY_API_URL_ID, companyPublicEmission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(companyPublicEmission.getId().intValue()))
            .andExpect(jsonPath("$.reportingCompany").value(DEFAULT_REPORTING_COMPANY))
            .andExpect(jsonPath("$.reportingYear").value(DEFAULT_REPORTING_YEAR))
            .andExpect(jsonPath("$.revenue").value(sameNumber(DEFAULT_REVENUE)))
            .andExpect(jsonPath("$.totalEnergyInMwh").value(sameNumber(DEFAULT_TOTAL_ENERGY_IN_MWH)))
            .andExpect(jsonPath("$.permanentEmployees").value(DEFAULT_PERMANENT_EMPLOYEES))
            .andExpect(jsonPath("$.scope1").value(sameNumber(DEFAULT_SCOPE_1)))
            .andExpect(jsonPath("$.scope2Location").value(sameNumber(DEFAULT_SCOPE_2_LOCATION)))
            .andExpect(jsonPath("$.scope2Market").value(sameNumber(DEFAULT_SCOPE_2_MARKET)))
            .andExpect(jsonPath("$.scope3").value(sameNumber(DEFAULT_SCOPE_3)))
            .andExpect(jsonPath("$.category1").value(sameNumber(DEFAULT_CATEGORY_1)))
            .andExpect(jsonPath("$.category2").value(sameNumber(DEFAULT_CATEGORY_2)))
            .andExpect(jsonPath("$.category3").value(sameNumber(DEFAULT_CATEGORY_3)))
            .andExpect(jsonPath("$.category4").value(sameNumber(DEFAULT_CATEGORY_4)))
            .andExpect(jsonPath("$.category5").value(sameNumber(DEFAULT_CATEGORY_5)))
            .andExpect(jsonPath("$.category6").value(sameNumber(DEFAULT_CATEGORY_6)))
            .andExpect(jsonPath("$.category7").value(sameNumber(DEFAULT_CATEGORY_7)))
            .andExpect(jsonPath("$.category8").value(sameNumber(DEFAULT_CATEGORY_8)))
            .andExpect(jsonPath("$.category9").value(sameNumber(DEFAULT_CATEGORY_9)))
            .andExpect(jsonPath("$.category10").value(sameNumber(DEFAULT_CATEGORY_10)))
            .andExpect(jsonPath("$.category11").value(sameNumber(DEFAULT_CATEGORY_11)))
            .andExpect(jsonPath("$.category12").value(sameNumber(DEFAULT_CATEGORY_12)))
            .andExpect(jsonPath("$.category13").value(sameNumber(DEFAULT_CATEGORY_13)))
            .andExpect(jsonPath("$.category14").value(sameNumber(DEFAULT_CATEGORY_14)))
            .andExpect(jsonPath("$.category15").value(sameNumber(DEFAULT_CATEGORY_15)))
            .andExpect(jsonPath("$.intensityScope1").value(sameNumber(DEFAULT_INTENSITY_SCOPE_1)))
            .andExpect(jsonPath("$.intensityScope1Loction").value(sameNumber(DEFAULT_INTENSITY_SCOPE_1_LOCTION)))
            .andExpect(jsonPath("$.intensityscope2Market").value(sameNumber(DEFAULT_INTENSITYSCOPE_2_MARKET)))
            .andExpect(jsonPath("$.intensityScope3").value(sameNumber(DEFAULT_INTENSITY_SCOPE_3)))
            .andExpect(jsonPath("$.intensityScope12").value(sameNumber(DEFAULT_INTENSITY_SCOPE_12)))
            .andExpect(jsonPath("$.intensityScope123").value(sameNumber(DEFAULT_INTENSITY_SCOPE_123)))
            .andExpect(jsonPath("$.activityLevel").value(sameNumber(DEFAULT_ACTIVITY_LEVEL)))
            .andExpect(jsonPath("$.dataSourceType").value(DEFAULT_DATA_SOURCE_TYPE))
            .andExpect(jsonPath("$.disclosureType").value(DEFAULT_DISCLOSURE_TYPE))
            .andExpect(jsonPath("$.dataSource").value(DEFAULT_DATA_SOURCE))
            .andExpect(jsonPath("$.industryCodes").value(DEFAULT_INDUSTRY_CODES))
            .andExpect(jsonPath("$.codeType").value(DEFAULT_CODE_TYPE))
            .andExpect(jsonPath("$.companyWebsite").value(DEFAULT_COMPANY_WEBSITE))
            .andExpect(jsonPath("$.companyActivities").value(DEFAULT_COMPANY_ACTIVITIES));
    }

    @Test
    @Transactional
    void getNonExistingCompanyPublicEmission() throws Exception {
        // Get the companyPublicEmission
        restCompanyPublicEmissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompanyPublicEmission() throws Exception {
        // Initialize the database
        insertedCompanyPublicEmission = companyPublicEmissionRepository.saveAndFlush(companyPublicEmission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the companyPublicEmission
        CompanyPublicEmission updatedCompanyPublicEmission = companyPublicEmissionRepository
            .findById(companyPublicEmission.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCompanyPublicEmission are not directly saved in db
        em.detach(updatedCompanyPublicEmission);
        updatedCompanyPublicEmission
            .reportingCompany(UPDATED_REPORTING_COMPANY)
            .reportingYear(UPDATED_REPORTING_YEAR)
            .revenue(UPDATED_REVENUE)
            .totalEnergyInMwh(UPDATED_TOTAL_ENERGY_IN_MWH)
            .permanentEmployees(UPDATED_PERMANENT_EMPLOYEES)
            .scope1(UPDATED_SCOPE_1)
            .scope2Location(UPDATED_SCOPE_2_LOCATION)
            .scope2Market(UPDATED_SCOPE_2_MARKET)
            .scope3(UPDATED_SCOPE_3)
            .category1(UPDATED_CATEGORY_1)
            .category2(UPDATED_CATEGORY_2)
            .category3(UPDATED_CATEGORY_3)
            .category4(UPDATED_CATEGORY_4)
            .category5(UPDATED_CATEGORY_5)
            .category6(UPDATED_CATEGORY_6)
            .category7(UPDATED_CATEGORY_7)
            .category8(UPDATED_CATEGORY_8)
            .category9(UPDATED_CATEGORY_9)
            .category10(UPDATED_CATEGORY_10)
            .category11(UPDATED_CATEGORY_11)
            .category12(UPDATED_CATEGORY_12)
            .category13(UPDATED_CATEGORY_13)
            .category14(UPDATED_CATEGORY_14)
            .category15(UPDATED_CATEGORY_15)
            .intensityScope1(UPDATED_INTENSITY_SCOPE_1)
            .intensityScope1Loction(UPDATED_INTENSITY_SCOPE_1_LOCTION)
            .intensityscope2Market(UPDATED_INTENSITYSCOPE_2_MARKET)
            .intensityScope3(UPDATED_INTENSITY_SCOPE_3)
            .intensityScope12(UPDATED_INTENSITY_SCOPE_12)
            .intensityScope123(UPDATED_INTENSITY_SCOPE_123)
            .activityLevel(UPDATED_ACTIVITY_LEVEL)
            .dataSourceType(UPDATED_DATA_SOURCE_TYPE)
            .disclosureType(UPDATED_DISCLOSURE_TYPE)
            .dataSource(UPDATED_DATA_SOURCE)
            .industryCodes(UPDATED_INDUSTRY_CODES)
            .codeType(UPDATED_CODE_TYPE)
            .companyWebsite(UPDATED_COMPANY_WEBSITE)
            .companyActivities(UPDATED_COMPANY_ACTIVITIES);

        restCompanyPublicEmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompanyPublicEmission.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCompanyPublicEmission))
            )
            .andExpect(status().isOk());

        // Validate the CompanyPublicEmission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompanyPublicEmissionToMatchAllProperties(updatedCompanyPublicEmission);
    }

    @Test
    @Transactional
    void putNonExistingCompanyPublicEmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyPublicEmission.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyPublicEmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyPublicEmission.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(companyPublicEmission))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyPublicEmission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompanyPublicEmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyPublicEmission.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyPublicEmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(companyPublicEmission))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyPublicEmission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompanyPublicEmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyPublicEmission.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyPublicEmissionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companyPublicEmission)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyPublicEmission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyPublicEmissionWithPatch() throws Exception {
        // Initialize the database
        insertedCompanyPublicEmission = companyPublicEmissionRepository.saveAndFlush(companyPublicEmission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the companyPublicEmission using partial update
        CompanyPublicEmission partialUpdatedCompanyPublicEmission = new CompanyPublicEmission();
        partialUpdatedCompanyPublicEmission.setId(companyPublicEmission.getId());

        partialUpdatedCompanyPublicEmission
            .reportingCompany(UPDATED_REPORTING_COMPANY)
            .reportingYear(UPDATED_REPORTING_YEAR)
            .totalEnergyInMwh(UPDATED_TOTAL_ENERGY_IN_MWH)
            .permanentEmployees(UPDATED_PERMANENT_EMPLOYEES)
            .scope2Location(UPDATED_SCOPE_2_LOCATION)
            .scope2Market(UPDATED_SCOPE_2_MARKET)
            .scope3(UPDATED_SCOPE_3)
            .category1(UPDATED_CATEGORY_1)
            .category4(UPDATED_CATEGORY_4)
            .category5(UPDATED_CATEGORY_5)
            .category6(UPDATED_CATEGORY_6)
            .category11(UPDATED_CATEGORY_11)
            .category12(UPDATED_CATEGORY_12)
            .category14(UPDATED_CATEGORY_14)
            .intensityScope1(UPDATED_INTENSITY_SCOPE_1)
            .intensityScope1Loction(UPDATED_INTENSITY_SCOPE_1_LOCTION)
            .intensityScope3(UPDATED_INTENSITY_SCOPE_3)
            .dataSourceType(UPDATED_DATA_SOURCE_TYPE)
            .disclosureType(UPDATED_DISCLOSURE_TYPE)
            .companyWebsite(UPDATED_COMPANY_WEBSITE);

        restCompanyPublicEmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyPublicEmission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompanyPublicEmission))
            )
            .andExpect(status().isOk());

        // Validate the CompanyPublicEmission in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompanyPublicEmissionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCompanyPublicEmission, companyPublicEmission),
            getPersistedCompanyPublicEmission(companyPublicEmission)
        );
    }

    @Test
    @Transactional
    void fullUpdateCompanyPublicEmissionWithPatch() throws Exception {
        // Initialize the database
        insertedCompanyPublicEmission = companyPublicEmissionRepository.saveAndFlush(companyPublicEmission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the companyPublicEmission using partial update
        CompanyPublicEmission partialUpdatedCompanyPublicEmission = new CompanyPublicEmission();
        partialUpdatedCompanyPublicEmission.setId(companyPublicEmission.getId());

        partialUpdatedCompanyPublicEmission
            .reportingCompany(UPDATED_REPORTING_COMPANY)
            .reportingYear(UPDATED_REPORTING_YEAR)
            .revenue(UPDATED_REVENUE)
            .totalEnergyInMwh(UPDATED_TOTAL_ENERGY_IN_MWH)
            .permanentEmployees(UPDATED_PERMANENT_EMPLOYEES)
            .scope1(UPDATED_SCOPE_1)
            .scope2Location(UPDATED_SCOPE_2_LOCATION)
            .scope2Market(UPDATED_SCOPE_2_MARKET)
            .scope3(UPDATED_SCOPE_3)
            .category1(UPDATED_CATEGORY_1)
            .category2(UPDATED_CATEGORY_2)
            .category3(UPDATED_CATEGORY_3)
            .category4(UPDATED_CATEGORY_4)
            .category5(UPDATED_CATEGORY_5)
            .category6(UPDATED_CATEGORY_6)
            .category7(UPDATED_CATEGORY_7)
            .category8(UPDATED_CATEGORY_8)
            .category9(UPDATED_CATEGORY_9)
            .category10(UPDATED_CATEGORY_10)
            .category11(UPDATED_CATEGORY_11)
            .category12(UPDATED_CATEGORY_12)
            .category13(UPDATED_CATEGORY_13)
            .category14(UPDATED_CATEGORY_14)
            .category15(UPDATED_CATEGORY_15)
            .intensityScope1(UPDATED_INTENSITY_SCOPE_1)
            .intensityScope1Loction(UPDATED_INTENSITY_SCOPE_1_LOCTION)
            .intensityscope2Market(UPDATED_INTENSITYSCOPE_2_MARKET)
            .intensityScope3(UPDATED_INTENSITY_SCOPE_3)
            .intensityScope12(UPDATED_INTENSITY_SCOPE_12)
            .intensityScope123(UPDATED_INTENSITY_SCOPE_123)
            .activityLevel(UPDATED_ACTIVITY_LEVEL)
            .dataSourceType(UPDATED_DATA_SOURCE_TYPE)
            .disclosureType(UPDATED_DISCLOSURE_TYPE)
            .dataSource(UPDATED_DATA_SOURCE)
            .industryCodes(UPDATED_INDUSTRY_CODES)
            .codeType(UPDATED_CODE_TYPE)
            .companyWebsite(UPDATED_COMPANY_WEBSITE)
            .companyActivities(UPDATED_COMPANY_ACTIVITIES);

        restCompanyPublicEmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyPublicEmission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompanyPublicEmission))
            )
            .andExpect(status().isOk());

        // Validate the CompanyPublicEmission in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompanyPublicEmissionUpdatableFieldsEquals(
            partialUpdatedCompanyPublicEmission,
            getPersistedCompanyPublicEmission(partialUpdatedCompanyPublicEmission)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCompanyPublicEmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyPublicEmission.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyPublicEmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, companyPublicEmission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(companyPublicEmission))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyPublicEmission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompanyPublicEmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyPublicEmission.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyPublicEmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(companyPublicEmission))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyPublicEmission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompanyPublicEmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyPublicEmission.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyPublicEmissionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(companyPublicEmission)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyPublicEmission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompanyPublicEmission() throws Exception {
        // Initialize the database
        insertedCompanyPublicEmission = companyPublicEmissionRepository.saveAndFlush(companyPublicEmission);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the companyPublicEmission
        restCompanyPublicEmissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, companyPublicEmission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return companyPublicEmissionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CompanyPublicEmission getPersistedCompanyPublicEmission(CompanyPublicEmission companyPublicEmission) {
        return companyPublicEmissionRepository.findById(companyPublicEmission.getId()).orElseThrow();
    }

    protected void assertPersistedCompanyPublicEmissionToMatchAllProperties(CompanyPublicEmission expectedCompanyPublicEmission) {
        assertCompanyPublicEmissionAllPropertiesEquals(
            expectedCompanyPublicEmission,
            getPersistedCompanyPublicEmission(expectedCompanyPublicEmission)
        );
    }

    protected void assertPersistedCompanyPublicEmissionToMatchUpdatableProperties(CompanyPublicEmission expectedCompanyPublicEmission) {
        assertCompanyPublicEmissionAllUpdatablePropertiesEquals(
            expectedCompanyPublicEmission,
            getPersistedCompanyPublicEmission(expectedCompanyPublicEmission)
        );
    }
}
