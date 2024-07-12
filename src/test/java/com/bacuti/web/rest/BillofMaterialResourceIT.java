package com.bacuti.web.rest;

import static com.bacuti.domain.BillofMaterialAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bacuti.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.BillofMaterial;
import com.bacuti.repository.BillofMaterialRepository;
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
 * Integration tests for the {@link BillofMaterialResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BillofMaterialResourceIT {

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);

    private static final BigDecimal DEFAULT_YIELD_FACTOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_YIELD_FACTOR = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/billof-materials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BillofMaterialRepository billofMaterialRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillofMaterialMockMvc;

    private BillofMaterial billofMaterial;

    private BillofMaterial insertedBillofMaterial;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillofMaterial createEntity(EntityManager em) {
        BillofMaterial billofMaterial = new BillofMaterial().quantity(DEFAULT_QUANTITY).yieldFactor(DEFAULT_YIELD_FACTOR);
        return billofMaterial;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillofMaterial createUpdatedEntity(EntityManager em) {
        BillofMaterial billofMaterial = new BillofMaterial().quantity(UPDATED_QUANTITY).yieldFactor(UPDATED_YIELD_FACTOR);
        return billofMaterial;
    }

    @BeforeEach
    public void initTest() {
        billofMaterial = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedBillofMaterial != null) {
            billofMaterialRepository.delete(insertedBillofMaterial);
            insertedBillofMaterial = null;
        }
    }

    @Test
    @Transactional
    void createBillofMaterial() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BillofMaterial
        var returnedBillofMaterial = om.readValue(
            restBillofMaterialMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billofMaterial)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BillofMaterial.class
        );

        // Validate the BillofMaterial in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBillofMaterialUpdatableFieldsEquals(returnedBillofMaterial, getPersistedBillofMaterial(returnedBillofMaterial));

        insertedBillofMaterial = returnedBillofMaterial;
    }

    @Test
    @Transactional
    void createBillofMaterialWithExistingId() throws Exception {
        // Create the BillofMaterial with an existing ID
        billofMaterial.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillofMaterialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billofMaterial)))
            .andExpect(status().isBadRequest());

        // Validate the BillofMaterial in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBillofMaterials() throws Exception {
        // Initialize the database
        insertedBillofMaterial = billofMaterialRepository.saveAndFlush(billofMaterial);

        // Get all the billofMaterialList
        restBillofMaterialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billofMaterial.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].yieldFactor").value(hasItem(sameNumber(DEFAULT_YIELD_FACTOR))));
    }

    @Test
    @Transactional
    void getBillofMaterial() throws Exception {
        // Initialize the database
        insertedBillofMaterial = billofMaterialRepository.saveAndFlush(billofMaterial);

        // Get the billofMaterial
        restBillofMaterialMockMvc
            .perform(get(ENTITY_API_URL_ID, billofMaterial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(billofMaterial.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.yieldFactor").value(sameNumber(DEFAULT_YIELD_FACTOR)));
    }

    @Test
    @Transactional
    void getNonExistingBillofMaterial() throws Exception {
        // Get the billofMaterial
        restBillofMaterialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBillofMaterial() throws Exception {
        // Initialize the database
        insertedBillofMaterial = billofMaterialRepository.saveAndFlush(billofMaterial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billofMaterial
        BillofMaterial updatedBillofMaterial = billofMaterialRepository.findById(billofMaterial.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBillofMaterial are not directly saved in db
        em.detach(updatedBillofMaterial);
        updatedBillofMaterial.quantity(UPDATED_QUANTITY).yieldFactor(UPDATED_YIELD_FACTOR);

        restBillofMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBillofMaterial.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBillofMaterial))
            )
            .andExpect(status().isOk());

        // Validate the BillofMaterial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBillofMaterialToMatchAllProperties(updatedBillofMaterial);
    }

    @Test
    @Transactional
    void putNonExistingBillofMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billofMaterial.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillofMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billofMaterial.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(billofMaterial))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillofMaterial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBillofMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billofMaterial.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillofMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(billofMaterial))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillofMaterial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBillofMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billofMaterial.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillofMaterialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billofMaterial)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillofMaterial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBillofMaterialWithPatch() throws Exception {
        // Initialize the database
        insertedBillofMaterial = billofMaterialRepository.saveAndFlush(billofMaterial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billofMaterial using partial update
        BillofMaterial partialUpdatedBillofMaterial = new BillofMaterial();
        partialUpdatedBillofMaterial.setId(billofMaterial.getId());

        partialUpdatedBillofMaterial.quantity(UPDATED_QUANTITY);

        restBillofMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillofMaterial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBillofMaterial))
            )
            .andExpect(status().isOk());

        // Validate the BillofMaterial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBillofMaterialUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBillofMaterial, billofMaterial),
            getPersistedBillofMaterial(billofMaterial)
        );
    }

    @Test
    @Transactional
    void fullUpdateBillofMaterialWithPatch() throws Exception {
        // Initialize the database
        insertedBillofMaterial = billofMaterialRepository.saveAndFlush(billofMaterial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billofMaterial using partial update
        BillofMaterial partialUpdatedBillofMaterial = new BillofMaterial();
        partialUpdatedBillofMaterial.setId(billofMaterial.getId());

        partialUpdatedBillofMaterial.quantity(UPDATED_QUANTITY).yieldFactor(UPDATED_YIELD_FACTOR);

        restBillofMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillofMaterial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBillofMaterial))
            )
            .andExpect(status().isOk());

        // Validate the BillofMaterial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBillofMaterialUpdatableFieldsEquals(partialUpdatedBillofMaterial, getPersistedBillofMaterial(partialUpdatedBillofMaterial));
    }

    @Test
    @Transactional
    void patchNonExistingBillofMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billofMaterial.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillofMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, billofMaterial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(billofMaterial))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillofMaterial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBillofMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billofMaterial.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillofMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(billofMaterial))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillofMaterial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBillofMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billofMaterial.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillofMaterialMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(billofMaterial)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillofMaterial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBillofMaterial() throws Exception {
        // Initialize the database
        insertedBillofMaterial = billofMaterialRepository.saveAndFlush(billofMaterial);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the billofMaterial
        restBillofMaterialMockMvc
            .perform(delete(ENTITY_API_URL_ID, billofMaterial.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return billofMaterialRepository.count();
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

    protected BillofMaterial getPersistedBillofMaterial(BillofMaterial billofMaterial) {
        return billofMaterialRepository.findById(billofMaterial.getId()).orElseThrow();
    }

    protected void assertPersistedBillofMaterialToMatchAllProperties(BillofMaterial expectedBillofMaterial) {
        assertBillofMaterialAllPropertiesEquals(expectedBillofMaterial, getPersistedBillofMaterial(expectedBillofMaterial));
    }

    protected void assertPersistedBillofMaterialToMatchUpdatableProperties(BillofMaterial expectedBillofMaterial) {
        assertBillofMaterialAllUpdatablePropertiesEquals(expectedBillofMaterial, getPersistedBillofMaterial(expectedBillofMaterial));
    }
}
