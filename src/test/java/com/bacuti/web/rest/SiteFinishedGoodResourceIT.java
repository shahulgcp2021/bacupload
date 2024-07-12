package com.bacuti.web.rest;

import static com.bacuti.domain.SiteFinishedGoodAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.SiteFinishedGood;
import com.bacuti.repository.SiteFinishedGoodRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link SiteFinishedGoodResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SiteFinishedGoodResourceIT {

    private static final String ENTITY_API_URL = "/api/site-finished-goods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SiteFinishedGoodRepository siteFinishedGoodRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSiteFinishedGoodMockMvc;

    private SiteFinishedGood siteFinishedGood;

    private SiteFinishedGood insertedSiteFinishedGood;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SiteFinishedGood createEntity(EntityManager em) {
        SiteFinishedGood siteFinishedGood = new SiteFinishedGood();
        return siteFinishedGood;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SiteFinishedGood createUpdatedEntity(EntityManager em) {
        SiteFinishedGood siteFinishedGood = new SiteFinishedGood();
        return siteFinishedGood;
    }

    @BeforeEach
    public void initTest() {
        siteFinishedGood = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSiteFinishedGood != null) {
            siteFinishedGoodRepository.delete(insertedSiteFinishedGood);
            insertedSiteFinishedGood = null;
        }
    }

    @Test
    @Transactional
    void createSiteFinishedGood() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SiteFinishedGood
        var returnedSiteFinishedGood = om.readValue(
            restSiteFinishedGoodMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteFinishedGood)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SiteFinishedGood.class
        );

        // Validate the SiteFinishedGood in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSiteFinishedGoodUpdatableFieldsEquals(returnedSiteFinishedGood, getPersistedSiteFinishedGood(returnedSiteFinishedGood));

        insertedSiteFinishedGood = returnedSiteFinishedGood;
    }

    @Test
    @Transactional
    void createSiteFinishedGoodWithExistingId() throws Exception {
        // Create the SiteFinishedGood with an existing ID
        siteFinishedGood.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSiteFinishedGoodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteFinishedGood)))
            .andExpect(status().isBadRequest());

        // Validate the SiteFinishedGood in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSiteFinishedGoods() throws Exception {
        // Initialize the database
        insertedSiteFinishedGood = siteFinishedGoodRepository.saveAndFlush(siteFinishedGood);

        // Get all the siteFinishedGoodList
        restSiteFinishedGoodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(siteFinishedGood.getId().intValue())));
    }

    @Test
    @Transactional
    void getSiteFinishedGood() throws Exception {
        // Initialize the database
        insertedSiteFinishedGood = siteFinishedGoodRepository.saveAndFlush(siteFinishedGood);

        // Get the siteFinishedGood
        restSiteFinishedGoodMockMvc
            .perform(get(ENTITY_API_URL_ID, siteFinishedGood.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(siteFinishedGood.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSiteFinishedGood() throws Exception {
        // Get the siteFinishedGood
        restSiteFinishedGoodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSiteFinishedGood() throws Exception {
        // Initialize the database
        insertedSiteFinishedGood = siteFinishedGoodRepository.saveAndFlush(siteFinishedGood);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the siteFinishedGood
        SiteFinishedGood updatedSiteFinishedGood = siteFinishedGoodRepository.findById(siteFinishedGood.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSiteFinishedGood are not directly saved in db
        em.detach(updatedSiteFinishedGood);

        restSiteFinishedGoodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSiteFinishedGood.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSiteFinishedGood))
            )
            .andExpect(status().isOk());

        // Validate the SiteFinishedGood in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSiteFinishedGoodToMatchAllProperties(updatedSiteFinishedGood);
    }

    @Test
    @Transactional
    void putNonExistingSiteFinishedGood() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        siteFinishedGood.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteFinishedGoodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, siteFinishedGood.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(siteFinishedGood))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteFinishedGood in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSiteFinishedGood() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        siteFinishedGood.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteFinishedGoodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(siteFinishedGood))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteFinishedGood in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSiteFinishedGood() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        siteFinishedGood.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteFinishedGoodMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteFinishedGood)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SiteFinishedGood in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSiteFinishedGoodWithPatch() throws Exception {
        // Initialize the database
        insertedSiteFinishedGood = siteFinishedGoodRepository.saveAndFlush(siteFinishedGood);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the siteFinishedGood using partial update
        SiteFinishedGood partialUpdatedSiteFinishedGood = new SiteFinishedGood();
        partialUpdatedSiteFinishedGood.setId(siteFinishedGood.getId());

        restSiteFinishedGoodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSiteFinishedGood.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSiteFinishedGood))
            )
            .andExpect(status().isOk());

        // Validate the SiteFinishedGood in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSiteFinishedGoodUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSiteFinishedGood, siteFinishedGood),
            getPersistedSiteFinishedGood(siteFinishedGood)
        );
    }

    @Test
    @Transactional
    void fullUpdateSiteFinishedGoodWithPatch() throws Exception {
        // Initialize the database
        insertedSiteFinishedGood = siteFinishedGoodRepository.saveAndFlush(siteFinishedGood);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the siteFinishedGood using partial update
        SiteFinishedGood partialUpdatedSiteFinishedGood = new SiteFinishedGood();
        partialUpdatedSiteFinishedGood.setId(siteFinishedGood.getId());

        restSiteFinishedGoodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSiteFinishedGood.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSiteFinishedGood))
            )
            .andExpect(status().isOk());

        // Validate the SiteFinishedGood in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSiteFinishedGoodUpdatableFieldsEquals(
            partialUpdatedSiteFinishedGood,
            getPersistedSiteFinishedGood(partialUpdatedSiteFinishedGood)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSiteFinishedGood() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        siteFinishedGood.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteFinishedGoodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, siteFinishedGood.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(siteFinishedGood))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteFinishedGood in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSiteFinishedGood() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        siteFinishedGood.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteFinishedGoodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(siteFinishedGood))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteFinishedGood in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSiteFinishedGood() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        siteFinishedGood.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteFinishedGoodMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(siteFinishedGood)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SiteFinishedGood in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSiteFinishedGood() throws Exception {
        // Initialize the database
        insertedSiteFinishedGood = siteFinishedGoodRepository.saveAndFlush(siteFinishedGood);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the siteFinishedGood
        restSiteFinishedGoodMockMvc
            .perform(delete(ENTITY_API_URL_ID, siteFinishedGood.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return siteFinishedGoodRepository.count();
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

    protected SiteFinishedGood getPersistedSiteFinishedGood(SiteFinishedGood siteFinishedGood) {
        return siteFinishedGoodRepository.findById(siteFinishedGood.getId()).orElseThrow();
    }

    protected void assertPersistedSiteFinishedGoodToMatchAllProperties(SiteFinishedGood expectedSiteFinishedGood) {
        assertSiteFinishedGoodAllPropertiesEquals(expectedSiteFinishedGood, getPersistedSiteFinishedGood(expectedSiteFinishedGood));
    }

    protected void assertPersistedSiteFinishedGoodToMatchUpdatableProperties(SiteFinishedGood expectedSiteFinishedGood) {
        assertSiteFinishedGoodAllUpdatablePropertiesEquals(
            expectedSiteFinishedGood,
            getPersistedSiteFinishedGood(expectedSiteFinishedGood)
        );
    }
}
