package com.bacuti.web.rest;

import static com.bacuti.domain.ShipmentLegAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bacuti.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.ShipmentLeg;
import com.bacuti.domain.enumeration.Mode;
import com.bacuti.repository.ShipmentLegRepository;
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
 * Integration tests for the {@link ShipmentLegResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShipmentLegResourceIT {

    private static final Long DEFAULT_SEGMENT = 1L;
    private static final Long UPDATED_SEGMENT = 2L;

    private static final String DEFAULT_CARRIER = "AAAAAAAAAA";
    private static final String UPDATED_CARRIER = "BBBBBBBBBB";

    private static final Mode DEFAULT_MODE = Mode.MODE1;
    private static final Mode UPDATED_MODE = Mode.MODE2;

    private static final BigDecimal DEFAULT_DISTANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISTANCE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_FROM_COORDINATE_LAT = new BigDecimal(1);
    private static final BigDecimal UPDATED_FROM_COORDINATE_LAT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_FROM_COORDINATE_LONG = new BigDecimal(1);
    private static final BigDecimal UPDATED_FROM_COORDINATE_LONG = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TO_COORDINATE_LAT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TO_COORDINATE_LAT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TO_COORDINATE_LONG = new BigDecimal(1);
    private static final BigDecimal UPDATED_TO_COORDINATE_LONG = new BigDecimal(2);

    private static final String DEFAULT_FROM_IATA = "AAAAAAAAAA";
    private static final String UPDATED_FROM_IATA = "BBBBBBBBBB";

    private static final String DEFAULT_TO_IATA = "AAAAAAAAAA";
    private static final String UPDATED_TO_IATA = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_EMISSIONS_FACTOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_EMISSIONS_FACTOR = new BigDecimal(2);

    private static final String DEFAULT_EF_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_EF_SOURCE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shipment-legs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShipmentLegRepository shipmentLegRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentLegMockMvc;

    private ShipmentLeg shipmentLeg;

    private ShipmentLeg insertedShipmentLeg;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentLeg createEntity(EntityManager em) {
        ShipmentLeg shipmentLeg = new ShipmentLeg()
            .segment(DEFAULT_SEGMENT)
            .carrier(DEFAULT_CARRIER)
            .mode(DEFAULT_MODE)
            .distance(DEFAULT_DISTANCE)
            .fromCoordinateLat(DEFAULT_FROM_COORDINATE_LAT)
            .fromCoordinateLong(DEFAULT_FROM_COORDINATE_LONG)
            .toCoordinateLat(DEFAULT_TO_COORDINATE_LAT)
            .toCoordinateLong(DEFAULT_TO_COORDINATE_LONG)
            .fromIata(DEFAULT_FROM_IATA)
            .toIata(DEFAULT_TO_IATA)
            .emissionsFactor(DEFAULT_EMISSIONS_FACTOR)
            .efSource(DEFAULT_EF_SOURCE);
        return shipmentLeg;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentLeg createUpdatedEntity(EntityManager em) {
        ShipmentLeg shipmentLeg = new ShipmentLeg()
            .segment(UPDATED_SEGMENT)
            .carrier(UPDATED_CARRIER)
            .mode(UPDATED_MODE)
            .distance(UPDATED_DISTANCE)
            .fromCoordinateLat(UPDATED_FROM_COORDINATE_LAT)
            .fromCoordinateLong(UPDATED_FROM_COORDINATE_LONG)
            .toCoordinateLat(UPDATED_TO_COORDINATE_LAT)
            .toCoordinateLong(UPDATED_TO_COORDINATE_LONG)
            .fromIata(UPDATED_FROM_IATA)
            .toIata(UPDATED_TO_IATA)
            .emissionsFactor(UPDATED_EMISSIONS_FACTOR)
            .efSource(UPDATED_EF_SOURCE);
        return shipmentLeg;
    }

    @BeforeEach
    public void initTest() {
        shipmentLeg = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedShipmentLeg != null) {
            shipmentLegRepository.delete(insertedShipmentLeg);
            insertedShipmentLeg = null;
        }
    }

    @Test
    @Transactional
    void createShipmentLeg() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShipmentLeg
        var returnedShipmentLeg = om.readValue(
            restShipmentLegMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentLeg)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShipmentLeg.class
        );

        // Validate the ShipmentLeg in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShipmentLegUpdatableFieldsEquals(returnedShipmentLeg, getPersistedShipmentLeg(returnedShipmentLeg));

        insertedShipmentLeg = returnedShipmentLeg;
    }

    @Test
    @Transactional
    void createShipmentLegWithExistingId() throws Exception {
        // Create the ShipmentLeg with an existing ID
        shipmentLeg.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentLegMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentLeg)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentLeg in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllShipmentLegs() throws Exception {
        // Initialize the database
        insertedShipmentLeg = shipmentLegRepository.saveAndFlush(shipmentLeg);

        // Get all the shipmentLegList
        restShipmentLegMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentLeg.getId().intValue())))
            .andExpect(jsonPath("$.[*].segment").value(hasItem(DEFAULT_SEGMENT.intValue())))
            .andExpect(jsonPath("$.[*].carrier").value(hasItem(DEFAULT_CARRIER)))
            .andExpect(jsonPath("$.[*].mode").value(hasItem(DEFAULT_MODE.toString())))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(sameNumber(DEFAULT_DISTANCE))))
            .andExpect(jsonPath("$.[*].fromCoordinateLat").value(hasItem(sameNumber(DEFAULT_FROM_COORDINATE_LAT))))
            .andExpect(jsonPath("$.[*].fromCoordinateLong").value(hasItem(sameNumber(DEFAULT_FROM_COORDINATE_LONG))))
            .andExpect(jsonPath("$.[*].toCoordinateLat").value(hasItem(sameNumber(DEFAULT_TO_COORDINATE_LAT))))
            .andExpect(jsonPath("$.[*].toCoordinateLong").value(hasItem(sameNumber(DEFAULT_TO_COORDINATE_LONG))))
            .andExpect(jsonPath("$.[*].fromIata").value(hasItem(DEFAULT_FROM_IATA)))
            .andExpect(jsonPath("$.[*].toIata").value(hasItem(DEFAULT_TO_IATA)))
            .andExpect(jsonPath("$.[*].emissionsFactor").value(hasItem(sameNumber(DEFAULT_EMISSIONS_FACTOR))))
            .andExpect(jsonPath("$.[*].efSource").value(hasItem(DEFAULT_EF_SOURCE)));
    }

    @Test
    @Transactional
    void getShipmentLeg() throws Exception {
        // Initialize the database
        insertedShipmentLeg = shipmentLegRepository.saveAndFlush(shipmentLeg);

        // Get the shipmentLeg
        restShipmentLegMockMvc
            .perform(get(ENTITY_API_URL_ID, shipmentLeg.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentLeg.getId().intValue()))
            .andExpect(jsonPath("$.segment").value(DEFAULT_SEGMENT.intValue()))
            .andExpect(jsonPath("$.carrier").value(DEFAULT_CARRIER))
            .andExpect(jsonPath("$.mode").value(DEFAULT_MODE.toString()))
            .andExpect(jsonPath("$.distance").value(sameNumber(DEFAULT_DISTANCE)))
            .andExpect(jsonPath("$.fromCoordinateLat").value(sameNumber(DEFAULT_FROM_COORDINATE_LAT)))
            .andExpect(jsonPath("$.fromCoordinateLong").value(sameNumber(DEFAULT_FROM_COORDINATE_LONG)))
            .andExpect(jsonPath("$.toCoordinateLat").value(sameNumber(DEFAULT_TO_COORDINATE_LAT)))
            .andExpect(jsonPath("$.toCoordinateLong").value(sameNumber(DEFAULT_TO_COORDINATE_LONG)))
            .andExpect(jsonPath("$.fromIata").value(DEFAULT_FROM_IATA))
            .andExpect(jsonPath("$.toIata").value(DEFAULT_TO_IATA))
            .andExpect(jsonPath("$.emissionsFactor").value(sameNumber(DEFAULT_EMISSIONS_FACTOR)))
            .andExpect(jsonPath("$.efSource").value(DEFAULT_EF_SOURCE));
    }

    @Test
    @Transactional
    void getNonExistingShipmentLeg() throws Exception {
        // Get the shipmentLeg
        restShipmentLegMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipmentLeg() throws Exception {
        // Initialize the database
        insertedShipmentLeg = shipmentLegRepository.saveAndFlush(shipmentLeg);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentLeg
        ShipmentLeg updatedShipmentLeg = shipmentLegRepository.findById(shipmentLeg.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipmentLeg are not directly saved in db
        em.detach(updatedShipmentLeg);
        updatedShipmentLeg
            .segment(UPDATED_SEGMENT)
            .carrier(UPDATED_CARRIER)
            .mode(UPDATED_MODE)
            .distance(UPDATED_DISTANCE)
            .fromCoordinateLat(UPDATED_FROM_COORDINATE_LAT)
            .fromCoordinateLong(UPDATED_FROM_COORDINATE_LONG)
            .toCoordinateLat(UPDATED_TO_COORDINATE_LAT)
            .toCoordinateLong(UPDATED_TO_COORDINATE_LONG)
            .fromIata(UPDATED_FROM_IATA)
            .toIata(UPDATED_TO_IATA)
            .emissionsFactor(UPDATED_EMISSIONS_FACTOR)
            .efSource(UPDATED_EF_SOURCE);

        restShipmentLegMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShipmentLeg.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShipmentLeg))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentLeg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShipmentLegToMatchAllProperties(updatedShipmentLeg);
    }

    @Test
    @Transactional
    void putNonExistingShipmentLeg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentLeg.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentLegMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentLeg.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentLeg))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentLeg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipmentLeg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentLeg.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentLegMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentLeg))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentLeg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipmentLeg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentLeg.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentLegMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentLeg)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentLeg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentLegWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentLeg = shipmentLegRepository.saveAndFlush(shipmentLeg);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentLeg using partial update
        ShipmentLeg partialUpdatedShipmentLeg = new ShipmentLeg();
        partialUpdatedShipmentLeg.setId(shipmentLeg.getId());

        partialUpdatedShipmentLeg
            .segment(UPDATED_SEGMENT)
            .carrier(UPDATED_CARRIER)
            .toCoordinateLat(UPDATED_TO_COORDINATE_LAT)
            .fromIata(UPDATED_FROM_IATA)
            .toIata(UPDATED_TO_IATA)
            .emissionsFactor(UPDATED_EMISSIONS_FACTOR)
            .efSource(UPDATED_EF_SOURCE);

        restShipmentLegMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentLeg.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentLeg))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentLeg in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentLegUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShipmentLeg, shipmentLeg),
            getPersistedShipmentLeg(shipmentLeg)
        );
    }

    @Test
    @Transactional
    void fullUpdateShipmentLegWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentLeg = shipmentLegRepository.saveAndFlush(shipmentLeg);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentLeg using partial update
        ShipmentLeg partialUpdatedShipmentLeg = new ShipmentLeg();
        partialUpdatedShipmentLeg.setId(shipmentLeg.getId());

        partialUpdatedShipmentLeg
            .segment(UPDATED_SEGMENT)
            .carrier(UPDATED_CARRIER)
            .mode(UPDATED_MODE)
            .distance(UPDATED_DISTANCE)
            .fromCoordinateLat(UPDATED_FROM_COORDINATE_LAT)
            .fromCoordinateLong(UPDATED_FROM_COORDINATE_LONG)
            .toCoordinateLat(UPDATED_TO_COORDINATE_LAT)
            .toCoordinateLong(UPDATED_TO_COORDINATE_LONG)
            .fromIata(UPDATED_FROM_IATA)
            .toIata(UPDATED_TO_IATA)
            .emissionsFactor(UPDATED_EMISSIONS_FACTOR)
            .efSource(UPDATED_EF_SOURCE);

        restShipmentLegMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentLeg.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentLeg))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentLeg in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentLegUpdatableFieldsEquals(partialUpdatedShipmentLeg, getPersistedShipmentLeg(partialUpdatedShipmentLeg));
    }

    @Test
    @Transactional
    void patchNonExistingShipmentLeg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentLeg.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentLegMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentLeg.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentLeg))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentLeg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipmentLeg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentLeg.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentLegMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentLeg))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentLeg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipmentLeg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentLeg.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentLegMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shipmentLeg)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentLeg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipmentLeg() throws Exception {
        // Initialize the database
        insertedShipmentLeg = shipmentLegRepository.saveAndFlush(shipmentLeg);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipmentLeg
        restShipmentLegMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipmentLeg.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shipmentLegRepository.count();
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

    protected ShipmentLeg getPersistedShipmentLeg(ShipmentLeg shipmentLeg) {
        return shipmentLegRepository.findById(shipmentLeg.getId()).orElseThrow();
    }

    protected void assertPersistedShipmentLegToMatchAllProperties(ShipmentLeg expectedShipmentLeg) {
        assertShipmentLegAllPropertiesEquals(expectedShipmentLeg, getPersistedShipmentLeg(expectedShipmentLeg));
    }

    protected void assertPersistedShipmentLegToMatchUpdatableProperties(ShipmentLeg expectedShipmentLeg) {
        assertShipmentLegAllUpdatablePropertiesEquals(expectedShipmentLeg, getPersistedShipmentLeg(expectedShipmentLeg));
    }
}
