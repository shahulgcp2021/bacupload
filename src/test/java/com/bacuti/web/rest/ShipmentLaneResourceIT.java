package com.bacuti.web.rest;

import static com.bacuti.domain.ShipmentLaneAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.ShipmentLane;
import com.bacuti.repository.ShipmentLaneRepository;
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
 * Integration tests for the {@link ShipmentLaneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShipmentLaneResourceIT {

    private static final String DEFAULT_LANE = "AAAAAAAAAA";
    private static final String UPDATED_LANE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shipment-lanes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShipmentLaneRepository shipmentLaneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentLaneMockMvc;

    private ShipmentLane shipmentLane;

    private ShipmentLane insertedShipmentLane;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentLane createEntity(EntityManager em) {
        ShipmentLane shipmentLane = new ShipmentLane().lane(DEFAULT_LANE).description(DEFAULT_DESCRIPTION);
        return shipmentLane;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentLane createUpdatedEntity(EntityManager em) {
        ShipmentLane shipmentLane = new ShipmentLane().lane(UPDATED_LANE).description(UPDATED_DESCRIPTION);
        return shipmentLane;
    }

    @BeforeEach
    public void initTest() {
        shipmentLane = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedShipmentLane != null) {
            shipmentLaneRepository.delete(insertedShipmentLane);
            insertedShipmentLane = null;
        }
    }

    @Test
    @Transactional
    void createShipmentLane() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShipmentLane
        var returnedShipmentLane = om.readValue(
            restShipmentLaneMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentLane)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShipmentLane.class
        );

        // Validate the ShipmentLane in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShipmentLaneUpdatableFieldsEquals(returnedShipmentLane, getPersistedShipmentLane(returnedShipmentLane));

        insertedShipmentLane = returnedShipmentLane;
    }

    @Test
    @Transactional
    void createShipmentLaneWithExistingId() throws Exception {
        // Create the ShipmentLane with an existing ID
        shipmentLane.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentLaneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentLane)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentLane in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllShipmentLanes() throws Exception {
        // Initialize the database
        insertedShipmentLane = shipmentLaneRepository.saveAndFlush(shipmentLane);

        // Get all the shipmentLaneList
        restShipmentLaneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentLane.getId().intValue())))
            .andExpect(jsonPath("$.[*].lane").value(hasItem(DEFAULT_LANE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getShipmentLane() throws Exception {
        // Initialize the database
        insertedShipmentLane = shipmentLaneRepository.saveAndFlush(shipmentLane);

        // Get the shipmentLane
        restShipmentLaneMockMvc
            .perform(get(ENTITY_API_URL_ID, shipmentLane.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentLane.getId().intValue()))
            .andExpect(jsonPath("$.lane").value(DEFAULT_LANE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingShipmentLane() throws Exception {
        // Get the shipmentLane
        restShipmentLaneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipmentLane() throws Exception {
        // Initialize the database
        insertedShipmentLane = shipmentLaneRepository.saveAndFlush(shipmentLane);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentLane
        ShipmentLane updatedShipmentLane = shipmentLaneRepository.findById(shipmentLane.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipmentLane are not directly saved in db
        em.detach(updatedShipmentLane);
        updatedShipmentLane.lane(UPDATED_LANE).description(UPDATED_DESCRIPTION);

        restShipmentLaneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShipmentLane.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShipmentLane))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentLane in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShipmentLaneToMatchAllProperties(updatedShipmentLane);
    }

    @Test
    @Transactional
    void putNonExistingShipmentLane() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentLane.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentLaneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentLane.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentLane))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentLane in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipmentLane() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentLane.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentLaneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentLane))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentLane in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipmentLane() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentLane.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentLaneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentLane)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentLane in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentLaneWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentLane = shipmentLaneRepository.saveAndFlush(shipmentLane);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentLane using partial update
        ShipmentLane partialUpdatedShipmentLane = new ShipmentLane();
        partialUpdatedShipmentLane.setId(shipmentLane.getId());

        partialUpdatedShipmentLane.description(UPDATED_DESCRIPTION);

        restShipmentLaneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentLane.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentLane))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentLane in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentLaneUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShipmentLane, shipmentLane),
            getPersistedShipmentLane(shipmentLane)
        );
    }

    @Test
    @Transactional
    void fullUpdateShipmentLaneWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentLane = shipmentLaneRepository.saveAndFlush(shipmentLane);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentLane using partial update
        ShipmentLane partialUpdatedShipmentLane = new ShipmentLane();
        partialUpdatedShipmentLane.setId(shipmentLane.getId());

        partialUpdatedShipmentLane.lane(UPDATED_LANE).description(UPDATED_DESCRIPTION);

        restShipmentLaneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentLane.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentLane))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentLane in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentLaneUpdatableFieldsEquals(partialUpdatedShipmentLane, getPersistedShipmentLane(partialUpdatedShipmentLane));
    }

    @Test
    @Transactional
    void patchNonExistingShipmentLane() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentLane.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentLaneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentLane.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentLane))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentLane in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipmentLane() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentLane.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentLaneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentLane))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentLane in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipmentLane() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentLane.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentLaneMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shipmentLane)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentLane in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipmentLane() throws Exception {
        // Initialize the database
        insertedShipmentLane = shipmentLaneRepository.saveAndFlush(shipmentLane);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipmentLane
        restShipmentLaneMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipmentLane.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shipmentLaneRepository.count();
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

    protected ShipmentLane getPersistedShipmentLane(ShipmentLane shipmentLane) {
        return shipmentLaneRepository.findById(shipmentLane.getId()).orElseThrow();
    }

    protected void assertPersistedShipmentLaneToMatchAllProperties(ShipmentLane expectedShipmentLane) {
        assertShipmentLaneAllPropertiesEquals(expectedShipmentLane, getPersistedShipmentLane(expectedShipmentLane));
    }

    protected void assertPersistedShipmentLaneToMatchUpdatableProperties(ShipmentLane expectedShipmentLane) {
        assertShipmentLaneAllUpdatablePropertiesEquals(expectedShipmentLane, getPersistedShipmentLane(expectedShipmentLane));
    }
}
