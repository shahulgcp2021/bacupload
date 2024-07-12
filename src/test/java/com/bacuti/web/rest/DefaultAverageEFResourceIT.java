package com.bacuti.web.rest;

import static com.bacuti.domain.DefaultAverageEFAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bacuti.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.DefaultAverageEF;
import com.bacuti.repository.DefaultAverageEFRepository;
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
 * Integration tests for the {@link DefaultAverageEFResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DefaultAverageEFResourceIT {

    private static final String DEFAULT_DOMAIN = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN = "BBBBBBBBBB";

    private static final String DEFAULT_DETAIL = "AAAAAAAAAA";
    private static final String UPDATED_DETAIL = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_OR_REGION = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_OR_REGION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_EMISSION_FACTOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_EMISSION_FACTOR = new BigDecimal(2);

    private static final String DEFAULT_EF_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_EF_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/default-average-efs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DefaultAverageEFRepository defaultAverageEFRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDefaultAverageEFMockMvc;

    private DefaultAverageEF defaultAverageEF;

    private DefaultAverageEF insertedDefaultAverageEF;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DefaultAverageEF createEntity(EntityManager em) {
        DefaultAverageEF defaultAverageEF = new DefaultAverageEF()
            .domain(DEFAULT_DOMAIN)
            .detail(DEFAULT_DETAIL)
            .countryOrRegion(DEFAULT_COUNTRY_OR_REGION)
            .emissionFactor(DEFAULT_EMISSION_FACTOR)
            .efSource(DEFAULT_EF_SOURCE)
            .code(DEFAULT_CODE)
            .codeType(DEFAULT_CODE_TYPE);
        return defaultAverageEF;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DefaultAverageEF createUpdatedEntity(EntityManager em) {
        DefaultAverageEF defaultAverageEF = new DefaultAverageEF()
            .domain(UPDATED_DOMAIN)
            .detail(UPDATED_DETAIL)
            .countryOrRegion(UPDATED_COUNTRY_OR_REGION)
            .emissionFactor(UPDATED_EMISSION_FACTOR)
            .efSource(UPDATED_EF_SOURCE)
            .code(UPDATED_CODE)
            .codeType(UPDATED_CODE_TYPE);
        return defaultAverageEF;
    }

    @BeforeEach
    public void initTest() {
        defaultAverageEF = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedDefaultAverageEF != null) {
            defaultAverageEFRepository.delete(insertedDefaultAverageEF);
            insertedDefaultAverageEF = null;
        }
    }

    @Test
    @Transactional
    void createDefaultAverageEF() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DefaultAverageEF
        var returnedDefaultAverageEF = om.readValue(
            restDefaultAverageEFMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defaultAverageEF)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DefaultAverageEF.class
        );

        // Validate the DefaultAverageEF in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDefaultAverageEFUpdatableFieldsEquals(returnedDefaultAverageEF, getPersistedDefaultAverageEF(returnedDefaultAverageEF));

        insertedDefaultAverageEF = returnedDefaultAverageEF;
    }

    @Test
    @Transactional
    void createDefaultAverageEFWithExistingId() throws Exception {
        // Create the DefaultAverageEF with an existing ID
        defaultAverageEF.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDefaultAverageEFMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defaultAverageEF)))
            .andExpect(status().isBadRequest());

        // Validate the DefaultAverageEF in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDefaultAverageEFS() throws Exception {
        // Initialize the database
        insertedDefaultAverageEF = defaultAverageEFRepository.saveAndFlush(defaultAverageEF);

        // Get all the defaultAverageEFList
        restDefaultAverageEFMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(defaultAverageEF.getId().intValue())))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN)))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL)))
            .andExpect(jsonPath("$.[*].countryOrRegion").value(hasItem(DEFAULT_COUNTRY_OR_REGION)))
            .andExpect(jsonPath("$.[*].emissionFactor").value(hasItem(sameNumber(DEFAULT_EMISSION_FACTOR))))
            .andExpect(jsonPath("$.[*].efSource").value(hasItem(DEFAULT_EF_SOURCE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].codeType").value(hasItem(DEFAULT_CODE_TYPE)));
    }

    @Test
    @Transactional
    void getDefaultAverageEF() throws Exception {
        // Initialize the database
        insertedDefaultAverageEF = defaultAverageEFRepository.saveAndFlush(defaultAverageEF);

        // Get the defaultAverageEF
        restDefaultAverageEFMockMvc
            .perform(get(ENTITY_API_URL_ID, defaultAverageEF.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(defaultAverageEF.getId().intValue()))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN))
            .andExpect(jsonPath("$.detail").value(DEFAULT_DETAIL))
            .andExpect(jsonPath("$.countryOrRegion").value(DEFAULT_COUNTRY_OR_REGION))
            .andExpect(jsonPath("$.emissionFactor").value(sameNumber(DEFAULT_EMISSION_FACTOR)))
            .andExpect(jsonPath("$.efSource").value(DEFAULT_EF_SOURCE))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.codeType").value(DEFAULT_CODE_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingDefaultAverageEF() throws Exception {
        // Get the defaultAverageEF
        restDefaultAverageEFMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDefaultAverageEF() throws Exception {
        // Initialize the database
        insertedDefaultAverageEF = defaultAverageEFRepository.saveAndFlush(defaultAverageEF);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the defaultAverageEF
        DefaultAverageEF updatedDefaultAverageEF = defaultAverageEFRepository.findById(defaultAverageEF.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDefaultAverageEF are not directly saved in db
        em.detach(updatedDefaultAverageEF);
        updatedDefaultAverageEF
            .domain(UPDATED_DOMAIN)
            .detail(UPDATED_DETAIL)
            .countryOrRegion(UPDATED_COUNTRY_OR_REGION)
            .emissionFactor(UPDATED_EMISSION_FACTOR)
            .efSource(UPDATED_EF_SOURCE)
            .code(UPDATED_CODE)
            .codeType(UPDATED_CODE_TYPE);

        restDefaultAverageEFMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDefaultAverageEF.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDefaultAverageEF))
            )
            .andExpect(status().isOk());

        // Validate the DefaultAverageEF in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDefaultAverageEFToMatchAllProperties(updatedDefaultAverageEF);
    }

    @Test
    @Transactional
    void putNonExistingDefaultAverageEF() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        defaultAverageEF.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDefaultAverageEFMockMvc
            .perform(
                put(ENTITY_API_URL_ID, defaultAverageEF.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(defaultAverageEF))
            )
            .andExpect(status().isBadRequest());

        // Validate the DefaultAverageEF in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDefaultAverageEF() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        defaultAverageEF.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDefaultAverageEFMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(defaultAverageEF))
            )
            .andExpect(status().isBadRequest());

        // Validate the DefaultAverageEF in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDefaultAverageEF() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        defaultAverageEF.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDefaultAverageEFMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defaultAverageEF)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DefaultAverageEF in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDefaultAverageEFWithPatch() throws Exception {
        // Initialize the database
        insertedDefaultAverageEF = defaultAverageEFRepository.saveAndFlush(defaultAverageEF);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the defaultAverageEF using partial update
        DefaultAverageEF partialUpdatedDefaultAverageEF = new DefaultAverageEF();
        partialUpdatedDefaultAverageEF.setId(defaultAverageEF.getId());

        partialUpdatedDefaultAverageEF.emissionFactor(UPDATED_EMISSION_FACTOR);

        restDefaultAverageEFMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDefaultAverageEF.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDefaultAverageEF))
            )
            .andExpect(status().isOk());

        // Validate the DefaultAverageEF in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDefaultAverageEFUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDefaultAverageEF, defaultAverageEF),
            getPersistedDefaultAverageEF(defaultAverageEF)
        );
    }

    @Test
    @Transactional
    void fullUpdateDefaultAverageEFWithPatch() throws Exception {
        // Initialize the database
        insertedDefaultAverageEF = defaultAverageEFRepository.saveAndFlush(defaultAverageEF);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the defaultAverageEF using partial update
        DefaultAverageEF partialUpdatedDefaultAverageEF = new DefaultAverageEF();
        partialUpdatedDefaultAverageEF.setId(defaultAverageEF.getId());

        partialUpdatedDefaultAverageEF
            .domain(UPDATED_DOMAIN)
            .detail(UPDATED_DETAIL)
            .countryOrRegion(UPDATED_COUNTRY_OR_REGION)
            .emissionFactor(UPDATED_EMISSION_FACTOR)
            .efSource(UPDATED_EF_SOURCE)
            .code(UPDATED_CODE)
            .codeType(UPDATED_CODE_TYPE);

        restDefaultAverageEFMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDefaultAverageEF.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDefaultAverageEF))
            )
            .andExpect(status().isOk());

        // Validate the DefaultAverageEF in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDefaultAverageEFUpdatableFieldsEquals(
            partialUpdatedDefaultAverageEF,
            getPersistedDefaultAverageEF(partialUpdatedDefaultAverageEF)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDefaultAverageEF() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        defaultAverageEF.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDefaultAverageEFMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, defaultAverageEF.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(defaultAverageEF))
            )
            .andExpect(status().isBadRequest());

        // Validate the DefaultAverageEF in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDefaultAverageEF() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        defaultAverageEF.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDefaultAverageEFMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(defaultAverageEF))
            )
            .andExpect(status().isBadRequest());

        // Validate the DefaultAverageEF in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDefaultAverageEF() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        defaultAverageEF.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDefaultAverageEFMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(defaultAverageEF)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DefaultAverageEF in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDefaultAverageEF() throws Exception {
        // Initialize the database
        insertedDefaultAverageEF = defaultAverageEFRepository.saveAndFlush(defaultAverageEF);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the defaultAverageEF
        restDefaultAverageEFMockMvc
            .perform(delete(ENTITY_API_URL_ID, defaultAverageEF.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return defaultAverageEFRepository.count();
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

    protected DefaultAverageEF getPersistedDefaultAverageEF(DefaultAverageEF defaultAverageEF) {
        return defaultAverageEFRepository.findById(defaultAverageEF.getId()).orElseThrow();
    }

    protected void assertPersistedDefaultAverageEFToMatchAllProperties(DefaultAverageEF expectedDefaultAverageEF) {
        assertDefaultAverageEFAllPropertiesEquals(expectedDefaultAverageEF, getPersistedDefaultAverageEF(expectedDefaultAverageEF));
    }

    protected void assertPersistedDefaultAverageEFToMatchUpdatableProperties(DefaultAverageEF expectedDefaultAverageEF) {
        assertDefaultAverageEFAllUpdatablePropertiesEquals(
            expectedDefaultAverageEF,
            getPersistedDefaultAverageEF(expectedDefaultAverageEF)
        );
    }
}
