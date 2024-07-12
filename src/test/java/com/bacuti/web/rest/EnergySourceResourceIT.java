package com.bacuti.web.rest;

import static com.bacuti.domain.EnergySourceAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bacuti.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.EnergySource;
import com.bacuti.domain.enumeration.EfUnits;
import com.bacuti.domain.enumeration.EnergyType;
import com.bacuti.repository.EnergySourceRepository;
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
 * Integration tests for the {@link EnergySourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnergySourceResourceIT {

    private static final EnergyType DEFAULT_ENERGY_TYPE = EnergyType.ELECTRICITY;
    private static final EnergyType UPDATED_ENERGY_TYPE = EnergyType.NATURALGAS;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPLIER = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_CO_2_EMISSION_FACTOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_CO_2_EMISSION_FACTOR = new BigDecimal(2);

    private static final BigDecimal DEFAULT_UPSTREAM_CO_2_EF = new BigDecimal(1);
    private static final BigDecimal UPDATED_UPSTREAM_CO_2_EF = new BigDecimal(2);

    private static final EfUnits DEFAULT_EF_UNITS = EfUnits.TYPE1;
    private static final EfUnits UPDATED_EF_UNITS = EfUnits.TYPE2;

    private static final String DEFAULT_SOURCE_FOR_EF = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_FOR_EF = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PERCENT_RENEWABLE_SRC = new BigDecimal(1);
    private static final BigDecimal UPDATED_PERCENT_RENEWABLE_SRC = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/energy-sources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EnergySourceRepository energySourceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnergySourceMockMvc;

    private EnergySource energySource;

    private EnergySource insertedEnergySource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EnergySource createEntity(EntityManager em) {
        EnergySource energySource = new EnergySource()
            .energyType(DEFAULT_ENERGY_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .supplier(DEFAULT_SUPPLIER)
            .co2EmissionFactor(DEFAULT_CO_2_EMISSION_FACTOR)
            .upstreamCo2EF(DEFAULT_UPSTREAM_CO_2_EF)
            .efUnits(DEFAULT_EF_UNITS)
            .sourceForEf(DEFAULT_SOURCE_FOR_EF)
            .percentRenewableSrc(DEFAULT_PERCENT_RENEWABLE_SRC);
        return energySource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EnergySource createUpdatedEntity(EntityManager em) {
        EnergySource energySource = new EnergySource()
            .energyType(UPDATED_ENERGY_TYPE)
            .description(UPDATED_DESCRIPTION)
            .supplier(UPDATED_SUPPLIER)
            .co2EmissionFactor(UPDATED_CO_2_EMISSION_FACTOR)
            .upstreamCo2EF(UPDATED_UPSTREAM_CO_2_EF)
            .efUnits(UPDATED_EF_UNITS)
            .sourceForEf(UPDATED_SOURCE_FOR_EF)
            .percentRenewableSrc(UPDATED_PERCENT_RENEWABLE_SRC);
        return energySource;
    }

    @BeforeEach
    public void initTest() {
        energySource = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedEnergySource != null) {
            energySourceRepository.delete(insertedEnergySource);
            insertedEnergySource = null;
        }
    }

    @Test
    @Transactional
    void createEnergySource() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EnergySource
        var returnedEnergySource = om.readValue(
            restEnergySourceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(energySource)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EnergySource.class
        );

        // Validate the EnergySource in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEnergySourceUpdatableFieldsEquals(returnedEnergySource, getPersistedEnergySource(returnedEnergySource));

        insertedEnergySource = returnedEnergySource;
    }

    @Test
    @Transactional
    void createEnergySourceWithExistingId() throws Exception {
        // Create the EnergySource with an existing ID
        energySource.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnergySourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(energySource)))
            .andExpect(status().isBadRequest());

        // Validate the EnergySource in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEnergySources() throws Exception {
        // Initialize the database
        insertedEnergySource = energySourceRepository.saveAndFlush(energySource);

        // Get all the energySourceList
        restEnergySourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(energySource.getId().intValue())))
            .andExpect(jsonPath("$.[*].energyType").value(hasItem(DEFAULT_ENERGY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].supplier").value(hasItem(DEFAULT_SUPPLIER)))
            .andExpect(jsonPath("$.[*].co2EmissionFactor").value(hasItem(sameNumber(DEFAULT_CO_2_EMISSION_FACTOR))))
            .andExpect(jsonPath("$.[*].upstreamCo2EF").value(hasItem(sameNumber(DEFAULT_UPSTREAM_CO_2_EF))))
            .andExpect(jsonPath("$.[*].efUnits").value(hasItem(DEFAULT_EF_UNITS.toString())))
            .andExpect(jsonPath("$.[*].sourceForEf").value(hasItem(DEFAULT_SOURCE_FOR_EF)))
            .andExpect(jsonPath("$.[*].percentRenewableSrc").value(hasItem(sameNumber(DEFAULT_PERCENT_RENEWABLE_SRC))));
    }

    @Test
    @Transactional
    void getEnergySource() throws Exception {
        // Initialize the database
        insertedEnergySource = energySourceRepository.saveAndFlush(energySource);

        // Get the energySource
        restEnergySourceMockMvc
            .perform(get(ENTITY_API_URL_ID, energySource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(energySource.getId().intValue()))
            .andExpect(jsonPath("$.energyType").value(DEFAULT_ENERGY_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.supplier").value(DEFAULT_SUPPLIER))
            .andExpect(jsonPath("$.co2EmissionFactor").value(sameNumber(DEFAULT_CO_2_EMISSION_FACTOR)))
            .andExpect(jsonPath("$.upstreamCo2EF").value(sameNumber(DEFAULT_UPSTREAM_CO_2_EF)))
            .andExpect(jsonPath("$.efUnits").value(DEFAULT_EF_UNITS.toString()))
            .andExpect(jsonPath("$.sourceForEf").value(DEFAULT_SOURCE_FOR_EF))
            .andExpect(jsonPath("$.percentRenewableSrc").value(sameNumber(DEFAULT_PERCENT_RENEWABLE_SRC)));
    }

    @Test
    @Transactional
    void getNonExistingEnergySource() throws Exception {
        // Get the energySource
        restEnergySourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEnergySource() throws Exception {
        // Initialize the database
        insertedEnergySource = energySourceRepository.saveAndFlush(energySource);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the energySource
        EnergySource updatedEnergySource = energySourceRepository.findById(energySource.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEnergySource are not directly saved in db
        em.detach(updatedEnergySource);
        updatedEnergySource
            .energyType(UPDATED_ENERGY_TYPE)
            .description(UPDATED_DESCRIPTION)
            .supplier(UPDATED_SUPPLIER)
            .co2EmissionFactor(UPDATED_CO_2_EMISSION_FACTOR)
            .upstreamCo2EF(UPDATED_UPSTREAM_CO_2_EF)
            .efUnits(UPDATED_EF_UNITS)
            .sourceForEf(UPDATED_SOURCE_FOR_EF)
            .percentRenewableSrc(UPDATED_PERCENT_RENEWABLE_SRC);

        restEnergySourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEnergySource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEnergySource))
            )
            .andExpect(status().isOk());

        // Validate the EnergySource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEnergySourceToMatchAllProperties(updatedEnergySource);
    }

    @Test
    @Transactional
    void putNonExistingEnergySource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        energySource.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnergySourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, energySource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(energySource))
            )
            .andExpect(status().isBadRequest());

        // Validate the EnergySource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnergySource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        energySource.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnergySourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(energySource))
            )
            .andExpect(status().isBadRequest());

        // Validate the EnergySource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnergySource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        energySource.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnergySourceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(energySource)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EnergySource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnergySourceWithPatch() throws Exception {
        // Initialize the database
        insertedEnergySource = energySourceRepository.saveAndFlush(energySource);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the energySource using partial update
        EnergySource partialUpdatedEnergySource = new EnergySource();
        partialUpdatedEnergySource.setId(energySource.getId());

        partialUpdatedEnergySource.energyType(UPDATED_ENERGY_TYPE).description(UPDATED_DESCRIPTION).upstreamCo2EF(UPDATED_UPSTREAM_CO_2_EF);

        restEnergySourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnergySource.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnergySource))
            )
            .andExpect(status().isOk());

        // Validate the EnergySource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnergySourceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEnergySource, energySource),
            getPersistedEnergySource(energySource)
        );
    }

    @Test
    @Transactional
    void fullUpdateEnergySourceWithPatch() throws Exception {
        // Initialize the database
        insertedEnergySource = energySourceRepository.saveAndFlush(energySource);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the energySource using partial update
        EnergySource partialUpdatedEnergySource = new EnergySource();
        partialUpdatedEnergySource.setId(energySource.getId());

        partialUpdatedEnergySource
            .energyType(UPDATED_ENERGY_TYPE)
            .description(UPDATED_DESCRIPTION)
            .supplier(UPDATED_SUPPLIER)
            .co2EmissionFactor(UPDATED_CO_2_EMISSION_FACTOR)
            .upstreamCo2EF(UPDATED_UPSTREAM_CO_2_EF)
            .efUnits(UPDATED_EF_UNITS)
            .sourceForEf(UPDATED_SOURCE_FOR_EF)
            .percentRenewableSrc(UPDATED_PERCENT_RENEWABLE_SRC);

        restEnergySourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnergySource.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnergySource))
            )
            .andExpect(status().isOk());

        // Validate the EnergySource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnergySourceUpdatableFieldsEquals(partialUpdatedEnergySource, getPersistedEnergySource(partialUpdatedEnergySource));
    }

    @Test
    @Transactional
    void patchNonExistingEnergySource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        energySource.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnergySourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, energySource.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(energySource))
            )
            .andExpect(status().isBadRequest());

        // Validate the EnergySource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnergySource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        energySource.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnergySourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(energySource))
            )
            .andExpect(status().isBadRequest());

        // Validate the EnergySource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnergySource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        energySource.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnergySourceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(energySource)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EnergySource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnergySource() throws Exception {
        // Initialize the database
        insertedEnergySource = energySourceRepository.saveAndFlush(energySource);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the energySource
        restEnergySourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, energySource.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return energySourceRepository.count();
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

    protected EnergySource getPersistedEnergySource(EnergySource energySource) {
        return energySourceRepository.findById(energySource.getId()).orElseThrow();
    }

    protected void assertPersistedEnergySourceToMatchAllProperties(EnergySource expectedEnergySource) {
        assertEnergySourceAllPropertiesEquals(expectedEnergySource, getPersistedEnergySource(expectedEnergySource));
    }

    protected void assertPersistedEnergySourceToMatchUpdatableProperties(EnergySource expectedEnergySource) {
        assertEnergySourceAllUpdatablePropertiesEquals(expectedEnergySource, getPersistedEnergySource(expectedEnergySource));
    }
}
