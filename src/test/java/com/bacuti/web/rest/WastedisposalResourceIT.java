package com.bacuti.web.rest;

import static com.bacuti.domain.WastedisposalAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bacuti.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.Wastedisposal;
import com.bacuti.domain.enumeration.DisposalMethod;
import com.bacuti.domain.enumeration.Stage;
import com.bacuti.repository.WastedisposalRepository;
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
 * Integration tests for the {@link WastedisposalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WastedisposalResourceIT {

    private static final Stage DEFAULT_STAGE = Stage.S1;
    private static final Stage UPDATED_STAGE = Stage.S2;

    private static final String DEFAULT_WASTE_COMPONENT = "AAAAAAAAAA";
    private static final String UPDATED_WASTE_COMPONENT = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);

    private static final DisposalMethod DEFAULT_DISPOSAL_METHOD = DisposalMethod.M1;
    private static final DisposalMethod UPDATED_DISPOSAL_METHOD = DisposalMethod.M2;

    private static final String ENTITY_API_URL = "/api/wastedisposals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WastedisposalRepository wastedisposalRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWastedisposalMockMvc;

    private Wastedisposal wastedisposal;

    private Wastedisposal insertedWastedisposal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wastedisposal createEntity(EntityManager em) {
        Wastedisposal wastedisposal = new Wastedisposal()
            .stage(DEFAULT_STAGE)
            .wasteComponent(DEFAULT_WASTE_COMPONENT)
            .quantity(DEFAULT_QUANTITY)
            .disposalMethod(DEFAULT_DISPOSAL_METHOD);
        return wastedisposal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wastedisposal createUpdatedEntity(EntityManager em) {
        Wastedisposal wastedisposal = new Wastedisposal()
            .stage(UPDATED_STAGE)
            .wasteComponent(UPDATED_WASTE_COMPONENT)
            .quantity(UPDATED_QUANTITY)
            .disposalMethod(UPDATED_DISPOSAL_METHOD);
        return wastedisposal;
    }

    @BeforeEach
    public void initTest() {
        wastedisposal = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedWastedisposal != null) {
            wastedisposalRepository.delete(insertedWastedisposal);
            insertedWastedisposal = null;
        }
    }

    @Test
    @Transactional
    void createWastedisposal() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Wastedisposal
        var returnedWastedisposal = om.readValue(
            restWastedisposalMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wastedisposal)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Wastedisposal.class
        );

        // Validate the Wastedisposal in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertWastedisposalUpdatableFieldsEquals(returnedWastedisposal, getPersistedWastedisposal(returnedWastedisposal));

        insertedWastedisposal = returnedWastedisposal;
    }

    @Test
    @Transactional
    void createWastedisposalWithExistingId() throws Exception {
        // Create the Wastedisposal with an existing ID
        wastedisposal.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWastedisposalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wastedisposal)))
            .andExpect(status().isBadRequest());

        // Validate the Wastedisposal in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWastedisposals() throws Exception {
        // Initialize the database
        insertedWastedisposal = wastedisposalRepository.saveAndFlush(wastedisposal);

        // Get all the wastedisposalList
        restWastedisposalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wastedisposal.getId().intValue())))
            .andExpect(jsonPath("$.[*].stage").value(hasItem(DEFAULT_STAGE.toString())))
            .andExpect(jsonPath("$.[*].wasteComponent").value(hasItem(DEFAULT_WASTE_COMPONENT)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].disposalMethod").value(hasItem(DEFAULT_DISPOSAL_METHOD.toString())));
    }

    @Test
    @Transactional
    void getWastedisposal() throws Exception {
        // Initialize the database
        insertedWastedisposal = wastedisposalRepository.saveAndFlush(wastedisposal);

        // Get the wastedisposal
        restWastedisposalMockMvc
            .perform(get(ENTITY_API_URL_ID, wastedisposal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wastedisposal.getId().intValue()))
            .andExpect(jsonPath("$.stage").value(DEFAULT_STAGE.toString()))
            .andExpect(jsonPath("$.wasteComponent").value(DEFAULT_WASTE_COMPONENT))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.disposalMethod").value(DEFAULT_DISPOSAL_METHOD.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWastedisposal() throws Exception {
        // Get the wastedisposal
        restWastedisposalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWastedisposal() throws Exception {
        // Initialize the database
        insertedWastedisposal = wastedisposalRepository.saveAndFlush(wastedisposal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wastedisposal
        Wastedisposal updatedWastedisposal = wastedisposalRepository.findById(wastedisposal.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWastedisposal are not directly saved in db
        em.detach(updatedWastedisposal);
        updatedWastedisposal
            .stage(UPDATED_STAGE)
            .wasteComponent(UPDATED_WASTE_COMPONENT)
            .quantity(UPDATED_QUANTITY)
            .disposalMethod(UPDATED_DISPOSAL_METHOD);

        restWastedisposalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWastedisposal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedWastedisposal))
            )
            .andExpect(status().isOk());

        // Validate the Wastedisposal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWastedisposalToMatchAllProperties(updatedWastedisposal);
    }

    @Test
    @Transactional
    void putNonExistingWastedisposal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wastedisposal.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWastedisposalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wastedisposal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(wastedisposal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wastedisposal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWastedisposal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wastedisposal.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWastedisposalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(wastedisposal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wastedisposal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWastedisposal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wastedisposal.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWastedisposalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wastedisposal)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wastedisposal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWastedisposalWithPatch() throws Exception {
        // Initialize the database
        insertedWastedisposal = wastedisposalRepository.saveAndFlush(wastedisposal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wastedisposal using partial update
        Wastedisposal partialUpdatedWastedisposal = new Wastedisposal();
        partialUpdatedWastedisposal.setId(wastedisposal.getId());

        partialUpdatedWastedisposal.stage(UPDATED_STAGE).wasteComponent(UPDATED_WASTE_COMPONENT);

        restWastedisposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWastedisposal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWastedisposal))
            )
            .andExpect(status().isOk());

        // Validate the Wastedisposal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWastedisposalUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWastedisposal, wastedisposal),
            getPersistedWastedisposal(wastedisposal)
        );
    }

    @Test
    @Transactional
    void fullUpdateWastedisposalWithPatch() throws Exception {
        // Initialize the database
        insertedWastedisposal = wastedisposalRepository.saveAndFlush(wastedisposal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wastedisposal using partial update
        Wastedisposal partialUpdatedWastedisposal = new Wastedisposal();
        partialUpdatedWastedisposal.setId(wastedisposal.getId());

        partialUpdatedWastedisposal
            .stage(UPDATED_STAGE)
            .wasteComponent(UPDATED_WASTE_COMPONENT)
            .quantity(UPDATED_QUANTITY)
            .disposalMethod(UPDATED_DISPOSAL_METHOD);

        restWastedisposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWastedisposal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWastedisposal))
            )
            .andExpect(status().isOk());

        // Validate the Wastedisposal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWastedisposalUpdatableFieldsEquals(partialUpdatedWastedisposal, getPersistedWastedisposal(partialUpdatedWastedisposal));
    }

    @Test
    @Transactional
    void patchNonExistingWastedisposal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wastedisposal.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWastedisposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wastedisposal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(wastedisposal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wastedisposal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWastedisposal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wastedisposal.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWastedisposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(wastedisposal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wastedisposal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWastedisposal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wastedisposal.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWastedisposalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(wastedisposal)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wastedisposal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWastedisposal() throws Exception {
        // Initialize the database
        insertedWastedisposal = wastedisposalRepository.saveAndFlush(wastedisposal);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the wastedisposal
        restWastedisposalMockMvc
            .perform(delete(ENTITY_API_URL_ID, wastedisposal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return wastedisposalRepository.count();
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

    protected Wastedisposal getPersistedWastedisposal(Wastedisposal wastedisposal) {
        return wastedisposalRepository.findById(wastedisposal.getId()).orElseThrow();
    }

    protected void assertPersistedWastedisposalToMatchAllProperties(Wastedisposal expectedWastedisposal) {
        assertWastedisposalAllPropertiesEquals(expectedWastedisposal, getPersistedWastedisposal(expectedWastedisposal));
    }

    protected void assertPersistedWastedisposalToMatchUpdatableProperties(Wastedisposal expectedWastedisposal) {
        assertWastedisposalAllUpdatablePropertiesEquals(expectedWastedisposal, getPersistedWastedisposal(expectedWastedisposal));
    }
}
