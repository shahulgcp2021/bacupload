package com.bacuti.web.rest;

import static com.bacuti.domain.ProductUsageDetailAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bacuti.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.ProductUsageDetail;
import com.bacuti.domain.enumeration.EmissionSource;
import com.bacuti.repository.ProductUsageDetailRepository;
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
 * Integration tests for the {@link ProductUsageDetailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductUsageDetailResourceIT {

    private static final BigDecimal DEFAULT_USEFUL_LIFE_YRS = new BigDecimal(1);
    private static final BigDecimal UPDATED_USEFUL_LIFE_YRS = new BigDecimal(2);

    private static final EmissionSource DEFAULT_EMISSION_SOURCE = EmissionSource.CATEG1;
    private static final EmissionSource UPDATED_EMISSION_SOURCE = EmissionSource.CATEG2;

    private static final String DEFAULT_DETAIL = "AAAAAAAAAA";
    private static final String UPDATED_DETAIL = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AVG_QUANTITY_PER_DAY = new BigDecimal(1);
    private static final BigDecimal UPDATED_AVG_QUANTITY_PER_DAY = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PROPORTION = new BigDecimal(1);
    private static final BigDecimal UPDATED_PROPORTION = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/product-usage-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductUsageDetailRepository productUsageDetailRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductUsageDetailMockMvc;

    private ProductUsageDetail productUsageDetail;

    private ProductUsageDetail insertedProductUsageDetail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductUsageDetail createEntity(EntityManager em) {
        ProductUsageDetail productUsageDetail = new ProductUsageDetail()
            .usefulLifeYrs(DEFAULT_USEFUL_LIFE_YRS)
            .emissionSource(DEFAULT_EMISSION_SOURCE)
            .detail(DEFAULT_DETAIL)
            .avgQuantityPerDay(DEFAULT_AVG_QUANTITY_PER_DAY)
            .proportion(DEFAULT_PROPORTION);
        return productUsageDetail;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductUsageDetail createUpdatedEntity(EntityManager em) {
        ProductUsageDetail productUsageDetail = new ProductUsageDetail()
            .usefulLifeYrs(UPDATED_USEFUL_LIFE_YRS)
            .emissionSource(UPDATED_EMISSION_SOURCE)
            .detail(UPDATED_DETAIL)
            .avgQuantityPerDay(UPDATED_AVG_QUANTITY_PER_DAY)
            .proportion(UPDATED_PROPORTION);
        return productUsageDetail;
    }

    @BeforeEach
    public void initTest() {
        productUsageDetail = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductUsageDetail != null) {
            productUsageDetailRepository.delete(insertedProductUsageDetail);
            insertedProductUsageDetail = null;
        }
    }

    @Test
    @Transactional
    void createProductUsageDetail() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductUsageDetail
        var returnedProductUsageDetail = om.readValue(
            restProductUsageDetailMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productUsageDetail)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductUsageDetail.class
        );

        // Validate the ProductUsageDetail in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductUsageDetailUpdatableFieldsEquals(
            returnedProductUsageDetail,
            getPersistedProductUsageDetail(returnedProductUsageDetail)
        );

        insertedProductUsageDetail = returnedProductUsageDetail;
    }

    @Test
    @Transactional
    void createProductUsageDetailWithExistingId() throws Exception {
        // Create the ProductUsageDetail with an existing ID
        productUsageDetail.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductUsageDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productUsageDetail)))
            .andExpect(status().isBadRequest());

        // Validate the ProductUsageDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductUsageDetails() throws Exception {
        // Initialize the database
        insertedProductUsageDetail = productUsageDetailRepository.saveAndFlush(productUsageDetail);

        // Get all the productUsageDetailList
        restProductUsageDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productUsageDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].usefulLifeYrs").value(hasItem(sameNumber(DEFAULT_USEFUL_LIFE_YRS))))
            .andExpect(jsonPath("$.[*].emissionSource").value(hasItem(DEFAULT_EMISSION_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL)))
            .andExpect(jsonPath("$.[*].avgQuantityPerDay").value(hasItem(sameNumber(DEFAULT_AVG_QUANTITY_PER_DAY))))
            .andExpect(jsonPath("$.[*].proportion").value(hasItem(sameNumber(DEFAULT_PROPORTION))));
    }

    @Test
    @Transactional
    void getProductUsageDetail() throws Exception {
        // Initialize the database
        insertedProductUsageDetail = productUsageDetailRepository.saveAndFlush(productUsageDetail);

        // Get the productUsageDetail
        restProductUsageDetailMockMvc
            .perform(get(ENTITY_API_URL_ID, productUsageDetail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productUsageDetail.getId().intValue()))
            .andExpect(jsonPath("$.usefulLifeYrs").value(sameNumber(DEFAULT_USEFUL_LIFE_YRS)))
            .andExpect(jsonPath("$.emissionSource").value(DEFAULT_EMISSION_SOURCE.toString()))
            .andExpect(jsonPath("$.detail").value(DEFAULT_DETAIL))
            .andExpect(jsonPath("$.avgQuantityPerDay").value(sameNumber(DEFAULT_AVG_QUANTITY_PER_DAY)))
            .andExpect(jsonPath("$.proportion").value(sameNumber(DEFAULT_PROPORTION)));
    }

    @Test
    @Transactional
    void getNonExistingProductUsageDetail() throws Exception {
        // Get the productUsageDetail
        restProductUsageDetailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductUsageDetail() throws Exception {
        // Initialize the database
        insertedProductUsageDetail = productUsageDetailRepository.saveAndFlush(productUsageDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productUsageDetail
        ProductUsageDetail updatedProductUsageDetail = productUsageDetailRepository.findById(productUsageDetail.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductUsageDetail are not directly saved in db
        em.detach(updatedProductUsageDetail);
        updatedProductUsageDetail
            .usefulLifeYrs(UPDATED_USEFUL_LIFE_YRS)
            .emissionSource(UPDATED_EMISSION_SOURCE)
            .detail(UPDATED_DETAIL)
            .avgQuantityPerDay(UPDATED_AVG_QUANTITY_PER_DAY)
            .proportion(UPDATED_PROPORTION);

        restProductUsageDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductUsageDetail.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductUsageDetail))
            )
            .andExpect(status().isOk());

        // Validate the ProductUsageDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductUsageDetailToMatchAllProperties(updatedProductUsageDetail);
    }

    @Test
    @Transactional
    void putNonExistingProductUsageDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productUsageDetail.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductUsageDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productUsageDetail.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productUsageDetail))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductUsageDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductUsageDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productUsageDetail.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductUsageDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productUsageDetail))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductUsageDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductUsageDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productUsageDetail.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductUsageDetailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productUsageDetail)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductUsageDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductUsageDetailWithPatch() throws Exception {
        // Initialize the database
        insertedProductUsageDetail = productUsageDetailRepository.saveAndFlush(productUsageDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productUsageDetail using partial update
        ProductUsageDetail partialUpdatedProductUsageDetail = new ProductUsageDetail();
        partialUpdatedProductUsageDetail.setId(productUsageDetail.getId());

        partialUpdatedProductUsageDetail
            .usefulLifeYrs(UPDATED_USEFUL_LIFE_YRS)
            .emissionSource(UPDATED_EMISSION_SOURCE)
            .avgQuantityPerDay(UPDATED_AVG_QUANTITY_PER_DAY);

        restProductUsageDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductUsageDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductUsageDetail))
            )
            .andExpect(status().isOk());

        // Validate the ProductUsageDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUsageDetailUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductUsageDetail, productUsageDetail),
            getPersistedProductUsageDetail(productUsageDetail)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductUsageDetailWithPatch() throws Exception {
        // Initialize the database
        insertedProductUsageDetail = productUsageDetailRepository.saveAndFlush(productUsageDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productUsageDetail using partial update
        ProductUsageDetail partialUpdatedProductUsageDetail = new ProductUsageDetail();
        partialUpdatedProductUsageDetail.setId(productUsageDetail.getId());

        partialUpdatedProductUsageDetail
            .usefulLifeYrs(UPDATED_USEFUL_LIFE_YRS)
            .emissionSource(UPDATED_EMISSION_SOURCE)
            .detail(UPDATED_DETAIL)
            .avgQuantityPerDay(UPDATED_AVG_QUANTITY_PER_DAY)
            .proportion(UPDATED_PROPORTION);

        restProductUsageDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductUsageDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductUsageDetail))
            )
            .andExpect(status().isOk());

        // Validate the ProductUsageDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUsageDetailUpdatableFieldsEquals(
            partialUpdatedProductUsageDetail,
            getPersistedProductUsageDetail(partialUpdatedProductUsageDetail)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductUsageDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productUsageDetail.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductUsageDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productUsageDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productUsageDetail))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductUsageDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductUsageDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productUsageDetail.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductUsageDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productUsageDetail))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductUsageDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductUsageDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productUsageDetail.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductUsageDetailMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productUsageDetail)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductUsageDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductUsageDetail() throws Exception {
        // Initialize the database
        insertedProductUsageDetail = productUsageDetailRepository.saveAndFlush(productUsageDetail);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productUsageDetail
        restProductUsageDetailMockMvc
            .perform(delete(ENTITY_API_URL_ID, productUsageDetail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productUsageDetailRepository.count();
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

    protected ProductUsageDetail getPersistedProductUsageDetail(ProductUsageDetail productUsageDetail) {
        return productUsageDetailRepository.findById(productUsageDetail.getId()).orElseThrow();
    }

    protected void assertPersistedProductUsageDetailToMatchAllProperties(ProductUsageDetail expectedProductUsageDetail) {
        assertProductUsageDetailAllPropertiesEquals(expectedProductUsageDetail, getPersistedProductUsageDetail(expectedProductUsageDetail));
    }

    protected void assertPersistedProductUsageDetailToMatchUpdatableProperties(ProductUsageDetail expectedProductUsageDetail) {
        assertProductUsageDetailAllUpdatablePropertiesEquals(
            expectedProductUsageDetail,
            getPersistedProductUsageDetail(expectedProductUsageDetail)
        );
    }
}
