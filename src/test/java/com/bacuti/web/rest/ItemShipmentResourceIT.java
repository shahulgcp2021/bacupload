package com.bacuti.web.rest;

import static com.bacuti.domain.ItemShipmentAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bacuti.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.ItemShipment;
import com.bacuti.repository.ItemShipmentRepository;
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
 * Integration tests for the {@link ItemShipmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemShipmentResourceIT {

    private static final LocalDate DEFAULT_SHIPMENTDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SHIPMENTDATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_SHIPPER = "AAAAAAAAAA";
    private static final String UPDATED_SHIPPER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_UPSTREAM = false;
    private static final Boolean UPDATED_UPSTREAM = true;

    private static final BigDecimal DEFAULT_QUANTITY_SHIPPED = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY_SHIPPED = new BigDecimal(2);

    private static final BigDecimal DEFAULT_WEIGHT_IN_KG = new BigDecimal(1);
    private static final BigDecimal UPDATED_WEIGHT_IN_KG = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/item-shipments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ItemShipmentRepository itemShipmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemShipmentMockMvc;

    private ItemShipment itemShipment;

    private ItemShipment insertedItemShipment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemShipment createEntity(EntityManager em) {
        ItemShipment itemShipment = new ItemShipment()
            .shipmentdate(DEFAULT_SHIPMENTDATE)
            .shipper(DEFAULT_SHIPPER)
            .upstream(DEFAULT_UPSTREAM)
            .quantityShipped(DEFAULT_QUANTITY_SHIPPED)
            .weightInKg(DEFAULT_WEIGHT_IN_KG);
        return itemShipment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemShipment createUpdatedEntity(EntityManager em) {
        ItemShipment itemShipment = new ItemShipment()
            .shipmentdate(UPDATED_SHIPMENTDATE)
            .shipper(UPDATED_SHIPPER)
            .upstream(UPDATED_UPSTREAM)
            .quantityShipped(UPDATED_QUANTITY_SHIPPED)
            .weightInKg(UPDATED_WEIGHT_IN_KG);
        return itemShipment;
    }

    @BeforeEach
    public void initTest() {
        itemShipment = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedItemShipment != null) {
            itemShipmentRepository.delete(insertedItemShipment);
            insertedItemShipment = null;
        }
    }

    @Test
    @Transactional
    void createItemShipment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ItemShipment
        var returnedItemShipment = om.readValue(
            restItemShipmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemShipment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ItemShipment.class
        );

        // Validate the ItemShipment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertItemShipmentUpdatableFieldsEquals(returnedItemShipment, getPersistedItemShipment(returnedItemShipment));

        insertedItemShipment = returnedItemShipment;
    }

    @Test
    @Transactional
    void createItemShipmentWithExistingId() throws Exception {
        // Create the ItemShipment with an existing ID
        itemShipment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemShipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemShipment)))
            .andExpect(status().isBadRequest());

        // Validate the ItemShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllItemShipments() throws Exception {
        // Initialize the database
        insertedItemShipment = itemShipmentRepository.saveAndFlush(itemShipment);

        // Get all the itemShipmentList
        restItemShipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemShipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].shipmentdate").value(hasItem(DEFAULT_SHIPMENTDATE.toString())))
            .andExpect(jsonPath("$.[*].shipper").value(hasItem(DEFAULT_SHIPPER)))
            .andExpect(jsonPath("$.[*].upstream").value(hasItem(DEFAULT_UPSTREAM.booleanValue())))
            .andExpect(jsonPath("$.[*].quantityShipped").value(hasItem(sameNumber(DEFAULT_QUANTITY_SHIPPED))))
            .andExpect(jsonPath("$.[*].weightInKg").value(hasItem(sameNumber(DEFAULT_WEIGHT_IN_KG))));
    }

    @Test
    @Transactional
    void getItemShipment() throws Exception {
        // Initialize the database
        insertedItemShipment = itemShipmentRepository.saveAndFlush(itemShipment);

        // Get the itemShipment
        restItemShipmentMockMvc
            .perform(get(ENTITY_API_URL_ID, itemShipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemShipment.getId().intValue()))
            .andExpect(jsonPath("$.shipmentdate").value(DEFAULT_SHIPMENTDATE.toString()))
            .andExpect(jsonPath("$.shipper").value(DEFAULT_SHIPPER))
            .andExpect(jsonPath("$.upstream").value(DEFAULT_UPSTREAM.booleanValue()))
            .andExpect(jsonPath("$.quantityShipped").value(sameNumber(DEFAULT_QUANTITY_SHIPPED)))
            .andExpect(jsonPath("$.weightInKg").value(sameNumber(DEFAULT_WEIGHT_IN_KG)));
    }

    @Test
    @Transactional
    void getNonExistingItemShipment() throws Exception {
        // Get the itemShipment
        restItemShipmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingItemShipment() throws Exception {
        // Initialize the database
        insertedItemShipment = itemShipmentRepository.saveAndFlush(itemShipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the itemShipment
        ItemShipment updatedItemShipment = itemShipmentRepository.findById(itemShipment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedItemShipment are not directly saved in db
        em.detach(updatedItemShipment);
        updatedItemShipment
            .shipmentdate(UPDATED_SHIPMENTDATE)
            .shipper(UPDATED_SHIPPER)
            .upstream(UPDATED_UPSTREAM)
            .quantityShipped(UPDATED_QUANTITY_SHIPPED)
            .weightInKg(UPDATED_WEIGHT_IN_KG);

        restItemShipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedItemShipment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedItemShipment))
            )
            .andExpect(status().isOk());

        // Validate the ItemShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedItemShipmentToMatchAllProperties(updatedItemShipment);
    }

    @Test
    @Transactional
    void putNonExistingItemShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemShipment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemShipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemShipment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(itemShipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemShipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemShipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(itemShipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemShipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemShipmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemShipment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemShipmentWithPatch() throws Exception {
        // Initialize the database
        insertedItemShipment = itemShipmentRepository.saveAndFlush(itemShipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the itemShipment using partial update
        ItemShipment partialUpdatedItemShipment = new ItemShipment();
        partialUpdatedItemShipment.setId(itemShipment.getId());

        partialUpdatedItemShipment.shipper(UPDATED_SHIPPER);

        restItemShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemShipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedItemShipment))
            )
            .andExpect(status().isOk());

        // Validate the ItemShipment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertItemShipmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedItemShipment, itemShipment),
            getPersistedItemShipment(itemShipment)
        );
    }

    @Test
    @Transactional
    void fullUpdateItemShipmentWithPatch() throws Exception {
        // Initialize the database
        insertedItemShipment = itemShipmentRepository.saveAndFlush(itemShipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the itemShipment using partial update
        ItemShipment partialUpdatedItemShipment = new ItemShipment();
        partialUpdatedItemShipment.setId(itemShipment.getId());

        partialUpdatedItemShipment
            .shipmentdate(UPDATED_SHIPMENTDATE)
            .shipper(UPDATED_SHIPPER)
            .upstream(UPDATED_UPSTREAM)
            .quantityShipped(UPDATED_QUANTITY_SHIPPED)
            .weightInKg(UPDATED_WEIGHT_IN_KG);

        restItemShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemShipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedItemShipment))
            )
            .andExpect(status().isOk());

        // Validate the ItemShipment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertItemShipmentUpdatableFieldsEquals(partialUpdatedItemShipment, getPersistedItemShipment(partialUpdatedItemShipment));
    }

    @Test
    @Transactional
    void patchNonExistingItemShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemShipment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemShipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(itemShipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemShipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(itemShipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemShipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemShipmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(itemShipment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemShipment() throws Exception {
        // Initialize the database
        insertedItemShipment = itemShipmentRepository.saveAndFlush(itemShipment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the itemShipment
        restItemShipmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemShipment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return itemShipmentRepository.count();
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

    protected ItemShipment getPersistedItemShipment(ItemShipment itemShipment) {
        return itemShipmentRepository.findById(itemShipment.getId()).orElseThrow();
    }

    protected void assertPersistedItemShipmentToMatchAllProperties(ItemShipment expectedItemShipment) {
        assertItemShipmentAllPropertiesEquals(expectedItemShipment, getPersistedItemShipment(expectedItemShipment));
    }

    protected void assertPersistedItemShipmentToMatchUpdatableProperties(ItemShipment expectedItemShipment) {
        assertItemShipmentAllUpdatablePropertiesEquals(expectedItemShipment, getPersistedItemShipment(expectedItemShipment));
    }
}
