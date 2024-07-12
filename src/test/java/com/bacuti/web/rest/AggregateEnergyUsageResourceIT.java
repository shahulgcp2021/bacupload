package com.bacuti.web.rest;

import static com.bacuti.domain.AggregateEnergyUsageAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bacuti.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.AggregateEnergyUsage;
import com.bacuti.repository.AggregateEnergyUsageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
 * Integration tests for the {@link AggregateEnergyUsageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AggregateEnergyUsageResourceIT {

    private static final Instant DEFAULT_DATE = LocalDate.ofEpochDay(0L).atStartOfDay().toInstant(ZoneOffset.UTC);
    private static final Instant UPDATED_DATE = LocalDate.now(ZoneId.systemDefault()).atStartOfDay().toInstant(ZoneOffset.UTC);

    private static final BigDecimal DEFAULT_USAGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_USAGE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/aggregate-energy-usages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AggregateEnergyUsageRepository aggregateEnergyUsageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAggregateEnergyUsageMockMvc;

    private AggregateEnergyUsage aggregateEnergyUsage;

    private AggregateEnergyUsage insertedAggregateEnergyUsage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AggregateEnergyUsage createEntity(EntityManager em) {
        AggregateEnergyUsage aggregateEnergyUsage = new AggregateEnergyUsage().date(DEFAULT_DATE).usage(DEFAULT_USAGE);
        return aggregateEnergyUsage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AggregateEnergyUsage createUpdatedEntity(EntityManager em) {
        AggregateEnergyUsage aggregateEnergyUsage = new AggregateEnergyUsage().date(UPDATED_DATE).usage(UPDATED_USAGE);
        return aggregateEnergyUsage;
    }

    @BeforeEach
    public void initTest() {
        aggregateEnergyUsage = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAggregateEnergyUsage != null) {
            aggregateEnergyUsageRepository.delete(insertedAggregateEnergyUsage);
            insertedAggregateEnergyUsage = null;
        }
    }

    @Test
    @Transactional
    void createAggregateEnergyUsage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AggregateEnergyUsage
        var returnedAggregateEnergyUsage = om.readValue(
            restAggregateEnergyUsageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aggregateEnergyUsage)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AggregateEnergyUsage.class
        );

        // Validate the AggregateEnergyUsage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAggregateEnergyUsageUpdatableFieldsEquals(
            returnedAggregateEnergyUsage,
            getPersistedAggregateEnergyUsage(returnedAggregateEnergyUsage)
        );

        insertedAggregateEnergyUsage = returnedAggregateEnergyUsage;
    }

    @Test
    @Transactional
    void createAggregateEnergyUsageWithExistingId() throws Exception {
        // Create the AggregateEnergyUsage with an existing ID
        aggregateEnergyUsage.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAggregateEnergyUsageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aggregateEnergyUsage)))
            .andExpect(status().isBadRequest());

        // Validate the AggregateEnergyUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAggregateEnergyUsages() throws Exception {
        // Initialize the database
        insertedAggregateEnergyUsage = aggregateEnergyUsageRepository.saveAndFlush(aggregateEnergyUsage);

        // Get all the aggregateEnergyUsageList
        restAggregateEnergyUsageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aggregateEnergyUsage.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].usage").value(hasItem(sameNumber(DEFAULT_USAGE))));
    }

    @Test
    @Transactional
    void getAggregateEnergyUsage() throws Exception {
        // Initialize the database
        insertedAggregateEnergyUsage = aggregateEnergyUsageRepository.saveAndFlush(aggregateEnergyUsage);

        // Get the aggregateEnergyUsage
        restAggregateEnergyUsageMockMvc
            .perform(get(ENTITY_API_URL_ID, aggregateEnergyUsage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aggregateEnergyUsage.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.usage").value(sameNumber(DEFAULT_USAGE)));
    }

    @Test
    @Transactional
    void getNonExistingAggregateEnergyUsage() throws Exception {
        // Get the aggregateEnergyUsage
        restAggregateEnergyUsageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAggregateEnergyUsage() throws Exception {
        // Initialize the database
        insertedAggregateEnergyUsage = aggregateEnergyUsageRepository.saveAndFlush(aggregateEnergyUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aggregateEnergyUsage
        AggregateEnergyUsage updatedAggregateEnergyUsage = aggregateEnergyUsageRepository
            .findById(aggregateEnergyUsage.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAggregateEnergyUsage are not directly saved in db
        em.detach(updatedAggregateEnergyUsage);
        updatedAggregateEnergyUsage.date(UPDATED_DATE).usage(UPDATED_USAGE);

        restAggregateEnergyUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAggregateEnergyUsage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAggregateEnergyUsage))
            )
            .andExpect(status().isOk());

        // Validate the AggregateEnergyUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAggregateEnergyUsageToMatchAllProperties(updatedAggregateEnergyUsage);
    }

    @Test
    @Transactional
    void putNonExistingAggregateEnergyUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aggregateEnergyUsage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAggregateEnergyUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aggregateEnergyUsage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aggregateEnergyUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the AggregateEnergyUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAggregateEnergyUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aggregateEnergyUsage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAggregateEnergyUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aggregateEnergyUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the AggregateEnergyUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAggregateEnergyUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aggregateEnergyUsage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAggregateEnergyUsageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aggregateEnergyUsage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AggregateEnergyUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAggregateEnergyUsageWithPatch() throws Exception {
        // Initialize the database
        insertedAggregateEnergyUsage = aggregateEnergyUsageRepository.saveAndFlush(aggregateEnergyUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aggregateEnergyUsage using partial update
        AggregateEnergyUsage partialUpdatedAggregateEnergyUsage = new AggregateEnergyUsage();
        partialUpdatedAggregateEnergyUsage.setId(aggregateEnergyUsage.getId());

        partialUpdatedAggregateEnergyUsage.date(UPDATED_DATE);

        restAggregateEnergyUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAggregateEnergyUsage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAggregateEnergyUsage))
            )
            .andExpect(status().isOk());

        // Validate the AggregateEnergyUsage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAggregateEnergyUsageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAggregateEnergyUsage, aggregateEnergyUsage),
            getPersistedAggregateEnergyUsage(aggregateEnergyUsage)
        );
    }

    @Test
    @Transactional
    void fullUpdateAggregateEnergyUsageWithPatch() throws Exception {
        // Initialize the database
        insertedAggregateEnergyUsage = aggregateEnergyUsageRepository.saveAndFlush(aggregateEnergyUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aggregateEnergyUsage using partial update
        AggregateEnergyUsage partialUpdatedAggregateEnergyUsage = new AggregateEnergyUsage();
        partialUpdatedAggregateEnergyUsage.setId(aggregateEnergyUsage.getId());

        partialUpdatedAggregateEnergyUsage.date(UPDATED_DATE).usage(UPDATED_USAGE);

        restAggregateEnergyUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAggregateEnergyUsage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAggregateEnergyUsage))
            )
            .andExpect(status().isOk());

        // Validate the AggregateEnergyUsage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAggregateEnergyUsageUpdatableFieldsEquals(
            partialUpdatedAggregateEnergyUsage,
            getPersistedAggregateEnergyUsage(partialUpdatedAggregateEnergyUsage)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAggregateEnergyUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aggregateEnergyUsage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAggregateEnergyUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aggregateEnergyUsage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aggregateEnergyUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the AggregateEnergyUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAggregateEnergyUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aggregateEnergyUsage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAggregateEnergyUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aggregateEnergyUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the AggregateEnergyUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAggregateEnergyUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aggregateEnergyUsage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAggregateEnergyUsageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aggregateEnergyUsage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AggregateEnergyUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAggregateEnergyUsage() throws Exception {
        // Initialize the database
        insertedAggregateEnergyUsage = aggregateEnergyUsageRepository.saveAndFlush(aggregateEnergyUsage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the aggregateEnergyUsage
        restAggregateEnergyUsageMockMvc
            .perform(delete(ENTITY_API_URL_ID, aggregateEnergyUsage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return aggregateEnergyUsageRepository.count();
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

    protected AggregateEnergyUsage getPersistedAggregateEnergyUsage(AggregateEnergyUsage aggregateEnergyUsage) {
        return aggregateEnergyUsageRepository.findById(aggregateEnergyUsage.getId()).orElseThrow();
    }

    protected void assertPersistedAggregateEnergyUsageToMatchAllProperties(AggregateEnergyUsage expectedAggregateEnergyUsage) {
        assertAggregateEnergyUsageAllPropertiesEquals(
            expectedAggregateEnergyUsage,
            getPersistedAggregateEnergyUsage(expectedAggregateEnergyUsage)
        );
    }

    protected void assertPersistedAggregateEnergyUsageToMatchUpdatableProperties(AggregateEnergyUsage expectedAggregateEnergyUsage) {
        assertAggregateEnergyUsageAllUpdatablePropertiesEquals(
            expectedAggregateEnergyUsage,
            getPersistedAggregateEnergyUsage(expectedAggregateEnergyUsage)
        );
    }
}
