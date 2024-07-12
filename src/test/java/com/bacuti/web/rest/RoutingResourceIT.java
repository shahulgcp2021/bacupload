package com.bacuti.web.rest;

import static com.bacuti.domain.RoutingAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bacuti.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.Routing;
import com.bacuti.repository.RoutingRepository;
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
 * Integration tests for the {@link RoutingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoutingResourceIT {

    private static final BigDecimal DEFAULT_STEP = new BigDecimal(1);
    private static final BigDecimal UPDATED_STEP = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DURATION = new BigDecimal(1);
    private static final BigDecimal UPDATED_DURATION = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/routings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RoutingRepository routingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoutingMockMvc;

    private Routing routing;

    private Routing insertedRouting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Routing createEntity(EntityManager em) {
        Routing routing = new Routing().step(DEFAULT_STEP).duration(DEFAULT_DURATION);
        return routing;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Routing createUpdatedEntity(EntityManager em) {
        Routing routing = new Routing().step(UPDATED_STEP).duration(UPDATED_DURATION);
        return routing;
    }

    @BeforeEach
    public void initTest() {
        routing = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedRouting != null) {
            routingRepository.delete(insertedRouting);
            insertedRouting = null;
        }
    }

    @Test
    @Transactional
    void createRouting() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Routing
        var returnedRouting = om.readValue(
            restRoutingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(routing)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Routing.class
        );

        // Validate the Routing in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRoutingUpdatableFieldsEquals(returnedRouting, getPersistedRouting(returnedRouting));

        insertedRouting = returnedRouting;
    }

    @Test
    @Transactional
    void createRoutingWithExistingId() throws Exception {
        // Create the Routing with an existing ID
        routing.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoutingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(routing)))
            .andExpect(status().isBadRequest());

        // Validate the Routing in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRoutings() throws Exception {
        // Initialize the database
        insertedRouting = routingRepository.saveAndFlush(routing);

        // Get all the routingList
        restRoutingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(routing.getId().intValue())))
            .andExpect(jsonPath("$.[*].step").value(hasItem(sameNumber(DEFAULT_STEP))))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(sameNumber(DEFAULT_DURATION))));
    }

    @Test
    @Transactional
    void getRouting() throws Exception {
        // Initialize the database
        insertedRouting = routingRepository.saveAndFlush(routing);

        // Get the routing
        restRoutingMockMvc
            .perform(get(ENTITY_API_URL_ID, routing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(routing.getId().intValue()))
            .andExpect(jsonPath("$.step").value(sameNumber(DEFAULT_STEP)))
            .andExpect(jsonPath("$.duration").value(sameNumber(DEFAULT_DURATION)));
    }

    @Test
    @Transactional
    void getNonExistingRouting() throws Exception {
        // Get the routing
        restRoutingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRouting() throws Exception {
        // Initialize the database
        insertedRouting = routingRepository.saveAndFlush(routing);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the routing
        Routing updatedRouting = routingRepository.findById(routing.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRouting are not directly saved in db
        em.detach(updatedRouting);
        updatedRouting.step(UPDATED_STEP).duration(UPDATED_DURATION);

        restRoutingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRouting.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRouting))
            )
            .andExpect(status().isOk());

        // Validate the Routing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRoutingToMatchAllProperties(updatedRouting);
    }

    @Test
    @Transactional
    void putNonExistingRouting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        routing.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoutingMockMvc
            .perform(put(ENTITY_API_URL_ID, routing.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(routing)))
            .andExpect(status().isBadRequest());

        // Validate the Routing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRouting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        routing.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoutingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(routing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Routing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRouting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        routing.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoutingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(routing)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Routing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoutingWithPatch() throws Exception {
        // Initialize the database
        insertedRouting = routingRepository.saveAndFlush(routing);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the routing using partial update
        Routing partialUpdatedRouting = new Routing();
        partialUpdatedRouting.setId(routing.getId());

        restRoutingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouting.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRouting))
            )
            .andExpect(status().isOk());

        // Validate the Routing in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoutingUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRouting, routing), getPersistedRouting(routing));
    }

    @Test
    @Transactional
    void fullUpdateRoutingWithPatch() throws Exception {
        // Initialize the database
        insertedRouting = routingRepository.saveAndFlush(routing);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the routing using partial update
        Routing partialUpdatedRouting = new Routing();
        partialUpdatedRouting.setId(routing.getId());

        partialUpdatedRouting.step(UPDATED_STEP).duration(UPDATED_DURATION);

        restRoutingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouting.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRouting))
            )
            .andExpect(status().isOk());

        // Validate the Routing in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoutingUpdatableFieldsEquals(partialUpdatedRouting, getPersistedRouting(partialUpdatedRouting));
    }

    @Test
    @Transactional
    void patchNonExistingRouting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        routing.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoutingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, routing.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(routing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Routing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRouting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        routing.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoutingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(routing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Routing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRouting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        routing.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoutingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(routing)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Routing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRouting() throws Exception {
        // Initialize the database
        insertedRouting = routingRepository.saveAndFlush(routing);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the routing
        restRoutingMockMvc
            .perform(delete(ENTITY_API_URL_ID, routing.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return routingRepository.count();
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

    protected Routing getPersistedRouting(Routing routing) {
        return routingRepository.findById(routing.getId()).orElseThrow();
    }

    protected void assertPersistedRoutingToMatchAllProperties(Routing expectedRouting) {
        assertRoutingAllPropertiesEquals(expectedRouting, getPersistedRouting(expectedRouting));
    }

    protected void assertPersistedRoutingToMatchUpdatableProperties(Routing expectedRouting) {
        assertRoutingAllUpdatablePropertiesEquals(expectedRouting, getPersistedRouting(expectedRouting));
    }
}
