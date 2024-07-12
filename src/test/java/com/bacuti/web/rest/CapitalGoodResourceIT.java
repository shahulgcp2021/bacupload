package com.bacuti.web.rest;

import static com.bacuti.domain.CapitalGoodAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bacuti.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.CapitalGood;
import com.bacuti.repository.CapitalGoodRepository;
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
 * Integration tests for the {@link CapitalGoodResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CapitalGoodResourceIT {

    private static final String DEFAULT_ASSET_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPLIER = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PURCHASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PURCHASE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_PURCHASE_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PURCHASE_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_USEFUL_LIFE = new BigDecimal(1);
    private static final BigDecimal UPDATED_USEFUL_LIFE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INTENSITY_UNITS = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTENSITY_UNITS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SCALING_FACTOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_SCALING_FACTOR = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/capital-goods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CapitalGoodRepository capitalGoodRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCapitalGoodMockMvc;

    private CapitalGood capitalGood;

    private CapitalGood insertedCapitalGood;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CapitalGood createEntity(EntityManager em) {
        CapitalGood capitalGood = new CapitalGood()
            .assetName(DEFAULT_ASSET_NAME)
            .description(DEFAULT_DESCRIPTION)
            .supplier(DEFAULT_SUPPLIER)
            .purchaseDate(DEFAULT_PURCHASE_DATE)
            .purchasePrice(DEFAULT_PURCHASE_PRICE)
            .usefulLife(DEFAULT_USEFUL_LIFE)
            .intensityUnits(DEFAULT_INTENSITY_UNITS)
            .scalingFactor(DEFAULT_SCALING_FACTOR);
        return capitalGood;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CapitalGood createUpdatedEntity(EntityManager em) {
        CapitalGood capitalGood = new CapitalGood()
            .assetName(UPDATED_ASSET_NAME)
            .description(UPDATED_DESCRIPTION)
            .supplier(UPDATED_SUPPLIER)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .usefulLife(UPDATED_USEFUL_LIFE)
            .intensityUnits(UPDATED_INTENSITY_UNITS)
            .scalingFactor(UPDATED_SCALING_FACTOR);
        return capitalGood;
    }

    @BeforeEach
    public void initTest() {
        capitalGood = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCapitalGood != null) {
            capitalGoodRepository.delete(insertedCapitalGood);
            insertedCapitalGood = null;
        }
    }

    @Test
    @Transactional
    void createCapitalGood() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CapitalGood
        var returnedCapitalGood = om.readValue(
            restCapitalGoodMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(capitalGood)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CapitalGood.class
        );

        // Validate the CapitalGood in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCapitalGoodUpdatableFieldsEquals(returnedCapitalGood, getPersistedCapitalGood(returnedCapitalGood));

        insertedCapitalGood = returnedCapitalGood;
    }

    @Test
    @Transactional
    void createCapitalGoodWithExistingId() throws Exception {
        // Create the CapitalGood with an existing ID
        capitalGood.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCapitalGoodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(capitalGood)))
            .andExpect(status().isBadRequest());

        // Validate the CapitalGood in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCapitalGoods() throws Exception {
        // Initialize the database
        insertedCapitalGood = capitalGoodRepository.saveAndFlush(capitalGood);

        // Get all the capitalGoodList
        restCapitalGoodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(capitalGood.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetName").value(hasItem(DEFAULT_ASSET_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].supplier").value(hasItem(DEFAULT_SUPPLIER)))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].purchasePrice").value(hasItem(sameNumber(DEFAULT_PURCHASE_PRICE))))
            .andExpect(jsonPath("$.[*].usefulLife").value(hasItem(sameNumber(DEFAULT_USEFUL_LIFE))))
            .andExpect(jsonPath("$.[*].intensityUnits").value(hasItem(sameNumber(DEFAULT_INTENSITY_UNITS))))
            .andExpect(jsonPath("$.[*].scalingFactor").value(hasItem(sameNumber(DEFAULT_SCALING_FACTOR))));
    }

    @Test
    @Transactional
    void getCapitalGood() throws Exception {
        // Initialize the database
        insertedCapitalGood = capitalGoodRepository.saveAndFlush(capitalGood);

        // Get the capitalGood
        restCapitalGoodMockMvc
            .perform(get(ENTITY_API_URL_ID, capitalGood.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(capitalGood.getId().intValue()))
            .andExpect(jsonPath("$.assetName").value(DEFAULT_ASSET_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.supplier").value(DEFAULT_SUPPLIER))
            .andExpect(jsonPath("$.purchaseDate").value(DEFAULT_PURCHASE_DATE.toString()))
            .andExpect(jsonPath("$.purchasePrice").value(sameNumber(DEFAULT_PURCHASE_PRICE)))
            .andExpect(jsonPath("$.usefulLife").value(sameNumber(DEFAULT_USEFUL_LIFE)))
            .andExpect(jsonPath("$.intensityUnits").value(sameNumber(DEFAULT_INTENSITY_UNITS)))
            .andExpect(jsonPath("$.scalingFactor").value(sameNumber(DEFAULT_SCALING_FACTOR)));
    }

    @Test
    @Transactional
    void getNonExistingCapitalGood() throws Exception {
        // Get the capitalGood
        restCapitalGoodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCapitalGood() throws Exception {
        // Initialize the database
        insertedCapitalGood = capitalGoodRepository.saveAndFlush(capitalGood);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the capitalGood
        CapitalGood updatedCapitalGood = capitalGoodRepository.findById(capitalGood.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCapitalGood are not directly saved in db
        em.detach(updatedCapitalGood);
        updatedCapitalGood
            .assetName(UPDATED_ASSET_NAME)
            .description(UPDATED_DESCRIPTION)
            .supplier(UPDATED_SUPPLIER)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .usefulLife(UPDATED_USEFUL_LIFE)
            .intensityUnits(UPDATED_INTENSITY_UNITS)
            .scalingFactor(UPDATED_SCALING_FACTOR);

        restCapitalGoodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCapitalGood.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCapitalGood))
            )
            .andExpect(status().isOk());

        // Validate the CapitalGood in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCapitalGoodToMatchAllProperties(updatedCapitalGood);
    }

    @Test
    @Transactional
    void putNonExistingCapitalGood() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        capitalGood.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCapitalGoodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, capitalGood.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(capitalGood))
            )
            .andExpect(status().isBadRequest());

        // Validate the CapitalGood in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCapitalGood() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        capitalGood.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapitalGoodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(capitalGood))
            )
            .andExpect(status().isBadRequest());

        // Validate the CapitalGood in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCapitalGood() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        capitalGood.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapitalGoodMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(capitalGood)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CapitalGood in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCapitalGoodWithPatch() throws Exception {
        // Initialize the database
        insertedCapitalGood = capitalGoodRepository.saveAndFlush(capitalGood);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the capitalGood using partial update
        CapitalGood partialUpdatedCapitalGood = new CapitalGood();
        partialUpdatedCapitalGood.setId(capitalGood.getId());

        partialUpdatedCapitalGood
            .description(UPDATED_DESCRIPTION)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .usefulLife(UPDATED_USEFUL_LIFE)
            .intensityUnits(UPDATED_INTENSITY_UNITS);

        restCapitalGoodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCapitalGood.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCapitalGood))
            )
            .andExpect(status().isOk());

        // Validate the CapitalGood in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCapitalGoodUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCapitalGood, capitalGood),
            getPersistedCapitalGood(capitalGood)
        );
    }

    @Test
    @Transactional
    void fullUpdateCapitalGoodWithPatch() throws Exception {
        // Initialize the database
        insertedCapitalGood = capitalGoodRepository.saveAndFlush(capitalGood);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the capitalGood using partial update
        CapitalGood partialUpdatedCapitalGood = new CapitalGood();
        partialUpdatedCapitalGood.setId(capitalGood.getId());

        partialUpdatedCapitalGood
            .assetName(UPDATED_ASSET_NAME)
            .description(UPDATED_DESCRIPTION)
            .supplier(UPDATED_SUPPLIER)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .usefulLife(UPDATED_USEFUL_LIFE)
            .intensityUnits(UPDATED_INTENSITY_UNITS)
            .scalingFactor(UPDATED_SCALING_FACTOR);

        restCapitalGoodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCapitalGood.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCapitalGood))
            )
            .andExpect(status().isOk());

        // Validate the CapitalGood in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCapitalGoodUpdatableFieldsEquals(partialUpdatedCapitalGood, getPersistedCapitalGood(partialUpdatedCapitalGood));
    }

    @Test
    @Transactional
    void patchNonExistingCapitalGood() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        capitalGood.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCapitalGoodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, capitalGood.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(capitalGood))
            )
            .andExpect(status().isBadRequest());

        // Validate the CapitalGood in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCapitalGood() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        capitalGood.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapitalGoodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(capitalGood))
            )
            .andExpect(status().isBadRequest());

        // Validate the CapitalGood in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCapitalGood() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        capitalGood.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapitalGoodMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(capitalGood)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CapitalGood in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCapitalGood() throws Exception {
        // Initialize the database
        insertedCapitalGood = capitalGoodRepository.saveAndFlush(capitalGood);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the capitalGood
        restCapitalGoodMockMvc
            .perform(delete(ENTITY_API_URL_ID, capitalGood.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return capitalGoodRepository.count();
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

    protected CapitalGood getPersistedCapitalGood(CapitalGood capitalGood) {
        return capitalGoodRepository.findById(capitalGood.getId()).orElseThrow();
    }

    protected void assertPersistedCapitalGoodToMatchAllProperties(CapitalGood expectedCapitalGood) {
        assertCapitalGoodAllPropertiesEquals(expectedCapitalGood, getPersistedCapitalGood(expectedCapitalGood));
    }

    protected void assertPersistedCapitalGoodToMatchUpdatableProperties(CapitalGood expectedCapitalGood) {
        assertCapitalGoodAllUpdatablePropertiesEquals(expectedCapitalGood, getPersistedCapitalGood(expectedCapitalGood));
    }
}
