package com.bacuti.web.rest;

import static com.bacuti.domain.PurchasedQtyAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bacuti.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.PurchasedQty;
import com.bacuti.repository.PurchasedQtyRepository;
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
 * Integration tests for the {@link PurchasedQtyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PurchasedQtyResourceIT {

    private static final LocalDate DEFAULT_PURCHASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PURCHASE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_EMISSIONS = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_EMISSIONS = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/purchased-qties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PurchasedQtyRepository purchasedQtyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchasedQtyMockMvc;

    private PurchasedQty purchasedQty;

    private PurchasedQty insertedPurchasedQty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedQty createEntity(EntityManager em) {
        PurchasedQty purchasedQty = new PurchasedQty()
            .purchaseDate(DEFAULT_PURCHASE_DATE)
            .quantity(DEFAULT_QUANTITY)
            .totalEmissions(DEFAULT_TOTAL_EMISSIONS);
        return purchasedQty;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedQty createUpdatedEntity(EntityManager em) {
        PurchasedQty purchasedQty = new PurchasedQty()
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .quantity(UPDATED_QUANTITY)
            .totalEmissions(UPDATED_TOTAL_EMISSIONS);
        return purchasedQty;
    }

    @BeforeEach
    public void initTest() {
        purchasedQty = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPurchasedQty != null) {
            purchasedQtyRepository.delete(insertedPurchasedQty);
            insertedPurchasedQty = null;
        }
    }

    @Test
    @Transactional
    void createPurchasedQty() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PurchasedQty
        var returnedPurchasedQty = om.readValue(
            restPurchasedQtyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchasedQty)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PurchasedQty.class
        );

        // Validate the PurchasedQty in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPurchasedQtyUpdatableFieldsEquals(returnedPurchasedQty, getPersistedPurchasedQty(returnedPurchasedQty));

        insertedPurchasedQty = returnedPurchasedQty;
    }

    @Test
    @Transactional
    void createPurchasedQtyWithExistingId() throws Exception {
        // Create the PurchasedQty with an existing ID
        purchasedQty.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchasedQtyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchasedQty)))
            .andExpect(status().isBadRequest());

        // Validate the PurchasedQty in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPurchasedQties() throws Exception {
        // Initialize the database
        insertedPurchasedQty = purchasedQtyRepository.saveAndFlush(purchasedQty);

        // Get all the purchasedQtyList
        restPurchasedQtyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchasedQty.getId().intValue())))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].totalEmissions").value(hasItem(sameNumber(DEFAULT_TOTAL_EMISSIONS))));
    }

    @Test
    @Transactional
    void getPurchasedQty() throws Exception {
        // Initialize the database
        insertedPurchasedQty = purchasedQtyRepository.saveAndFlush(purchasedQty);

        // Get the purchasedQty
        restPurchasedQtyMockMvc
            .perform(get(ENTITY_API_URL_ID, purchasedQty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchasedQty.getId().intValue()))
            .andExpect(jsonPath("$.purchaseDate").value(DEFAULT_PURCHASE_DATE.toString()))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.totalEmissions").value(sameNumber(DEFAULT_TOTAL_EMISSIONS)));
    }

    @Test
    @Transactional
    void getNonExistingPurchasedQty() throws Exception {
        // Get the purchasedQty
        restPurchasedQtyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPurchasedQty() throws Exception {
        // Initialize the database
        insertedPurchasedQty = purchasedQtyRepository.saveAndFlush(purchasedQty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchasedQty
        PurchasedQty updatedPurchasedQty = purchasedQtyRepository.findById(purchasedQty.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPurchasedQty are not directly saved in db
        em.detach(updatedPurchasedQty);
        updatedPurchasedQty.purchaseDate(UPDATED_PURCHASE_DATE).quantity(UPDATED_QUANTITY).totalEmissions(UPDATED_TOTAL_EMISSIONS);

        restPurchasedQtyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPurchasedQty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPurchasedQty))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedQty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPurchasedQtyToMatchAllProperties(updatedPurchasedQty);
    }

    @Test
    @Transactional
    void putNonExistingPurchasedQty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedQty.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchasedQtyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchasedQty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(purchasedQty))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedQty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchasedQty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedQty.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedQtyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(purchasedQty))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedQty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchasedQty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedQty.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedQtyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchasedQty)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchasedQty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchasedQtyWithPatch() throws Exception {
        // Initialize the database
        insertedPurchasedQty = purchasedQtyRepository.saveAndFlush(purchasedQty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchasedQty using partial update
        PurchasedQty partialUpdatedPurchasedQty = new PurchasedQty();
        partialUpdatedPurchasedQty.setId(purchasedQty.getId());

        partialUpdatedPurchasedQty.totalEmissions(UPDATED_TOTAL_EMISSIONS);

        restPurchasedQtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchasedQty.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPurchasedQty))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedQty in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPurchasedQtyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPurchasedQty, purchasedQty),
            getPersistedPurchasedQty(purchasedQty)
        );
    }

    @Test
    @Transactional
    void fullUpdatePurchasedQtyWithPatch() throws Exception {
        // Initialize the database
        insertedPurchasedQty = purchasedQtyRepository.saveAndFlush(purchasedQty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchasedQty using partial update
        PurchasedQty partialUpdatedPurchasedQty = new PurchasedQty();
        partialUpdatedPurchasedQty.setId(purchasedQty.getId());

        partialUpdatedPurchasedQty.purchaseDate(UPDATED_PURCHASE_DATE).quantity(UPDATED_QUANTITY).totalEmissions(UPDATED_TOTAL_EMISSIONS);

        restPurchasedQtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchasedQty.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPurchasedQty))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedQty in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPurchasedQtyUpdatableFieldsEquals(partialUpdatedPurchasedQty, getPersistedPurchasedQty(partialUpdatedPurchasedQty));
    }

    @Test
    @Transactional
    void patchNonExistingPurchasedQty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedQty.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchasedQtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchasedQty.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(purchasedQty))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedQty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchasedQty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedQty.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedQtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(purchasedQty))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedQty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchasedQty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedQty.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedQtyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(purchasedQty)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchasedQty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchasedQty() throws Exception {
        // Initialize the database
        insertedPurchasedQty = purchasedQtyRepository.saveAndFlush(purchasedQty);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the purchasedQty
        restPurchasedQtyMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchasedQty.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return purchasedQtyRepository.count();
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

    protected PurchasedQty getPersistedPurchasedQty(PurchasedQty purchasedQty) {
        return purchasedQtyRepository.findById(purchasedQty.getId()).orElseThrow();
    }

    protected void assertPersistedPurchasedQtyToMatchAllProperties(PurchasedQty expectedPurchasedQty) {
        assertPurchasedQtyAllPropertiesEquals(expectedPurchasedQty, getPersistedPurchasedQty(expectedPurchasedQty));
    }

    protected void assertPersistedPurchasedQtyToMatchUpdatableProperties(PurchasedQty expectedPurchasedQty) {
        assertPurchasedQtyAllUpdatablePropertiesEquals(expectedPurchasedQty, getPersistedPurchasedQty(expectedPurchasedQty));
    }
}
