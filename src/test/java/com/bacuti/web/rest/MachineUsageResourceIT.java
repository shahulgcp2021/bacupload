package com.bacuti.web.rest;

import static com.bacuti.domain.MachineUsageAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bacuti.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.MachineUsage;
import com.bacuti.repository.MachineUsageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link MachineUsageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MachineUsageResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_USAGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_USAGE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/machine-usages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MachineUsageRepository machineUsageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMachineUsageMockMvc;

    private MachineUsage machineUsage;

    private MachineUsage insertedMachineUsage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MachineUsage createEntity(EntityManager em) {
        MachineUsage machineUsage = new MachineUsage().date(DEFAULT_DATE).usage(DEFAULT_USAGE);
        return machineUsage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MachineUsage createUpdatedEntity(EntityManager em) {
        MachineUsage machineUsage = new MachineUsage().date(UPDATED_DATE).usage(UPDATED_USAGE);
        return machineUsage;
    }

    @BeforeEach
    public void initTest() {
        machineUsage = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMachineUsage != null) {
            machineUsageRepository.delete(insertedMachineUsage);
            insertedMachineUsage = null;
        }
    }

    @Test
    @Transactional
    void createMachineUsage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MachineUsage
        var returnedMachineUsage = om.readValue(
            restMachineUsageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(machineUsage)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MachineUsage.class
        );

        // Validate the MachineUsage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMachineUsageUpdatableFieldsEquals(returnedMachineUsage, getPersistedMachineUsage(returnedMachineUsage));

        insertedMachineUsage = returnedMachineUsage;
    }

    @Test
    @Transactional
    void createMachineUsageWithExistingId() throws Exception {
        // Create the MachineUsage with an existing ID
        machineUsage.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMachineUsageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(machineUsage)))
            .andExpect(status().isBadRequest());

        // Validate the MachineUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMachineUsages() throws Exception {
        // Initialize the database
        insertedMachineUsage = machineUsageRepository.saveAndFlush(machineUsage);

        // Get all the machineUsageList
        restMachineUsageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(machineUsage.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].usage").value(hasItem(sameNumber(DEFAULT_USAGE))));
    }

    @Test
    @Transactional
    void getMachineUsage() throws Exception {
        // Initialize the database
        insertedMachineUsage = machineUsageRepository.saveAndFlush(machineUsage);

        // Get the machineUsage
        restMachineUsageMockMvc
            .perform(get(ENTITY_API_URL_ID, machineUsage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(machineUsage.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.usage").value(sameNumber(DEFAULT_USAGE)));
    }

    @Test
    @Transactional
    void getNonExistingMachineUsage() throws Exception {
        // Get the machineUsage
        restMachineUsageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMachineUsage() throws Exception {
        // Initialize the database
        insertedMachineUsage = machineUsageRepository.saveAndFlush(machineUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the machineUsage
        MachineUsage updatedMachineUsage = machineUsageRepository.findById(machineUsage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMachineUsage are not directly saved in db
        em.detach(updatedMachineUsage);
        updatedMachineUsage.date(UPDATED_DATE).usage(UPDATED_USAGE);

        restMachineUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMachineUsage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMachineUsage))
            )
            .andExpect(status().isOk());

        // Validate the MachineUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMachineUsageToMatchAllProperties(updatedMachineUsage);
    }

    @Test
    @Transactional
    void putNonExistingMachineUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        machineUsage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMachineUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, machineUsage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(machineUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the MachineUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMachineUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        machineUsage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(machineUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the MachineUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMachineUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        machineUsage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineUsageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(machineUsage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MachineUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMachineUsageWithPatch() throws Exception {
        // Initialize the database
        insertedMachineUsage = machineUsageRepository.saveAndFlush(machineUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the machineUsage using partial update
        MachineUsage partialUpdatedMachineUsage = new MachineUsage();
        partialUpdatedMachineUsage.setId(machineUsage.getId());

        partialUpdatedMachineUsage.date(UPDATED_DATE).usage(UPDATED_USAGE);

        restMachineUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMachineUsage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMachineUsage))
            )
            .andExpect(status().isOk());

        // Validate the MachineUsage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMachineUsageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMachineUsage, machineUsage),
            getPersistedMachineUsage(machineUsage)
        );
    }

    @Test
    @Transactional
    void fullUpdateMachineUsageWithPatch() throws Exception {
        // Initialize the database
        insertedMachineUsage = machineUsageRepository.saveAndFlush(machineUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the machineUsage using partial update
        MachineUsage partialUpdatedMachineUsage = new MachineUsage();
        partialUpdatedMachineUsage.setId(machineUsage.getId());

        partialUpdatedMachineUsage.date(UPDATED_DATE).usage(UPDATED_USAGE);

        restMachineUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMachineUsage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMachineUsage))
            )
            .andExpect(status().isOk());

        // Validate the MachineUsage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMachineUsageUpdatableFieldsEquals(partialUpdatedMachineUsage, getPersistedMachineUsage(partialUpdatedMachineUsage));
    }

    @Test
    @Transactional
    void patchNonExistingMachineUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        machineUsage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMachineUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, machineUsage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(machineUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the MachineUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMachineUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        machineUsage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(machineUsage))
            )
            .andExpect(status().isBadRequest());

        // Validate the MachineUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMachineUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        machineUsage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineUsageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(machineUsage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MachineUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMachineUsage() throws Exception {
        // Initialize the database
        insertedMachineUsage = machineUsageRepository.saveAndFlush(machineUsage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the machineUsage
        restMachineUsageMockMvc
            .perform(delete(ENTITY_API_URL_ID, machineUsage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return machineUsageRepository.count();
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

    protected MachineUsage getPersistedMachineUsage(MachineUsage machineUsage) {
        return machineUsageRepository.findById(machineUsage.getId()).orElseThrow();
    }

    protected void assertPersistedMachineUsageToMatchAllProperties(MachineUsage expectedMachineUsage) {
        assertMachineUsageAllPropertiesEquals(expectedMachineUsage, getPersistedMachineUsage(expectedMachineUsage));
    }

    protected void assertPersistedMachineUsageToMatchUpdatableProperties(MachineUsage expectedMachineUsage) {
        assertMachineUsageAllUpdatablePropertiesEquals(expectedMachineUsage, getPersistedMachineUsage(expectedMachineUsage));
    }
}
