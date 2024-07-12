package com.bacuti.web.rest;

import static com.bacuti.domain.MachineAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.Machine;
import com.bacuti.repository.MachineRepository;
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
 * Integration tests for the {@link MachineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MachineResourceIT {

    private static final String DEFAULT_MACHINE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MACHINE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/machines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMachineMockMvc;

    private Machine machine;

    private Machine insertedMachine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Machine createEntity(EntityManager em) {
        Machine machine = new Machine().machineName(DEFAULT_MACHINE_NAME).description(DEFAULT_DESCRIPTION);
        return machine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Machine createUpdatedEntity(EntityManager em) {
        Machine machine = new Machine().machineName(UPDATED_MACHINE_NAME).description(UPDATED_DESCRIPTION);
        return machine;
    }

    @BeforeEach
    public void initTest() {
        machine = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMachine != null) {
            machineRepository.delete(insertedMachine);
            insertedMachine = null;
        }
    }

    @Test
    @Transactional
    void createMachine() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Machine
        var returnedMachine = om.readValue(
            restMachineMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(machine)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Machine.class
        );

        // Validate the Machine in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMachineUpdatableFieldsEquals(returnedMachine, getPersistedMachine(returnedMachine));

        insertedMachine = returnedMachine;
    }

    @Test
    @Transactional
    void createMachineWithExistingId() throws Exception {
        // Create the Machine with an existing ID
        machine.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMachineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(machine)))
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMachines() throws Exception {
        // Initialize the database
        insertedMachine = machineRepository.saveAndFlush(machine);

        // Get all the machineList
        restMachineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(machine.getId().intValue())))
            .andExpect(jsonPath("$.[*].machineName").value(hasItem(DEFAULT_MACHINE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getMachine() throws Exception {
        // Initialize the database
        insertedMachine = machineRepository.saveAndFlush(machine);

        // Get the machine
        restMachineMockMvc
            .perform(get(ENTITY_API_URL_ID, machine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(machine.getId().intValue()))
            .andExpect(jsonPath("$.machineName").value(DEFAULT_MACHINE_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingMachine() throws Exception {
        // Get the machine
        restMachineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMachine() throws Exception {
        // Initialize the database
        insertedMachine = machineRepository.saveAndFlush(machine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the machine
        Machine updatedMachine = machineRepository.findById(machine.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMachine are not directly saved in db
        em.detach(updatedMachine);
        updatedMachine.machineName(UPDATED_MACHINE_NAME).description(UPDATED_DESCRIPTION);

        restMachineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMachine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMachine))
            )
            .andExpect(status().isOk());

        // Validate the Machine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMachineToMatchAllProperties(updatedMachine);
    }

    @Test
    @Transactional
    void putNonExistingMachine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        machine.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(put(ENTITY_API_URL_ID, machine.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(machine)))
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMachine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        machine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(machine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMachine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        machine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(machine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Machine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMachineWithPatch() throws Exception {
        // Initialize the database
        insertedMachine = machineRepository.saveAndFlush(machine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the machine using partial update
        Machine partialUpdatedMachine = new Machine();
        partialUpdatedMachine.setId(machine.getId());

        restMachineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMachine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMachine))
            )
            .andExpect(status().isOk());

        // Validate the Machine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMachineUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMachine, machine), getPersistedMachine(machine));
    }

    @Test
    @Transactional
    void fullUpdateMachineWithPatch() throws Exception {
        // Initialize the database
        insertedMachine = machineRepository.saveAndFlush(machine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the machine using partial update
        Machine partialUpdatedMachine = new Machine();
        partialUpdatedMachine.setId(machine.getId());

        partialUpdatedMachine.machineName(UPDATED_MACHINE_NAME).description(UPDATED_DESCRIPTION);

        restMachineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMachine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMachine))
            )
            .andExpect(status().isOk());

        // Validate the Machine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMachineUpdatableFieldsEquals(partialUpdatedMachine, getPersistedMachine(partialUpdatedMachine));
    }

    @Test
    @Transactional
    void patchNonExistingMachine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        machine.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, machine.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(machine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMachine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        machine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(machine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMachine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        machine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(machine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Machine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMachine() throws Exception {
        // Initialize the database
        insertedMachine = machineRepository.saveAndFlush(machine);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the machine
        restMachineMockMvc
            .perform(delete(ENTITY_API_URL_ID, machine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return machineRepository.count();
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

    protected Machine getPersistedMachine(Machine machine) {
        return machineRepository.findById(machine.getId()).orElseThrow();
    }

    protected void assertPersistedMachineToMatchAllProperties(Machine expectedMachine) {
        assertMachineAllPropertiesEquals(expectedMachine, getPersistedMachine(expectedMachine));
    }

    protected void assertPersistedMachineToMatchUpdatableProperties(Machine expectedMachine) {
        assertMachineAllUpdatablePropertiesEquals(expectedMachine, getPersistedMachine(expectedMachine));
    }
}
