package com.bacuti.web.rest;

import static com.bacuti.domain.ItemSupplierAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bacuti.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.ItemSupplier;
import com.bacuti.repository.ItemSupplierRepository;
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
 * Integration tests for the {@link ItemSupplierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemSupplierResourceIT {

    private static final String DEFAULT_SUPPLIER_OWN_ITEM = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER_OWN_ITEM = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_SUPPLIER_MIX = new BigDecimal(1);
    private static final BigDecimal UPDATED_SUPPLIER_MIX = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SUPPLIER_EMISSION_MULTIPLIER = new BigDecimal(1);
    private static final BigDecimal UPDATED_SUPPLIER_EMISSION_MULTIPLIER = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INTENSITY_UNITS = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTENSITY_UNITS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INTENSITY_SCALING_FACTOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTENSITY_SCALING_FACTOR = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/item-suppliers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ItemSupplierRepository itemSupplierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemSupplierMockMvc;

    private ItemSupplier itemSupplier;

    private ItemSupplier insertedItemSupplier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemSupplier createEntity(EntityManager em) {
        ItemSupplier itemSupplier = new ItemSupplier()
            .supplierOwnItem(DEFAULT_SUPPLIER_OWN_ITEM)
            .supplierMix(DEFAULT_SUPPLIER_MIX)
            .supplierEmissionMultiplier(DEFAULT_SUPPLIER_EMISSION_MULTIPLIER)
            .intensityUnits(DEFAULT_INTENSITY_UNITS)
            .intensityScalingFactor(DEFAULT_INTENSITY_SCALING_FACTOR);
        return itemSupplier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemSupplier createUpdatedEntity(EntityManager em) {
        ItemSupplier itemSupplier = new ItemSupplier()
            .supplierOwnItem(UPDATED_SUPPLIER_OWN_ITEM)
            .supplierMix(UPDATED_SUPPLIER_MIX)
            .supplierEmissionMultiplier(UPDATED_SUPPLIER_EMISSION_MULTIPLIER)
            .intensityUnits(UPDATED_INTENSITY_UNITS)
            .intensityScalingFactor(UPDATED_INTENSITY_SCALING_FACTOR);
        return itemSupplier;
    }

    @BeforeEach
    public void initTest() {
        itemSupplier = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedItemSupplier != null) {
            itemSupplierRepository.delete(insertedItemSupplier);
            insertedItemSupplier = null;
        }
    }

    @Test
    @Transactional
    void createItemSupplier() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ItemSupplier
        var returnedItemSupplier = om.readValue(
            restItemSupplierMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemSupplier)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ItemSupplier.class
        );

        // Validate the ItemSupplier in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertItemSupplierUpdatableFieldsEquals(returnedItemSupplier, getPersistedItemSupplier(returnedItemSupplier));

        insertedItemSupplier = returnedItemSupplier;
    }

    @Test
    @Transactional
    void createItemSupplierWithExistingId() throws Exception {
        // Create the ItemSupplier with an existing ID
        itemSupplier.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemSupplierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemSupplier)))
            .andExpect(status().isBadRequest());

        // Validate the ItemSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllItemSuppliers() throws Exception {
        // Initialize the database
        insertedItemSupplier = itemSupplierRepository.saveAndFlush(itemSupplier);

        // Get all the itemSupplierList
        restItemSupplierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemSupplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].supplierOwnItem").value(hasItem(DEFAULT_SUPPLIER_OWN_ITEM)))
            .andExpect(jsonPath("$.[*].supplierMix").value(hasItem(sameNumber(DEFAULT_SUPPLIER_MIX))))
            .andExpect(jsonPath("$.[*].supplierEmissionMultiplier").value(hasItem(sameNumber(DEFAULT_SUPPLIER_EMISSION_MULTIPLIER))))
            .andExpect(jsonPath("$.[*].intensityUnits").value(hasItem(sameNumber(DEFAULT_INTENSITY_UNITS))))
            .andExpect(jsonPath("$.[*].intensityScalingFactor").value(hasItem(sameNumber(DEFAULT_INTENSITY_SCALING_FACTOR))));
    }

    @Test
    @Transactional
    void getItemSupplier() throws Exception {
        // Initialize the database
        insertedItemSupplier = itemSupplierRepository.saveAndFlush(itemSupplier);

        // Get the itemSupplier
        restItemSupplierMockMvc
            .perform(get(ENTITY_API_URL_ID, itemSupplier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemSupplier.getId().intValue()))
            .andExpect(jsonPath("$.supplierOwnItem").value(DEFAULT_SUPPLIER_OWN_ITEM))
            .andExpect(jsonPath("$.supplierMix").value(sameNumber(DEFAULT_SUPPLIER_MIX)))
            .andExpect(jsonPath("$.supplierEmissionMultiplier").value(sameNumber(DEFAULT_SUPPLIER_EMISSION_MULTIPLIER)))
            .andExpect(jsonPath("$.intensityUnits").value(sameNumber(DEFAULT_INTENSITY_UNITS)))
            .andExpect(jsonPath("$.intensityScalingFactor").value(sameNumber(DEFAULT_INTENSITY_SCALING_FACTOR)));
    }

    @Test
    @Transactional
    void getNonExistingItemSupplier() throws Exception {
        // Get the itemSupplier
        restItemSupplierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingItemSupplier() throws Exception {
        // Initialize the database
        insertedItemSupplier = itemSupplierRepository.saveAndFlush(itemSupplier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the itemSupplier
        ItemSupplier updatedItemSupplier = itemSupplierRepository.findById(itemSupplier.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedItemSupplier are not directly saved in db
        em.detach(updatedItemSupplier);
        updatedItemSupplier
            .supplierOwnItem(UPDATED_SUPPLIER_OWN_ITEM)
            .supplierMix(UPDATED_SUPPLIER_MIX)
            .supplierEmissionMultiplier(UPDATED_SUPPLIER_EMISSION_MULTIPLIER)
            .intensityUnits(UPDATED_INTENSITY_UNITS)
            .intensityScalingFactor(UPDATED_INTENSITY_SCALING_FACTOR);

        restItemSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedItemSupplier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedItemSupplier))
            )
            .andExpect(status().isOk());

        // Validate the ItemSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedItemSupplierToMatchAllProperties(updatedItemSupplier);
    }

    @Test
    @Transactional
    void putNonExistingItemSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemSupplier.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemSupplier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(itemSupplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemSupplier.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(itemSupplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemSupplier.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemSupplierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemSupplier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemSupplierWithPatch() throws Exception {
        // Initialize the database
        insertedItemSupplier = itemSupplierRepository.saveAndFlush(itemSupplier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the itemSupplier using partial update
        ItemSupplier partialUpdatedItemSupplier = new ItemSupplier();
        partialUpdatedItemSupplier.setId(itemSupplier.getId());

        partialUpdatedItemSupplier
            .supplierOwnItem(UPDATED_SUPPLIER_OWN_ITEM)
            .supplierEmissionMultiplier(UPDATED_SUPPLIER_EMISSION_MULTIPLIER)
            .intensityUnits(UPDATED_INTENSITY_UNITS)
            .intensityScalingFactor(UPDATED_INTENSITY_SCALING_FACTOR);

        restItemSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemSupplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedItemSupplier))
            )
            .andExpect(status().isOk());

        // Validate the ItemSupplier in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertItemSupplierUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedItemSupplier, itemSupplier),
            getPersistedItemSupplier(itemSupplier)
        );
    }

    @Test
    @Transactional
    void fullUpdateItemSupplierWithPatch() throws Exception {
        // Initialize the database
        insertedItemSupplier = itemSupplierRepository.saveAndFlush(itemSupplier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the itemSupplier using partial update
        ItemSupplier partialUpdatedItemSupplier = new ItemSupplier();
        partialUpdatedItemSupplier.setId(itemSupplier.getId());

        partialUpdatedItemSupplier
            .supplierOwnItem(UPDATED_SUPPLIER_OWN_ITEM)
            .supplierMix(UPDATED_SUPPLIER_MIX)
            .supplierEmissionMultiplier(UPDATED_SUPPLIER_EMISSION_MULTIPLIER)
            .intensityUnits(UPDATED_INTENSITY_UNITS)
            .intensityScalingFactor(UPDATED_INTENSITY_SCALING_FACTOR);

        restItemSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemSupplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedItemSupplier))
            )
            .andExpect(status().isOk());

        // Validate the ItemSupplier in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertItemSupplierUpdatableFieldsEquals(partialUpdatedItemSupplier, getPersistedItemSupplier(partialUpdatedItemSupplier));
    }

    @Test
    @Transactional
    void patchNonExistingItemSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemSupplier.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemSupplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(itemSupplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemSupplier.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(itemSupplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemSupplier.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemSupplierMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(itemSupplier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemSupplier() throws Exception {
        // Initialize the database
        insertedItemSupplier = itemSupplierRepository.saveAndFlush(itemSupplier);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the itemSupplier
        restItemSupplierMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemSupplier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return itemSupplierRepository.count();
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

    protected ItemSupplier getPersistedItemSupplier(ItemSupplier itemSupplier) {
        return itemSupplierRepository.findById(itemSupplier.getId()).orElseThrow();
    }

    protected void assertPersistedItemSupplierToMatchAllProperties(ItemSupplier expectedItemSupplier) {
        assertItemSupplierAllPropertiesEquals(expectedItemSupplier, getPersistedItemSupplier(expectedItemSupplier));
    }

    protected void assertPersistedItemSupplierToMatchUpdatableProperties(ItemSupplier expectedItemSupplier) {
        assertItemSupplierAllUpdatablePropertiesEquals(expectedItemSupplier, getPersistedItemSupplier(expectedItemSupplier));
    }
}
