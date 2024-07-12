package com.bacuti.web.rest;

import static com.bacuti.domain.SiteAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bacuti.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.Site;
import com.bacuti.repository.SiteRepository;
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
 * Integration tests for the {@link SiteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SiteResourceIT {

    private static final String DEFAULT_SITE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SITE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MANUFACTURING_SITE = false;
    private static final Boolean UPDATED_MANUFACTURING_SITE = true;

    private static final Integer DEFAULT_EMPLOYEE_COUNT = 1;
    private static final Integer UPDATED_EMPLOYEE_COUNT = 2;

    private static final Boolean DEFAULT_CBAM_IMPACTED = false;
    private static final Boolean UPDATED_CBAM_IMPACTED = true;

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LATTITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATTITUDE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(2);

    private static final String DEFAULT_UNLOCODE = "AAAAAAAAAA";
    private static final String UPDATED_UNLOCODE = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_QUALITY_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DATA_QUALITY_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_DEFAULT_VALUE_USAGE_JUSTFN = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_VALUE_USAGE_JUSTFN = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_QA_INFO = "AAAAAAAAAA";
    private static final String UPDATED_DATA_QA_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_DEFAULT_HEAT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_HEAT_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSiteMockMvc;

    private Site site;

    private Site insertedSite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Site createEntity(EntityManager em) {
        Site site = new Site()
            .siteName(DEFAULT_SITE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .manufacturingSite(DEFAULT_MANUFACTURING_SITE)
            .employeeCount(DEFAULT_EMPLOYEE_COUNT)
            .cbamImpacted(DEFAULT_CBAM_IMPACTED)
            .country(DEFAULT_COUNTRY)
            .state(DEFAULT_STATE)
            .address(DEFAULT_ADDRESS)
            .lattitude(DEFAULT_LATTITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .unlocode(DEFAULT_UNLOCODE)
            .dataQualityDesc(DEFAULT_DATA_QUALITY_DESC)
            .defaultValueUsageJustfn(DEFAULT_DEFAULT_VALUE_USAGE_JUSTFN)
            .dataQAInfo(DEFAULT_DATA_QA_INFO)
            .defaultHeatNumber(DEFAULT_DEFAULT_HEAT_NUMBER);
        return site;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Site createUpdatedEntity(EntityManager em) {
        Site site = new Site()
            .siteName(UPDATED_SITE_NAME)
            .description(UPDATED_DESCRIPTION)
            .manufacturingSite(UPDATED_MANUFACTURING_SITE)
            .employeeCount(UPDATED_EMPLOYEE_COUNT)
            .cbamImpacted(UPDATED_CBAM_IMPACTED)
            .country(UPDATED_COUNTRY)
            .state(UPDATED_STATE)
            .address(UPDATED_ADDRESS)
            .lattitude(UPDATED_LATTITUDE)
            .longitude(UPDATED_LONGITUDE)
            .unlocode(UPDATED_UNLOCODE)
            .dataQualityDesc(UPDATED_DATA_QUALITY_DESC)
            .defaultValueUsageJustfn(UPDATED_DEFAULT_VALUE_USAGE_JUSTFN)
            .dataQAInfo(UPDATED_DATA_QA_INFO)
            .defaultHeatNumber(UPDATED_DEFAULT_HEAT_NUMBER);
        return site;
    }

    @BeforeEach
    public void initTest() {
        site = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSite != null) {
            siteRepository.delete(insertedSite);
            insertedSite = null;
        }
    }

    @Test
    @Transactional
    void createSite() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Site
        var returnedSite = om.readValue(
            restSiteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(site)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Site.class
        );

        // Validate the Site in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSiteUpdatableFieldsEquals(returnedSite, getPersistedSite(returnedSite));

        insertedSite = returnedSite;
    }

    @Test
    @Transactional
    void createSiteWithExistingId() throws Exception {
        // Create the Site with an existing ID
        site.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(site)))
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSites() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList
        restSiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(site.getId().intValue())))
            .andExpect(jsonPath("$.[*].siteName").value(hasItem(DEFAULT_SITE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].manufacturingSite").value(hasItem(DEFAULT_MANUFACTURING_SITE.booleanValue())))
            .andExpect(jsonPath("$.[*].employeeCount").value(hasItem(DEFAULT_EMPLOYEE_COUNT)))
            .andExpect(jsonPath("$.[*].cbamImpacted").value(hasItem(DEFAULT_CBAM_IMPACTED.booleanValue())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].lattitude").value(hasItem(sameNumber(DEFAULT_LATTITUDE))))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(sameNumber(DEFAULT_LONGITUDE))))
            .andExpect(jsonPath("$.[*].unlocode").value(hasItem(DEFAULT_UNLOCODE)))
            .andExpect(jsonPath("$.[*].dataQualityDesc").value(hasItem(DEFAULT_DATA_QUALITY_DESC)))
            .andExpect(jsonPath("$.[*].defaultValueUsageJustfn").value(hasItem(DEFAULT_DEFAULT_VALUE_USAGE_JUSTFN)))
            .andExpect(jsonPath("$.[*].dataQAInfo").value(hasItem(DEFAULT_DATA_QA_INFO)))
            .andExpect(jsonPath("$.[*].defaultHeatNumber").value(hasItem(DEFAULT_DEFAULT_HEAT_NUMBER)));
    }

    @Test
    @Transactional
    void getSite() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get the site
        restSiteMockMvc
            .perform(get(ENTITY_API_URL_ID, site.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(site.getId().intValue()))
            .andExpect(jsonPath("$.siteName").value(DEFAULT_SITE_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.manufacturingSite").value(DEFAULT_MANUFACTURING_SITE.booleanValue()))
            .andExpect(jsonPath("$.employeeCount").value(DEFAULT_EMPLOYEE_COUNT))
            .andExpect(jsonPath("$.cbamImpacted").value(DEFAULT_CBAM_IMPACTED.booleanValue()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.lattitude").value(sameNumber(DEFAULT_LATTITUDE)))
            .andExpect(jsonPath("$.longitude").value(sameNumber(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.unlocode").value(DEFAULT_UNLOCODE))
            .andExpect(jsonPath("$.dataQualityDesc").value(DEFAULT_DATA_QUALITY_DESC))
            .andExpect(jsonPath("$.defaultValueUsageJustfn").value(DEFAULT_DEFAULT_VALUE_USAGE_JUSTFN))
            .andExpect(jsonPath("$.dataQAInfo").value(DEFAULT_DATA_QA_INFO))
            .andExpect(jsonPath("$.defaultHeatNumber").value(DEFAULT_DEFAULT_HEAT_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingSite() throws Exception {
        // Get the site
        restSiteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSite() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the site
        Site updatedSite = siteRepository.findById(site.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSite are not directly saved in db
        em.detach(updatedSite);
        updatedSite
            .siteName(UPDATED_SITE_NAME)
            .description(UPDATED_DESCRIPTION)
            .manufacturingSite(UPDATED_MANUFACTURING_SITE)
            .employeeCount(UPDATED_EMPLOYEE_COUNT)
            .cbamImpacted(UPDATED_CBAM_IMPACTED)
            .country(UPDATED_COUNTRY)
            .state(UPDATED_STATE)
            .address(UPDATED_ADDRESS)
            .lattitude(UPDATED_LATTITUDE)
            .longitude(UPDATED_LONGITUDE)
            .unlocode(UPDATED_UNLOCODE)
            .dataQualityDesc(UPDATED_DATA_QUALITY_DESC)
            .defaultValueUsageJustfn(UPDATED_DEFAULT_VALUE_USAGE_JUSTFN)
            .dataQAInfo(UPDATED_DATA_QA_INFO)
            .defaultHeatNumber(UPDATED_DEFAULT_HEAT_NUMBER);

        restSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSite))
            )
            .andExpect(status().isOk());

        // Validate the Site in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSiteToMatchAllProperties(updatedSite);
    }

    @Test
    @Transactional
    void putNonExistingSite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        site.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(put(ENTITY_API_URL_ID, site.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(site)))
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        site.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(site))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        site.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(site)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Site in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSiteWithPatch() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the site using partial update
        Site partialUpdatedSite = new Site();
        partialUpdatedSite.setId(site.getId());

        partialUpdatedSite
            .siteName(UPDATED_SITE_NAME)
            .manufacturingSite(UPDATED_MANUFACTURING_SITE)
            .cbamImpacted(UPDATED_CBAM_IMPACTED)
            .state(UPDATED_STATE)
            .defaultValueUsageJustfn(UPDATED_DEFAULT_VALUE_USAGE_JUSTFN);

        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSite))
            )
            .andExpect(status().isOk());

        // Validate the Site in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSiteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSite, site), getPersistedSite(site));
    }

    @Test
    @Transactional
    void fullUpdateSiteWithPatch() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the site using partial update
        Site partialUpdatedSite = new Site();
        partialUpdatedSite.setId(site.getId());

        partialUpdatedSite
            .siteName(UPDATED_SITE_NAME)
            .description(UPDATED_DESCRIPTION)
            .manufacturingSite(UPDATED_MANUFACTURING_SITE)
            .employeeCount(UPDATED_EMPLOYEE_COUNT)
            .cbamImpacted(UPDATED_CBAM_IMPACTED)
            .country(UPDATED_COUNTRY)
            .state(UPDATED_STATE)
            .address(UPDATED_ADDRESS)
            .lattitude(UPDATED_LATTITUDE)
            .longitude(UPDATED_LONGITUDE)
            .unlocode(UPDATED_UNLOCODE)
            .dataQualityDesc(UPDATED_DATA_QUALITY_DESC)
            .defaultValueUsageJustfn(UPDATED_DEFAULT_VALUE_USAGE_JUSTFN)
            .dataQAInfo(UPDATED_DATA_QA_INFO)
            .defaultHeatNumber(UPDATED_DEFAULT_HEAT_NUMBER);

        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSite))
            )
            .andExpect(status().isOk());

        // Validate the Site in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSiteUpdatableFieldsEquals(partialUpdatedSite, getPersistedSite(partialUpdatedSite));
    }

    @Test
    @Transactional
    void patchNonExistingSite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        site.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(patch(ENTITY_API_URL_ID, site.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(site)))
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        site.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(site))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        site.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(site)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Site in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSite() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the site
        restSiteMockMvc
            .perform(delete(ENTITY_API_URL_ID, site.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return siteRepository.count();
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

    protected Site getPersistedSite(Site site) {
        return siteRepository.findById(site.getId()).orElseThrow();
    }

    protected void assertPersistedSiteToMatchAllProperties(Site expectedSite) {
        assertSiteAllPropertiesEquals(expectedSite, getPersistedSite(expectedSite));
    }

    protected void assertPersistedSiteToMatchUpdatableProperties(Site expectedSite) {
        assertSiteAllUpdatablePropertiesEquals(expectedSite, getPersistedSite(expectedSite));
    }
}
