package com.bacuti.web.rest;

import static com.bacuti.domain.ProductionQtyAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bacuti.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.ProductionQty;
import com.bacuti.repository.ProductionQtyRepository;
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
 * Integration tests for the {@link ProductionQtyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductionQtyResourceIT {

    private static final LocalDate DEFAULT_PRODUCTIONN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PRODUCTIONN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);

    private static final String DEFAULT_HEAT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_HEAT_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/production-qties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductionQtyRepository productionQtyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductionQtyMockMvc;

    private ProductionQty productionQty;

    private ProductionQty insertedProductionQty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductionQty createEntity(EntityManager em) {
        ProductionQty productionQty = new ProductionQty()
            .productionnDate(DEFAULT_PRODUCTIONN_DATE)
            .quantity(DEFAULT_QUANTITY)
            .heatNumber(DEFAULT_HEAT_NUMBER);
        return productionQty;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductionQty createUpdatedEntity(EntityManager em) {
        ProductionQty productionQty = new ProductionQty()
            .productionnDate(UPDATED_PRODUCTIONN_DATE)
            .quantity(UPDATED_QUANTITY)
            .heatNumber(UPDATED_HEAT_NUMBER);
        return productionQty;
    }

    @BeforeEach
    public void initTest() {
        productionQty = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductionQty != null) {
            productionQtyRepository.delete(insertedProductionQty);
            insertedProductionQty = null;
        }
    }

    @Test
    @Transactional
    void createProductionQty() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductionQty
        var returnedProductionQty = om.readValue(
            restProductionQtyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productionQty)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductionQty.class
        );

        // Validate the ProductionQty in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductionQtyUpdatableFieldsEquals(returnedProductionQty, getPersistedProductionQty(returnedProductionQty));

        insertedProductionQty = returnedProductionQty;
    }

    @Test
    @Transactional
    void createProductionQtyWithExistingId() throws Exception {
        // Create the ProductionQty with an existing ID
        productionQty.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductionQtyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productionQty)))
            .andExpect(status().isBadRequest());

        // Validate the ProductionQty in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductionQties() throws Exception {
        // Initialize the database
        insertedProductionQty = productionQtyRepository.saveAndFlush(productionQty);

        // Get all the productionQtyList
        restProductionQtyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productionQty.getId().intValue())))
            .andExpect(jsonPath("$.[*].productionnDate").value(hasItem(DEFAULT_PRODUCTIONN_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].heatNumber").value(hasItem(DEFAULT_HEAT_NUMBER)));
    }

    @Test
    @Transactional
    void getProductionQty() throws Exception {
        // Initialize the database
        insertedProductionQty = productionQtyRepository.saveAndFlush(productionQty);

        // Get the productionQty
        restProductionQtyMockMvc
            .perform(get(ENTITY_API_URL_ID, productionQty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productionQty.getId().intValue()))
            .andExpect(jsonPath("$.productionnDate").value(DEFAULT_PRODUCTIONN_DATE.toString()))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.heatNumber").value(DEFAULT_HEAT_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingProductionQty() throws Exception {
        // Get the productionQty
        restProductionQtyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductionQty() throws Exception {
        // Initialize the database
        insertedProductionQty = productionQtyRepository.saveAndFlush(productionQty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productionQty
        ProductionQty updatedProductionQty = productionQtyRepository.findById(productionQty.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductionQty are not directly saved in db
        em.detach(updatedProductionQty);
        updatedProductionQty.productionnDate(UPDATED_PRODUCTIONN_DATE).quantity(UPDATED_QUANTITY).heatNumber(UPDATED_HEAT_NUMBER);

        restProductionQtyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductionQty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductionQty))
            )
            .andExpect(status().isOk());

        // Validate the ProductionQty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductionQtyToMatchAllProperties(updatedProductionQty);
    }

    @Test
    @Transactional
    void putNonExistingProductionQty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productionQty.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductionQtyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productionQty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productionQty))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductionQty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductionQty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productionQty.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductionQtyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productionQty))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductionQty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductionQty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productionQty.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductionQtyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productionQty)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductionQty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductionQtyWithPatch() throws Exception {
        // Initialize the database
        insertedProductionQty = productionQtyRepository.saveAndFlush(productionQty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productionQty using partial update
        ProductionQty partialUpdatedProductionQty = new ProductionQty();
        partialUpdatedProductionQty.setId(productionQty.getId());

        partialUpdatedProductionQty.heatNumber(UPDATED_HEAT_NUMBER);

        restProductionQtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductionQty.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductionQty))
            )
            .andExpect(status().isOk());

        // Validate the ProductionQty in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductionQtyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductionQty, productionQty),
            getPersistedProductionQty(productionQty)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductionQtyWithPatch() throws Exception {
        // Initialize the database
        insertedProductionQty = productionQtyRepository.saveAndFlush(productionQty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productionQty using partial update
        ProductionQty partialUpdatedProductionQty = new ProductionQty();
        partialUpdatedProductionQty.setId(productionQty.getId());

        partialUpdatedProductionQty.productionnDate(UPDATED_PRODUCTIONN_DATE).quantity(UPDATED_QUANTITY).heatNumber(UPDATED_HEAT_NUMBER);

        restProductionQtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductionQty.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductionQty))
            )
            .andExpect(status().isOk());

        // Validate the ProductionQty in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductionQtyUpdatableFieldsEquals(partialUpdatedProductionQty, getPersistedProductionQty(partialUpdatedProductionQty));
    }

    @Test
    @Transactional
    void patchNonExistingProductionQty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productionQty.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductionQtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productionQty.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productionQty))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductionQty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductionQty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productionQty.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductionQtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productionQty))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductionQty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductionQty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productionQty.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductionQtyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productionQty)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductionQty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductionQty() throws Exception {
        // Initialize the database
        insertedProductionQty = productionQtyRepository.saveAndFlush(productionQty);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productionQty
        restProductionQtyMockMvc
            .perform(delete(ENTITY_API_URL_ID, productionQty.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productionQtyRepository.count();
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

    protected ProductionQty getPersistedProductionQty(ProductionQty productionQty) {
        return productionQtyRepository.findById(productionQty.getId()).orElseThrow();
    }

    protected void assertPersistedProductionQtyToMatchAllProperties(ProductionQty expectedProductionQty) {
        assertProductionQtyAllPropertiesEquals(expectedProductionQty, getPersistedProductionQty(expectedProductionQty));
    }

    protected void assertPersistedProductionQtyToMatchUpdatableProperties(ProductionQty expectedProductionQty) {
        assertProductionQtyAllUpdatablePropertiesEquals(expectedProductionQty, getPersistedProductionQty(expectedProductionQty));
    }
}
