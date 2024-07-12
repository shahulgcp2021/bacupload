package com.bacuti.web.rest;

import static com.bacuti.domain.UnitOfMeasureAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.UnitOfMeasure;
import com.bacuti.repository.UnitOfMeasureRepository;
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
 * Integration tests for the {@link UnitOfMeasureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UnitOfMeasureResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/unit-of-measures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUnitOfMeasureMockMvc;

    private UnitOfMeasure unitOfMeasure;

    private UnitOfMeasure insertedUnitOfMeasure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitOfMeasure createEntity(EntityManager em) {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure().name(DEFAULT_NAME).key(DEFAULT_KEY).value(DEFAULT_VALUE);
        return unitOfMeasure;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitOfMeasure createUpdatedEntity(EntityManager em) {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure().name(UPDATED_NAME).key(UPDATED_KEY).value(UPDATED_VALUE);
        return unitOfMeasure;
    }

    @BeforeEach
    public void initTest() {
        unitOfMeasure = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUnitOfMeasure != null) {
            unitOfMeasureRepository.delete(insertedUnitOfMeasure);
            insertedUnitOfMeasure = null;
        }
    }

    @Test
    @Transactional
    void createUnitOfMeasure() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UnitOfMeasure
        var returnedUnitOfMeasure = om.readValue(
            restUnitOfMeasureMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(unitOfMeasure)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UnitOfMeasure.class
        );

        // Validate the UnitOfMeasure in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUnitOfMeasureUpdatableFieldsEquals(returnedUnitOfMeasure, getPersistedUnitOfMeasure(returnedUnitOfMeasure));

        insertedUnitOfMeasure = returnedUnitOfMeasure;
    }

    @Test
    @Transactional
    void createUnitOfMeasureWithExistingId() throws Exception {
        // Create the UnitOfMeasure with an existing ID
        unitOfMeasure.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnitOfMeasureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(unitOfMeasure)))
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasure in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUnitOfMeasures() throws Exception {
        // Initialize the database
        insertedUnitOfMeasure = unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList
        restUnitOfMeasureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unitOfMeasure.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getUnitOfMeasure() throws Exception {
        // Initialize the database
        insertedUnitOfMeasure = unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get the unitOfMeasure
        restUnitOfMeasureMockMvc
            .perform(get(ENTITY_API_URL_ID, unitOfMeasure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(unitOfMeasure.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingUnitOfMeasure() throws Exception {
        // Get the unitOfMeasure
        restUnitOfMeasureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUnitOfMeasure() throws Exception {
        // Initialize the database
        insertedUnitOfMeasure = unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the unitOfMeasure
        UnitOfMeasure updatedUnitOfMeasure = unitOfMeasureRepository.findById(unitOfMeasure.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUnitOfMeasure are not directly saved in db
        em.detach(updatedUnitOfMeasure);
        updatedUnitOfMeasure.name(UPDATED_NAME).key(UPDATED_KEY).value(UPDATED_VALUE);

        restUnitOfMeasureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUnitOfMeasure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUnitOfMeasure))
            )
            .andExpect(status().isOk());

        // Validate the UnitOfMeasure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUnitOfMeasureToMatchAllProperties(updatedUnitOfMeasure);
    }

    @Test
    @Transactional
    void putNonExistingUnitOfMeasure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        unitOfMeasure.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, unitOfMeasure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(unitOfMeasure))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUnitOfMeasure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        unitOfMeasure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(unitOfMeasure))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUnitOfMeasure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        unitOfMeasure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(unitOfMeasure)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitOfMeasure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUnitOfMeasureWithPatch() throws Exception {
        // Initialize the database
        insertedUnitOfMeasure = unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the unitOfMeasure using partial update
        UnitOfMeasure partialUpdatedUnitOfMeasure = new UnitOfMeasure();
        partialUpdatedUnitOfMeasure.setId(unitOfMeasure.getId());

        partialUpdatedUnitOfMeasure.name(UPDATED_NAME).value(UPDATED_VALUE);

        restUnitOfMeasureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitOfMeasure.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUnitOfMeasure))
            )
            .andExpect(status().isOk());

        // Validate the UnitOfMeasure in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUnitOfMeasureUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUnitOfMeasure, unitOfMeasure),
            getPersistedUnitOfMeasure(unitOfMeasure)
        );
    }

    @Test
    @Transactional
    void fullUpdateUnitOfMeasureWithPatch() throws Exception {
        // Initialize the database
        insertedUnitOfMeasure = unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the unitOfMeasure using partial update
        UnitOfMeasure partialUpdatedUnitOfMeasure = new UnitOfMeasure();
        partialUpdatedUnitOfMeasure.setId(unitOfMeasure.getId());

        partialUpdatedUnitOfMeasure.name(UPDATED_NAME).key(UPDATED_KEY).value(UPDATED_VALUE);

        restUnitOfMeasureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitOfMeasure.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUnitOfMeasure))
            )
            .andExpect(status().isOk());

        // Validate the UnitOfMeasure in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUnitOfMeasureUpdatableFieldsEquals(partialUpdatedUnitOfMeasure, getPersistedUnitOfMeasure(partialUpdatedUnitOfMeasure));
    }

    @Test
    @Transactional
    void patchNonExistingUnitOfMeasure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        unitOfMeasure.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, unitOfMeasure.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(unitOfMeasure))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUnitOfMeasure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        unitOfMeasure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(unitOfMeasure))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUnitOfMeasure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        unitOfMeasure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(unitOfMeasure)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitOfMeasure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUnitOfMeasure() throws Exception {
        // Initialize the database
        insertedUnitOfMeasure = unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the unitOfMeasure
        restUnitOfMeasureMockMvc
            .perform(delete(ENTITY_API_URL_ID, unitOfMeasure.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return unitOfMeasureRepository.count();
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

    protected UnitOfMeasure getPersistedUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        return unitOfMeasureRepository.findById(unitOfMeasure.getId()).orElseThrow();
    }

    protected void assertPersistedUnitOfMeasureToMatchAllProperties(UnitOfMeasure expectedUnitOfMeasure) {
        assertUnitOfMeasureAllPropertiesEquals(expectedUnitOfMeasure, getPersistedUnitOfMeasure(expectedUnitOfMeasure));
    }

    protected void assertPersistedUnitOfMeasureToMatchUpdatableProperties(UnitOfMeasure expectedUnitOfMeasure) {
        assertUnitOfMeasureAllUpdatablePropertiesEquals(expectedUnitOfMeasure, getPersistedUnitOfMeasure(expectedUnitOfMeasure));
    }
}
