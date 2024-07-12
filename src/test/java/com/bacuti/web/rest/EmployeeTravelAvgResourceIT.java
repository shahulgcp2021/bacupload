package com.bacuti.web.rest;

import static com.bacuti.domain.EmployeeTravelAvgAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bacuti.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.EmployeeTravelAvg;
import com.bacuti.domain.enumeration.TravelMode;
import com.bacuti.domain.enumeration.TravelType;
import com.bacuti.repository.EmployeeTravelAvgRepository;
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
 * Integration tests for the {@link EmployeeTravelAvgResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeTravelAvgResourceIT {

    private static final TravelType DEFAULT_TRAVEL_TYPE = TravelType.TYPE1;
    private static final TravelType UPDATED_TRAVEL_TYPE = TravelType.TYPE2;

    private static final TravelMode DEFAULT_TRAVEL_MODE = TravelMode.MODE1;
    private static final TravelMode UPDATED_TRAVEL_MODE = TravelMode.MODE2;

    private static final LocalDate DEFAULT_PERIOD_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PERIOD_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_TO = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_PCT_EMPLOYEES = new BigDecimal(1);
    private static final BigDecimal UPDATED_PCT_EMPLOYEES = new BigDecimal(2);

    private static final BigDecimal DEFAULT_COMMUTE_DAYS_PER_WEEK = new BigDecimal(1);
    private static final BigDecimal UPDATED_COMMUTE_DAYS_PER_WEEK = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PCT_DAYS_TRAVELLED = new BigDecimal(1);
    private static final BigDecimal UPDATED_PCT_DAYS_TRAVELLED = new BigDecimal(2);

    private static final BigDecimal DEFAULT_AVG_TRIPS_IN_PERIOD = new BigDecimal(1);
    private static final BigDecimal UPDATED_AVG_TRIPS_IN_PERIOD = new BigDecimal(2);

    private static final BigDecimal DEFAULT_AVG_TRAVEL_DISTANCE_IN_KM = new BigDecimal(1);
    private static final BigDecimal UPDATED_AVG_TRAVEL_DISTANCE_IN_KM = new BigDecimal(2);

    private static final BigDecimal DEFAULT_AVG_HOTEL_STAY_DAYS_PER_TRIP = new BigDecimal(1);
    private static final BigDecimal UPDATED_AVG_HOTEL_STAY_DAYS_PER_TRIP = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/employee-travel-avgs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployeeTravelAvgRepository employeeTravelAvgRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeTravelAvgMockMvc;

    private EmployeeTravelAvg employeeTravelAvg;

    private EmployeeTravelAvg insertedEmployeeTravelAvg;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeTravelAvg createEntity(EntityManager em) {
        EmployeeTravelAvg employeeTravelAvg = new EmployeeTravelAvg()
            .travelType(DEFAULT_TRAVEL_TYPE)
            .travelMode(DEFAULT_TRAVEL_MODE)
            .periodFrom(DEFAULT_PERIOD_FROM)
            .periodTo(DEFAULT_PERIOD_TO)
            .pctEmployees(DEFAULT_PCT_EMPLOYEES)
            .commuteDaysPerWeek(DEFAULT_COMMUTE_DAYS_PER_WEEK)
            .pctDaysTravelled(DEFAULT_PCT_DAYS_TRAVELLED)
            .avgTripsInPeriod(DEFAULT_AVG_TRIPS_IN_PERIOD)
            .avgTravelDistanceInKm(DEFAULT_AVG_TRAVEL_DISTANCE_IN_KM)
            .avgHotelStayDaysPerTrip(DEFAULT_AVG_HOTEL_STAY_DAYS_PER_TRIP);
        return employeeTravelAvg;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeTravelAvg createUpdatedEntity(EntityManager em) {
        EmployeeTravelAvg employeeTravelAvg = new EmployeeTravelAvg()
            .travelType(UPDATED_TRAVEL_TYPE)
            .travelMode(UPDATED_TRAVEL_MODE)
            .periodFrom(UPDATED_PERIOD_FROM)
            .periodTo(UPDATED_PERIOD_TO)
            .pctEmployees(UPDATED_PCT_EMPLOYEES)
            .commuteDaysPerWeek(UPDATED_COMMUTE_DAYS_PER_WEEK)
            .pctDaysTravelled(UPDATED_PCT_DAYS_TRAVELLED)
            .avgTripsInPeriod(UPDATED_AVG_TRIPS_IN_PERIOD)
            .avgTravelDistanceInKm(UPDATED_AVG_TRAVEL_DISTANCE_IN_KM)
            .avgHotelStayDaysPerTrip(UPDATED_AVG_HOTEL_STAY_DAYS_PER_TRIP);
        return employeeTravelAvg;
    }

    @BeforeEach
    public void initTest() {
        employeeTravelAvg = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedEmployeeTravelAvg != null) {
            employeeTravelAvgRepository.delete(insertedEmployeeTravelAvg);
            insertedEmployeeTravelAvg = null;
        }
    }

    @Test
    @Transactional
    void createEmployeeTravelAvg() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmployeeTravelAvg
        var returnedEmployeeTravelAvg = om.readValue(
            restEmployeeTravelAvgMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeTravelAvg)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmployeeTravelAvg.class
        );

        // Validate the EmployeeTravelAvg in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmployeeTravelAvgUpdatableFieldsEquals(returnedEmployeeTravelAvg, getPersistedEmployeeTravelAvg(returnedEmployeeTravelAvg));

        insertedEmployeeTravelAvg = returnedEmployeeTravelAvg;
    }

    @Test
    @Transactional
    void createEmployeeTravelAvgWithExistingId() throws Exception {
        // Create the EmployeeTravelAvg with an existing ID
        employeeTravelAvg.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeTravelAvgMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeTravelAvg)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeTravelAvg in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmployeeTravelAvgs() throws Exception {
        // Initialize the database
        insertedEmployeeTravelAvg = employeeTravelAvgRepository.saveAndFlush(employeeTravelAvg);

        // Get all the employeeTravelAvgList
        restEmployeeTravelAvgMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeTravelAvg.getId().intValue())))
            .andExpect(jsonPath("$.[*].travelType").value(hasItem(DEFAULT_TRAVEL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].travelMode").value(hasItem(DEFAULT_TRAVEL_MODE.toString())))
            .andExpect(jsonPath("$.[*].periodFrom").value(hasItem(DEFAULT_PERIOD_FROM.toString())))
            .andExpect(jsonPath("$.[*].periodTo").value(hasItem(DEFAULT_PERIOD_TO.toString())))
            .andExpect(jsonPath("$.[*].pctEmployees").value(hasItem(sameNumber(DEFAULT_PCT_EMPLOYEES))))
            .andExpect(jsonPath("$.[*].commuteDaysPerWeek").value(hasItem(sameNumber(DEFAULT_COMMUTE_DAYS_PER_WEEK))))
            .andExpect(jsonPath("$.[*].pctDaysTravelled").value(hasItem(sameNumber(DEFAULT_PCT_DAYS_TRAVELLED))))
            .andExpect(jsonPath("$.[*].avgTripsInPeriod").value(hasItem(sameNumber(DEFAULT_AVG_TRIPS_IN_PERIOD))))
            .andExpect(jsonPath("$.[*].avgTravelDistanceInKm").value(hasItem(sameNumber(DEFAULT_AVG_TRAVEL_DISTANCE_IN_KM))))
            .andExpect(jsonPath("$.[*].avgHotelStayDaysPerTrip").value(hasItem(sameNumber(DEFAULT_AVG_HOTEL_STAY_DAYS_PER_TRIP))));
    }

    @Test
    @Transactional
    void getEmployeeTravelAvg() throws Exception {
        // Initialize the database
        insertedEmployeeTravelAvg = employeeTravelAvgRepository.saveAndFlush(employeeTravelAvg);

        // Get the employeeTravelAvg
        restEmployeeTravelAvgMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeTravelAvg.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeTravelAvg.getId().intValue()))
            .andExpect(jsonPath("$.travelType").value(DEFAULT_TRAVEL_TYPE.toString()))
            .andExpect(jsonPath("$.travelMode").value(DEFAULT_TRAVEL_MODE.toString()))
            .andExpect(jsonPath("$.periodFrom").value(DEFAULT_PERIOD_FROM.toString()))
            .andExpect(jsonPath("$.periodTo").value(DEFAULT_PERIOD_TO.toString()))
            .andExpect(jsonPath("$.pctEmployees").value(sameNumber(DEFAULT_PCT_EMPLOYEES)))
            .andExpect(jsonPath("$.commuteDaysPerWeek").value(sameNumber(DEFAULT_COMMUTE_DAYS_PER_WEEK)))
            .andExpect(jsonPath("$.pctDaysTravelled").value(sameNumber(DEFAULT_PCT_DAYS_TRAVELLED)))
            .andExpect(jsonPath("$.avgTripsInPeriod").value(sameNumber(DEFAULT_AVG_TRIPS_IN_PERIOD)))
            .andExpect(jsonPath("$.avgTravelDistanceInKm").value(sameNumber(DEFAULT_AVG_TRAVEL_DISTANCE_IN_KM)))
            .andExpect(jsonPath("$.avgHotelStayDaysPerTrip").value(sameNumber(DEFAULT_AVG_HOTEL_STAY_DAYS_PER_TRIP)));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeTravelAvg() throws Exception {
        // Get the employeeTravelAvg
        restEmployeeTravelAvgMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeTravelAvg() throws Exception {
        // Initialize the database
        insertedEmployeeTravelAvg = employeeTravelAvgRepository.saveAndFlush(employeeTravelAvg);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeTravelAvg
        EmployeeTravelAvg updatedEmployeeTravelAvg = employeeTravelAvgRepository.findById(employeeTravelAvg.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmployeeTravelAvg are not directly saved in db
        em.detach(updatedEmployeeTravelAvg);
        updatedEmployeeTravelAvg
            .travelType(UPDATED_TRAVEL_TYPE)
            .travelMode(UPDATED_TRAVEL_MODE)
            .periodFrom(UPDATED_PERIOD_FROM)
            .periodTo(UPDATED_PERIOD_TO)
            .pctEmployees(UPDATED_PCT_EMPLOYEES)
            .commuteDaysPerWeek(UPDATED_COMMUTE_DAYS_PER_WEEK)
            .pctDaysTravelled(UPDATED_PCT_DAYS_TRAVELLED)
            .avgTripsInPeriod(UPDATED_AVG_TRIPS_IN_PERIOD)
            .avgTravelDistanceInKm(UPDATED_AVG_TRAVEL_DISTANCE_IN_KM)
            .avgHotelStayDaysPerTrip(UPDATED_AVG_HOTEL_STAY_DAYS_PER_TRIP);

        restEmployeeTravelAvgMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployeeTravelAvg.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEmployeeTravelAvg))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeTravelAvg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployeeTravelAvgToMatchAllProperties(updatedEmployeeTravelAvg);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeTravelAvg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeTravelAvg.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeTravelAvgMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeTravelAvg.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeTravelAvg))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeTravelAvg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeTravelAvg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeTravelAvg.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeTravelAvgMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeTravelAvg))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeTravelAvg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeTravelAvg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeTravelAvg.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeTravelAvgMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeTravelAvg)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeTravelAvg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeTravelAvgWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeTravelAvg = employeeTravelAvgRepository.saveAndFlush(employeeTravelAvg);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeTravelAvg using partial update
        EmployeeTravelAvg partialUpdatedEmployeeTravelAvg = new EmployeeTravelAvg();
        partialUpdatedEmployeeTravelAvg.setId(employeeTravelAvg.getId());

        partialUpdatedEmployeeTravelAvg
            .travelType(UPDATED_TRAVEL_TYPE)
            .periodFrom(UPDATED_PERIOD_FROM)
            .avgTravelDistanceInKm(UPDATED_AVG_TRAVEL_DISTANCE_IN_KM);

        restEmployeeTravelAvgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeTravelAvg.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeTravelAvg))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeTravelAvg in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeTravelAvgUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmployeeTravelAvg, employeeTravelAvg),
            getPersistedEmployeeTravelAvg(employeeTravelAvg)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmployeeTravelAvgWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeTravelAvg = employeeTravelAvgRepository.saveAndFlush(employeeTravelAvg);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeTravelAvg using partial update
        EmployeeTravelAvg partialUpdatedEmployeeTravelAvg = new EmployeeTravelAvg();
        partialUpdatedEmployeeTravelAvg.setId(employeeTravelAvg.getId());

        partialUpdatedEmployeeTravelAvg
            .travelType(UPDATED_TRAVEL_TYPE)
            .travelMode(UPDATED_TRAVEL_MODE)
            .periodFrom(UPDATED_PERIOD_FROM)
            .periodTo(UPDATED_PERIOD_TO)
            .pctEmployees(UPDATED_PCT_EMPLOYEES)
            .commuteDaysPerWeek(UPDATED_COMMUTE_DAYS_PER_WEEK)
            .pctDaysTravelled(UPDATED_PCT_DAYS_TRAVELLED)
            .avgTripsInPeriod(UPDATED_AVG_TRIPS_IN_PERIOD)
            .avgTravelDistanceInKm(UPDATED_AVG_TRAVEL_DISTANCE_IN_KM)
            .avgHotelStayDaysPerTrip(UPDATED_AVG_HOTEL_STAY_DAYS_PER_TRIP);

        restEmployeeTravelAvgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeTravelAvg.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeTravelAvg))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeTravelAvg in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeTravelAvgUpdatableFieldsEquals(
            partialUpdatedEmployeeTravelAvg,
            getPersistedEmployeeTravelAvg(partialUpdatedEmployeeTravelAvg)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeTravelAvg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeTravelAvg.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeTravelAvgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeTravelAvg.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeTravelAvg))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeTravelAvg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeTravelAvg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeTravelAvg.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeTravelAvgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeTravelAvg))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeTravelAvg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeTravelAvg() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeTravelAvg.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeTravelAvgMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employeeTravelAvg)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeTravelAvg in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeTravelAvg() throws Exception {
        // Initialize the database
        insertedEmployeeTravelAvg = employeeTravelAvgRepository.saveAndFlush(employeeTravelAvg);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employeeTravelAvg
        restEmployeeTravelAvgMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeTravelAvg.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employeeTravelAvgRepository.count();
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

    protected EmployeeTravelAvg getPersistedEmployeeTravelAvg(EmployeeTravelAvg employeeTravelAvg) {
        return employeeTravelAvgRepository.findById(employeeTravelAvg.getId()).orElseThrow();
    }

    protected void assertPersistedEmployeeTravelAvgToMatchAllProperties(EmployeeTravelAvg expectedEmployeeTravelAvg) {
        assertEmployeeTravelAvgAllPropertiesEquals(expectedEmployeeTravelAvg, getPersistedEmployeeTravelAvg(expectedEmployeeTravelAvg));
    }

    protected void assertPersistedEmployeeTravelAvgToMatchUpdatableProperties(EmployeeTravelAvg expectedEmployeeTravelAvg) {
        assertEmployeeTravelAvgAllUpdatablePropertiesEquals(
            expectedEmployeeTravelAvg,
            getPersistedEmployeeTravelAvg(expectedEmployeeTravelAvg)
        );
    }
}
