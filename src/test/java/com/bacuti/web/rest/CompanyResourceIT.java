package com.bacuti.web.rest;

import static com.bacuti.domain.CompanyAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.Company;
import com.bacuti.domain.enumeration.BacutiCapsEnabled;
import com.bacuti.domain.enumeration.CompanyStatus;
import com.bacuti.domain.enumeration.IndustryType;
import com.bacuti.repository.CompanyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Base64;
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
 * Integration tests for the {@link CompanyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanyResourceIT {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_INDUSTRY = "AAAAAAAAAA";
    private static final String UPDATED_INDUSTRY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final IndustryType DEFAULT_INDUSTRY_TYPE = IndustryType.PRIMARY;
    private static final IndustryType UPDATED_INDUSTRY_TYPE = IndustryType.SECONDARY;

    private static final String DEFAULT_ADMIN = "AAAAAAAAAA";
    private static final String UPDATED_ADMIN = "BBBBBBBBBB";

    private static final CompanyStatus DEFAULT_STATUS = CompanyStatus.ACTIVE;
    private static final CompanyStatus UPDATED_STATUS = CompanyStatus.INACTIVE;

    private static final BacutiCapsEnabled DEFAULT_BACUTI_CAPS_ENABLED = BacutiCapsEnabled.ACTIVE;
    private static final BacutiCapsEnabled UPDATED_BACUTI_CAPS_ENABLED = BacutiCapsEnabled.INACTIVE;

    private static final String DEFAULT_IMAGE = "default.jpg";
    private static final String UPDATED_IMAGE = "updated.jpg";
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_CBAM_IMPACTED = false;
    private static final Boolean UPDATED_CBAM_IMPACTED = true;

    private static final String ENTITY_API_URL = "/api/companies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyMockMvc;

    private Company company;

    private Company insertedCompany;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createEntity(EntityManager em) {
        Company company = new Company()
            .companyName(DEFAULT_COMPANY_NAME)
            .description(DEFAULT_DESCRIPTION)
            .industry(DEFAULT_INDUSTRY)
            .country(DEFAULT_COUNTRY)
            .industryType(DEFAULT_INDUSTRY_TYPE)
            .admin(DEFAULT_ADMIN)
            .status(DEFAULT_STATUS)
            .bacutiCapsEnabled(DEFAULT_BACUTI_CAPS_ENABLED)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .cbamImpacted(DEFAULT_CBAM_IMPACTED);
        return company;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createUpdatedEntity(EntityManager em) {
        Company company = new Company()
            .companyName(UPDATED_COMPANY_NAME)
            .description(UPDATED_DESCRIPTION)
            .industry(UPDATED_INDUSTRY)
            .country(UPDATED_COUNTRY)
            .industryType(UPDATED_INDUSTRY_TYPE)
            .admin(UPDATED_ADMIN)
            .status(UPDATED_STATUS)
            .bacutiCapsEnabled(UPDATED_BACUTI_CAPS_ENABLED)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .cbamImpacted(UPDATED_CBAM_IMPACTED);
        return company;
    }

    @BeforeEach
    public void initTest() {
        company = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCompany != null) {
            companyRepository.delete(insertedCompany);
            insertedCompany = null;
        }
    }

    @Test
    @Transactional
    void createCompany() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Company
        var returnedCompany = om.readValue(
            restCompanyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(company)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Company.class
        );

        // Validate the Company in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCompanyUpdatableFieldsEquals(returnedCompany, getPersistedCompany(returnedCompany));

        insertedCompany = returnedCompany;
    }

    @Test
    @Transactional
    void createCompanyWithExistingId() throws Exception {
        // Create the Company with an existing ID
        company.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(company)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompanies() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].industry").value(hasItem(DEFAULT_INDUSTRY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].industryType").value(hasItem(DEFAULT_INDUSTRY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].admin").value(hasItem(DEFAULT_ADMIN)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].bacutiCapsEnabled").value(hasItem(DEFAULT_BACUTI_CAPS_ENABLED.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].cbamImpacted").value(hasItem(DEFAULT_CBAM_IMPACTED.booleanValue())));
    }

    @Test
    @Transactional
    void getCompany() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL_ID, company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(company.getId().intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.industry").value(DEFAULT_INDUSTRY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.industryType").value(DEFAULT_INDUSTRY_TYPE.toString()))
            .andExpect(jsonPath("$.admin").value(DEFAULT_ADMIN))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.bacutiCapsEnabled").value(DEFAULT_BACUTI_CAPS_ENABLED.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.cbamImpacted").value(DEFAULT_CBAM_IMPACTED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompany() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the company
        Company updatedCompany = companyRepository.findById(company.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCompany are not directly saved in db
        em.detach(updatedCompany);
        updatedCompany
            .companyName(UPDATED_COMPANY_NAME)
            .description(UPDATED_DESCRIPTION)
            .industry(UPDATED_INDUSTRY)
            .country(UPDATED_COUNTRY)
            .industryType(UPDATED_INDUSTRY_TYPE)
            .admin(UPDATED_ADMIN)
            .status(UPDATED_STATUS)
            .bacutiCapsEnabled(UPDATED_BACUTI_CAPS_ENABLED)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .cbamImpacted(UPDATED_CBAM_IMPACTED);

        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompany.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompanyToMatchAllProperties(updatedCompany);
    }

    @Test
    @Transactional
    void putNonExistingCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(put(ENTITY_API_URL_ID, company.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(company)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(company)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany
            .companyName(UPDATED_COMPANY_NAME)
            .description(UPDATED_DESCRIPTION)
            .country(UPDATED_COUNTRY)
            .industryType(UPDATED_INDUSTRY_TYPE)
            .admin(UPDATED_ADMIN)
            .status(UPDATED_STATUS)
            .bacutiCapsEnabled(UPDATED_BACUTI_CAPS_ENABLED)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompanyUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCompany, company), getPersistedCompany(company));
    }

    @Test
    @Transactional
    void fullUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany
            .companyName(UPDATED_COMPANY_NAME)
            .description(UPDATED_DESCRIPTION)
            .industry(UPDATED_INDUSTRY)
            .country(UPDATED_COUNTRY)
            .industryType(UPDATED_INDUSTRY_TYPE)
            .admin(UPDATED_ADMIN)
            .status(UPDATED_STATUS)
            .bacutiCapsEnabled(UPDATED_BACUTI_CAPS_ENABLED)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .cbamImpacted(UPDATED_CBAM_IMPACTED);

        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompanyUpdatableFieldsEquals(partialUpdatedCompany, getPersistedCompany(partialUpdatedCompany));
    }

    @Test
    @Transactional
    void patchNonExistingCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, company.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(company)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompany() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the company
        restCompanyMockMvc
            .perform(delete(ENTITY_API_URL_ID, company.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return companyRepository.count();
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

    protected Company getPersistedCompany(Company company) {
        return companyRepository.findById(company.getId()).orElseThrow();
    }

    protected void assertPersistedCompanyToMatchAllProperties(Company expectedCompany) {
        assertCompanyAllPropertiesEquals(expectedCompany, getPersistedCompany(expectedCompany));
    }

    protected void assertPersistedCompanyToMatchUpdatableProperties(Company expectedCompany) {
        assertCompanyAllUpdatablePropertiesEquals(expectedCompany, getPersistedCompany(expectedCompany));
    }
}
