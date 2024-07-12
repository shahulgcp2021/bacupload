package com.bacuti.web.rest;

import static com.bacuti.domain.CarbonPricePaymentAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bacuti.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.CarbonPricePayment;
import com.bacuti.domain.enumeration.Currency;
import com.bacuti.repository.CarbonPricePaymentRepository;
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
 * Integration tests for the {@link CarbonPricePaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CarbonPricePaymentResourceIT {

    private static final LocalDate DEFAULT_PAYMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Currency DEFAULT_CURRENCY = Currency.USD;
    private static final Currency UPDATED_CURRENCY = Currency.EUR;

    private static final BigDecimal DEFAULT_EUR_FX_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_EUR_FX_RATE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_AMOUNT_IN_EUR = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT_IN_EUR = new BigDecimal(2);

    private static final LocalDate DEFAULT_EMISSION_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EMISSION_FROM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EMISSION_TO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EMISSION_TO_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_FORM_OF_CARBON_PRICE = "AAAAAAAAAA";
    private static final String UPDATED_FORM_OF_CARBON_PRICE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PERCENT_EMISSION_BY_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PERCENT_EMISSION_BY_PRICE = new BigDecimal(2);

    private static final String DEFAULT_FORM_OF_REBATE = "AAAAAAAAAA";
    private static final String UPDATED_FORM_OF_REBATE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PERCENT_EMISSION_BY_REBATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PERCENT_EMISSION_BY_REBATE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/carbon-price-payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CarbonPricePaymentRepository carbonPricePaymentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarbonPricePaymentMockMvc;

    private CarbonPricePayment carbonPricePayment;

    private CarbonPricePayment insertedCarbonPricePayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarbonPricePayment createEntity(EntityManager em) {
        CarbonPricePayment carbonPricePayment = new CarbonPricePayment()
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .amount(DEFAULT_AMOUNT)
            .currency(DEFAULT_CURRENCY)
            .eurFxRate(DEFAULT_EUR_FX_RATE)
            .amountInEur(DEFAULT_AMOUNT_IN_EUR)
            .emissionFromDate(DEFAULT_EMISSION_FROM_DATE)
            .emissionToDate(DEFAULT_EMISSION_TO_DATE)
            .formOfCarbonPrice(DEFAULT_FORM_OF_CARBON_PRICE)
            .percentEmissionByPrice(DEFAULT_PERCENT_EMISSION_BY_PRICE)
            .formOfRebate(DEFAULT_FORM_OF_REBATE)
            .percentEmissionByRebate(DEFAULT_PERCENT_EMISSION_BY_REBATE);
        return carbonPricePayment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarbonPricePayment createUpdatedEntity(EntityManager em) {
        CarbonPricePayment carbonPricePayment = new CarbonPricePayment()
            .paymentDate(UPDATED_PAYMENT_DATE)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .eurFxRate(UPDATED_EUR_FX_RATE)
            .amountInEur(UPDATED_AMOUNT_IN_EUR)
            .emissionFromDate(UPDATED_EMISSION_FROM_DATE)
            .emissionToDate(UPDATED_EMISSION_TO_DATE)
            .formOfCarbonPrice(UPDATED_FORM_OF_CARBON_PRICE)
            .percentEmissionByPrice(UPDATED_PERCENT_EMISSION_BY_PRICE)
            .formOfRebate(UPDATED_FORM_OF_REBATE)
            .percentEmissionByRebate(UPDATED_PERCENT_EMISSION_BY_REBATE);
        return carbonPricePayment;
    }

    @BeforeEach
    public void initTest() {
        carbonPricePayment = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCarbonPricePayment != null) {
            carbonPricePaymentRepository.delete(insertedCarbonPricePayment);
            insertedCarbonPricePayment = null;
        }
    }

    @Test
    @Transactional
    void createCarbonPricePayment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CarbonPricePayment
        var returnedCarbonPricePayment = om.readValue(
            restCarbonPricePaymentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carbonPricePayment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CarbonPricePayment.class
        );

        // Validate the CarbonPricePayment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCarbonPricePaymentUpdatableFieldsEquals(
            returnedCarbonPricePayment,
            getPersistedCarbonPricePayment(returnedCarbonPricePayment)
        );

        insertedCarbonPricePayment = returnedCarbonPricePayment;
    }

    @Test
    @Transactional
    void createCarbonPricePaymentWithExistingId() throws Exception {
        // Create the CarbonPricePayment with an existing ID
        carbonPricePayment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarbonPricePaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carbonPricePayment)))
            .andExpect(status().isBadRequest());

        // Validate the CarbonPricePayment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCarbonPricePayments() throws Exception {
        // Initialize the database
        insertedCarbonPricePayment = carbonPricePaymentRepository.saveAndFlush(carbonPricePayment);

        // Get all the carbonPricePaymentList
        restCarbonPricePaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carbonPricePayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].eurFxRate").value(hasItem(sameNumber(DEFAULT_EUR_FX_RATE))))
            .andExpect(jsonPath("$.[*].amountInEur").value(hasItem(sameNumber(DEFAULT_AMOUNT_IN_EUR))))
            .andExpect(jsonPath("$.[*].emissionFromDate").value(hasItem(DEFAULT_EMISSION_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].emissionToDate").value(hasItem(DEFAULT_EMISSION_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].formOfCarbonPrice").value(hasItem(DEFAULT_FORM_OF_CARBON_PRICE)))
            .andExpect(jsonPath("$.[*].percentEmissionByPrice").value(hasItem(sameNumber(DEFAULT_PERCENT_EMISSION_BY_PRICE))))
            .andExpect(jsonPath("$.[*].formOfRebate").value(hasItem(DEFAULT_FORM_OF_REBATE)))
            .andExpect(jsonPath("$.[*].percentEmissionByRebate").value(hasItem(sameNumber(DEFAULT_PERCENT_EMISSION_BY_REBATE))));
    }

    @Test
    @Transactional
    void getCarbonPricePayment() throws Exception {
        // Initialize the database
        insertedCarbonPricePayment = carbonPricePaymentRepository.saveAndFlush(carbonPricePayment);

        // Get the carbonPricePayment
        restCarbonPricePaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, carbonPricePayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carbonPricePayment.getId().intValue()))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.eurFxRate").value(sameNumber(DEFAULT_EUR_FX_RATE)))
            .andExpect(jsonPath("$.amountInEur").value(sameNumber(DEFAULT_AMOUNT_IN_EUR)))
            .andExpect(jsonPath("$.emissionFromDate").value(DEFAULT_EMISSION_FROM_DATE.toString()))
            .andExpect(jsonPath("$.emissionToDate").value(DEFAULT_EMISSION_TO_DATE.toString()))
            .andExpect(jsonPath("$.formOfCarbonPrice").value(DEFAULT_FORM_OF_CARBON_PRICE))
            .andExpect(jsonPath("$.percentEmissionByPrice").value(sameNumber(DEFAULT_PERCENT_EMISSION_BY_PRICE)))
            .andExpect(jsonPath("$.formOfRebate").value(DEFAULT_FORM_OF_REBATE))
            .andExpect(jsonPath("$.percentEmissionByRebate").value(sameNumber(DEFAULT_PERCENT_EMISSION_BY_REBATE)));
    }

    @Test
    @Transactional
    void getNonExistingCarbonPricePayment() throws Exception {
        // Get the carbonPricePayment
        restCarbonPricePaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCarbonPricePayment() throws Exception {
        // Initialize the database
        insertedCarbonPricePayment = carbonPricePaymentRepository.saveAndFlush(carbonPricePayment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carbonPricePayment
        CarbonPricePayment updatedCarbonPricePayment = carbonPricePaymentRepository.findById(carbonPricePayment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCarbonPricePayment are not directly saved in db
        em.detach(updatedCarbonPricePayment);
        updatedCarbonPricePayment
            .paymentDate(UPDATED_PAYMENT_DATE)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .eurFxRate(UPDATED_EUR_FX_RATE)
            .amountInEur(UPDATED_AMOUNT_IN_EUR)
            .emissionFromDate(UPDATED_EMISSION_FROM_DATE)
            .emissionToDate(UPDATED_EMISSION_TO_DATE)
            .formOfCarbonPrice(UPDATED_FORM_OF_CARBON_PRICE)
            .percentEmissionByPrice(UPDATED_PERCENT_EMISSION_BY_PRICE)
            .formOfRebate(UPDATED_FORM_OF_REBATE)
            .percentEmissionByRebate(UPDATED_PERCENT_EMISSION_BY_REBATE);

        restCarbonPricePaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCarbonPricePayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCarbonPricePayment))
            )
            .andExpect(status().isOk());

        // Validate the CarbonPricePayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCarbonPricePaymentToMatchAllProperties(updatedCarbonPricePayment);
    }

    @Test
    @Transactional
    void putNonExistingCarbonPricePayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carbonPricePayment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarbonPricePaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carbonPricePayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(carbonPricePayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarbonPricePayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCarbonPricePayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carbonPricePayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarbonPricePaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(carbonPricePayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarbonPricePayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCarbonPricePayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carbonPricePayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarbonPricePaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carbonPricePayment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarbonPricePayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarbonPricePaymentWithPatch() throws Exception {
        // Initialize the database
        insertedCarbonPricePayment = carbonPricePaymentRepository.saveAndFlush(carbonPricePayment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carbonPricePayment using partial update
        CarbonPricePayment partialUpdatedCarbonPricePayment = new CarbonPricePayment();
        partialUpdatedCarbonPricePayment.setId(carbonPricePayment.getId());

        partialUpdatedCarbonPricePayment
            .eurFxRate(UPDATED_EUR_FX_RATE)
            .emissionToDate(UPDATED_EMISSION_TO_DATE)
            .formOfCarbonPrice(UPDATED_FORM_OF_CARBON_PRICE)
            .formOfRebate(UPDATED_FORM_OF_REBATE);

        restCarbonPricePaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarbonPricePayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCarbonPricePayment))
            )
            .andExpect(status().isOk());

        // Validate the CarbonPricePayment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarbonPricePaymentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCarbonPricePayment, carbonPricePayment),
            getPersistedCarbonPricePayment(carbonPricePayment)
        );
    }

    @Test
    @Transactional
    void fullUpdateCarbonPricePaymentWithPatch() throws Exception {
        // Initialize the database
        insertedCarbonPricePayment = carbonPricePaymentRepository.saveAndFlush(carbonPricePayment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carbonPricePayment using partial update
        CarbonPricePayment partialUpdatedCarbonPricePayment = new CarbonPricePayment();
        partialUpdatedCarbonPricePayment.setId(carbonPricePayment.getId());

        partialUpdatedCarbonPricePayment
            .paymentDate(UPDATED_PAYMENT_DATE)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .eurFxRate(UPDATED_EUR_FX_RATE)
            .amountInEur(UPDATED_AMOUNT_IN_EUR)
            .emissionFromDate(UPDATED_EMISSION_FROM_DATE)
            .emissionToDate(UPDATED_EMISSION_TO_DATE)
            .formOfCarbonPrice(UPDATED_FORM_OF_CARBON_PRICE)
            .percentEmissionByPrice(UPDATED_PERCENT_EMISSION_BY_PRICE)
            .formOfRebate(UPDATED_FORM_OF_REBATE)
            .percentEmissionByRebate(UPDATED_PERCENT_EMISSION_BY_REBATE);

        restCarbonPricePaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarbonPricePayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCarbonPricePayment))
            )
            .andExpect(status().isOk());

        // Validate the CarbonPricePayment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarbonPricePaymentUpdatableFieldsEquals(
            partialUpdatedCarbonPricePayment,
            getPersistedCarbonPricePayment(partialUpdatedCarbonPricePayment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCarbonPricePayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carbonPricePayment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarbonPricePaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carbonPricePayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(carbonPricePayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarbonPricePayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCarbonPricePayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carbonPricePayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarbonPricePaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(carbonPricePayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarbonPricePayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCarbonPricePayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carbonPricePayment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarbonPricePaymentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(carbonPricePayment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarbonPricePayment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCarbonPricePayment() throws Exception {
        // Initialize the database
        insertedCarbonPricePayment = carbonPricePaymentRepository.saveAndFlush(carbonPricePayment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the carbonPricePayment
        restCarbonPricePaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, carbonPricePayment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return carbonPricePaymentRepository.count();
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

    protected CarbonPricePayment getPersistedCarbonPricePayment(CarbonPricePayment carbonPricePayment) {
        return carbonPricePaymentRepository.findById(carbonPricePayment.getId()).orElseThrow();
    }

    protected void assertPersistedCarbonPricePaymentToMatchAllProperties(CarbonPricePayment expectedCarbonPricePayment) {
        assertCarbonPricePaymentAllPropertiesEquals(expectedCarbonPricePayment, getPersistedCarbonPricePayment(expectedCarbonPricePayment));
    }

    protected void assertPersistedCarbonPricePaymentToMatchUpdatableProperties(CarbonPricePayment expectedCarbonPricePayment) {
        assertCarbonPricePaymentAllUpdatablePropertiesEquals(
            expectedCarbonPricePayment,
            getPersistedCarbonPricePayment(expectedCarbonPricePayment)
        );
    }
}
