package com.bacuti.web.rest;

import static com.bacuti.domain.ItemAsserts.*;
import static com.bacuti.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bacuti.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bacuti.IntegrationTest;
import com.bacuti.domain.Item;
import com.bacuti.domain.enumeration.AggregatedGoodsCategory;
import com.bacuti.domain.enumeration.ItemType;
import com.bacuti.repository.ItemRepository;
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
 * Integration tests for the {@link ItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemResourceIT {

    private static final String DEFAULT_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ItemType DEFAULT_ITEM_TYPE = ItemType.FINISHED_GOOD;
    private static final ItemType UPDATED_ITEM_TYPE = ItemType.SUB_ASSEMBLY;

    private static final String DEFAULT_ITEM_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_CATEGORY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PURCHASED_ITEM = false;
    private static final Boolean UPDATED_PURCHASED_ITEM = true;

    private static final Boolean DEFAULT_CBAM_IMPACTED = false;
    private static final Boolean UPDATED_CBAM_IMPACTED = true;

    private static final String DEFAULT_CN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CN_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CN_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PERCENT_MN = new BigDecimal(1);
    private static final BigDecimal UPDATED_PERCENT_MN = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PERCENT_CR = new BigDecimal(1);
    private static final BigDecimal UPDATED_PERCENT_CR = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PERCENT_NI = new BigDecimal(1);
    private static final BigDecimal UPDATED_PERCENT_NI = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PERCENT_CARBON = new BigDecimal(1);
    private static final BigDecimal UPDATED_PERCENT_CARBON = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PERCENT_OTHER_ALLOYS = new BigDecimal(1);
    private static final BigDecimal UPDATED_PERCENT_OTHER_ALLOYS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PERCENT_OTHER_MATERIALS = new BigDecimal(1);
    private static final BigDecimal UPDATED_PERCENT_OTHER_MATERIALS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PERCENT_PRECONSUMER_SCRAP = new BigDecimal(1);
    private static final BigDecimal UPDATED_PERCENT_PRECONSUMER_SCRAP = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SCRAP_PER_ITEM = new BigDecimal(1);
    private static final BigDecimal UPDATED_SCRAP_PER_ITEM = new BigDecimal(2);

    private static final AggregatedGoodsCategory DEFAULT_AGGREGATED_GOODS_CATEGORY = AggregatedGoodsCategory.CATEG1;
    private static final AggregatedGoodsCategory UPDATED_AGGREGATED_GOODS_CATEGORY = AggregatedGoodsCategory.CATEG2;

    private static final BigDecimal DEFAULT_EF_UNITS = new BigDecimal(1);
    private static final BigDecimal UPDATED_EF_UNITS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_EF_SCALING_FACTOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_EF_SCALING_FACTOR = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SUPPLIER_EMISSION_MULTIPLER = new BigDecimal(1);
    private static final BigDecimal UPDATED_SUPPLIER_EMISSION_MULTIPLER = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemMockMvc;

    private Item item;

    private Item insertedItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createEntity(EntityManager em) {
        Item item = new Item()
            .itemName(DEFAULT_ITEM_NAME)
            .description(DEFAULT_DESCRIPTION)
            .itemType(DEFAULT_ITEM_TYPE)
            .itemCategory(DEFAULT_ITEM_CATEGORY)
            .purchasedItem(DEFAULT_PURCHASED_ITEM)
            .cbamImpacted(DEFAULT_CBAM_IMPACTED)
            .cnCode(DEFAULT_CN_CODE)
            .cnName(DEFAULT_CN_NAME)
            .percentMn(DEFAULT_PERCENT_MN)
            .percentCr(DEFAULT_PERCENT_CR)
            .percentNi(DEFAULT_PERCENT_NI)
            .percentCarbon(DEFAULT_PERCENT_CARBON)
            .percentOtherAlloys(DEFAULT_PERCENT_OTHER_ALLOYS)
            .percentOtherMaterials(DEFAULT_PERCENT_OTHER_MATERIALS)
            .percentPreconsumerScrap(DEFAULT_PERCENT_PRECONSUMER_SCRAP)
            .scrapPerItem(DEFAULT_SCRAP_PER_ITEM)
            .aggregatedGoodsCategory(DEFAULT_AGGREGATED_GOODS_CATEGORY)
            .efUnits(DEFAULT_EF_UNITS)
            .efScalingFactor(DEFAULT_EF_SCALING_FACTOR)
            .supplierEmissionMultipler(DEFAULT_SUPPLIER_EMISSION_MULTIPLER);
        return item;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createUpdatedEntity(EntityManager em) {
        Item item = new Item()
            .itemName(UPDATED_ITEM_NAME)
            .description(UPDATED_DESCRIPTION)
            .itemType(UPDATED_ITEM_TYPE)
            .itemCategory(UPDATED_ITEM_CATEGORY)
            .purchasedItem(UPDATED_PURCHASED_ITEM)
            .cbamImpacted(UPDATED_CBAM_IMPACTED)
            .cnCode(UPDATED_CN_CODE)
            .cnName(UPDATED_CN_NAME)
            .percentMn(UPDATED_PERCENT_MN)
            .percentCr(UPDATED_PERCENT_CR)
            .percentNi(UPDATED_PERCENT_NI)
            .percentCarbon(UPDATED_PERCENT_CARBON)
            .percentOtherAlloys(UPDATED_PERCENT_OTHER_ALLOYS)
            .percentOtherMaterials(UPDATED_PERCENT_OTHER_MATERIALS)
            .percentPreconsumerScrap(UPDATED_PERCENT_PRECONSUMER_SCRAP)
            .scrapPerItem(UPDATED_SCRAP_PER_ITEM)
            .aggregatedGoodsCategory(UPDATED_AGGREGATED_GOODS_CATEGORY)
            .efUnits(UPDATED_EF_UNITS)
            .efScalingFactor(UPDATED_EF_SCALING_FACTOR)
            .supplierEmissionMultipler(UPDATED_SUPPLIER_EMISSION_MULTIPLER);
        return item;
    }

    @BeforeEach
    public void initTest() {
        item = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedItem != null) {
            itemRepository.delete(insertedItem);
            insertedItem = null;
        }
    }

    @Test
    @Transactional
    void createItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Item
        var returnedItem = om.readValue(
            restItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(item)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Item.class
        );

        // Validate the Item in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertItemUpdatableFieldsEquals(returnedItem, getPersistedItem(returnedItem));

        insertedItem = returnedItem;
    }

    @Test
    @Transactional
    void createItemWithExistingId() throws Exception {
        // Create the Item with an existing ID
        item.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(item)))
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllItems() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].itemType").value(hasItem(DEFAULT_ITEM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].itemCategory").value(hasItem(DEFAULT_ITEM_CATEGORY)))
            .andExpect(jsonPath("$.[*].purchasedItem").value(hasItem(DEFAULT_PURCHASED_ITEM.booleanValue())))
            .andExpect(jsonPath("$.[*].cbamImpacted").value(hasItem(DEFAULT_CBAM_IMPACTED.booleanValue())))
            .andExpect(jsonPath("$.[*].cnCode").value(hasItem(DEFAULT_CN_CODE)))
            .andExpect(jsonPath("$.[*].cnName").value(hasItem(DEFAULT_CN_NAME)))
            .andExpect(jsonPath("$.[*].percentMn").value(hasItem(sameNumber(DEFAULT_PERCENT_MN))))
            .andExpect(jsonPath("$.[*].percentCr").value(hasItem(sameNumber(DEFAULT_PERCENT_CR))))
            .andExpect(jsonPath("$.[*].percentNi").value(hasItem(sameNumber(DEFAULT_PERCENT_NI))))
            .andExpect(jsonPath("$.[*].percentCarbon").value(hasItem(sameNumber(DEFAULT_PERCENT_CARBON))))
            .andExpect(jsonPath("$.[*].percentOtherAlloys").value(hasItem(sameNumber(DEFAULT_PERCENT_OTHER_ALLOYS))))
            .andExpect(jsonPath("$.[*].percentOtherMaterials").value(hasItem(sameNumber(DEFAULT_PERCENT_OTHER_MATERIALS))))
            .andExpect(jsonPath("$.[*].percentPreconsumerScrap").value(hasItem(sameNumber(DEFAULT_PERCENT_PRECONSUMER_SCRAP))))
            .andExpect(jsonPath("$.[*].scrapPerItem").value(hasItem(sameNumber(DEFAULT_SCRAP_PER_ITEM))))
            .andExpect(jsonPath("$.[*].aggregatedGoodsCategory").value(hasItem(DEFAULT_AGGREGATED_GOODS_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].efUnits").value(hasItem(sameNumber(DEFAULT_EF_UNITS))))
            .andExpect(jsonPath("$.[*].efScalingFactor").value(hasItem(sameNumber(DEFAULT_EF_SCALING_FACTOR))))
            .andExpect(jsonPath("$.[*].supplierEmissionMultipler").value(hasItem(sameNumber(DEFAULT_SUPPLIER_EMISSION_MULTIPLER))));
    }

    @Test
    @Transactional
    void getItem() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get the item
        restItemMockMvc
            .perform(get(ENTITY_API_URL_ID, item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(item.getId().intValue()))
            .andExpect(jsonPath("$.itemName").value(DEFAULT_ITEM_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.itemType").value(DEFAULT_ITEM_TYPE.toString()))
            .andExpect(jsonPath("$.itemCategory").value(DEFAULT_ITEM_CATEGORY))
            .andExpect(jsonPath("$.purchasedItem").value(DEFAULT_PURCHASED_ITEM.booleanValue()))
            .andExpect(jsonPath("$.cbamImpacted").value(DEFAULT_CBAM_IMPACTED.booleanValue()))
            .andExpect(jsonPath("$.cnCode").value(DEFAULT_CN_CODE))
            .andExpect(jsonPath("$.cnName").value(DEFAULT_CN_NAME))
            .andExpect(jsonPath("$.percentMn").value(sameNumber(DEFAULT_PERCENT_MN)))
            .andExpect(jsonPath("$.percentCr").value(sameNumber(DEFAULT_PERCENT_CR)))
            .andExpect(jsonPath("$.percentNi").value(sameNumber(DEFAULT_PERCENT_NI)))
            .andExpect(jsonPath("$.percentCarbon").value(sameNumber(DEFAULT_PERCENT_CARBON)))
            .andExpect(jsonPath("$.percentOtherAlloys").value(sameNumber(DEFAULT_PERCENT_OTHER_ALLOYS)))
            .andExpect(jsonPath("$.percentOtherMaterials").value(sameNumber(DEFAULT_PERCENT_OTHER_MATERIALS)))
            .andExpect(jsonPath("$.percentPreconsumerScrap").value(sameNumber(DEFAULT_PERCENT_PRECONSUMER_SCRAP)))
            .andExpect(jsonPath("$.scrapPerItem").value(sameNumber(DEFAULT_SCRAP_PER_ITEM)))
            .andExpect(jsonPath("$.aggregatedGoodsCategory").value(DEFAULT_AGGREGATED_GOODS_CATEGORY.toString()))
            .andExpect(jsonPath("$.efUnits").value(sameNumber(DEFAULT_EF_UNITS)))
            .andExpect(jsonPath("$.efScalingFactor").value(sameNumber(DEFAULT_EF_SCALING_FACTOR)))
            .andExpect(jsonPath("$.supplierEmissionMultipler").value(sameNumber(DEFAULT_SUPPLIER_EMISSION_MULTIPLER)));
    }

    @Test
    @Transactional
    void getNonExistingItem() throws Exception {
        // Get the item
        restItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingItem() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the item
        Item updatedItem = itemRepository.findById(item.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedItem are not directly saved in db
        em.detach(updatedItem);
        updatedItem
            .itemName(UPDATED_ITEM_NAME)
            .description(UPDATED_DESCRIPTION)
            .itemType(UPDATED_ITEM_TYPE)
            .itemCategory(UPDATED_ITEM_CATEGORY)
            .purchasedItem(UPDATED_PURCHASED_ITEM)
            .cbamImpacted(UPDATED_CBAM_IMPACTED)
            .cnCode(UPDATED_CN_CODE)
            .cnName(UPDATED_CN_NAME)
            .percentMn(UPDATED_PERCENT_MN)
            .percentCr(UPDATED_PERCENT_CR)
            .percentNi(UPDATED_PERCENT_NI)
            .percentCarbon(UPDATED_PERCENT_CARBON)
            .percentOtherAlloys(UPDATED_PERCENT_OTHER_ALLOYS)
            .percentOtherMaterials(UPDATED_PERCENT_OTHER_MATERIALS)
            .percentPreconsumerScrap(UPDATED_PERCENT_PRECONSUMER_SCRAP)
            .scrapPerItem(UPDATED_SCRAP_PER_ITEM)
            .aggregatedGoodsCategory(UPDATED_AGGREGATED_GOODS_CATEGORY)
            .efUnits(UPDATED_EF_UNITS)
            .efScalingFactor(UPDATED_EF_SCALING_FACTOR)
            .supplierEmissionMultipler(UPDATED_SUPPLIER_EMISSION_MULTIPLER);

        restItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedItem))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedItemToMatchAllProperties(updatedItem);
    }

    @Test
    @Transactional
    void putNonExistingItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        item.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(put(ENTITY_API_URL_ID, item.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(item)))
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        item.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(item))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        item.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(item)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemWithPatch() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem
            .description(UPDATED_DESCRIPTION)
            .itemType(UPDATED_ITEM_TYPE)
            .itemCategory(UPDATED_ITEM_CATEGORY)
            .purchasedItem(UPDATED_PURCHASED_ITEM)
            .cbamImpacted(UPDATED_CBAM_IMPACTED)
            .percentNi(UPDATED_PERCENT_NI)
            .percentCarbon(UPDATED_PERCENT_CARBON)
            .aggregatedGoodsCategory(UPDATED_AGGREGATED_GOODS_CATEGORY)
            .efScalingFactor(UPDATED_EF_SCALING_FACTOR);

        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedItem))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertItemUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedItem, item), getPersistedItem(item));
    }

    @Test
    @Transactional
    void fullUpdateItemWithPatch() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem
            .itemName(UPDATED_ITEM_NAME)
            .description(UPDATED_DESCRIPTION)
            .itemType(UPDATED_ITEM_TYPE)
            .itemCategory(UPDATED_ITEM_CATEGORY)
            .purchasedItem(UPDATED_PURCHASED_ITEM)
            .cbamImpacted(UPDATED_CBAM_IMPACTED)
            .cnCode(UPDATED_CN_CODE)
            .cnName(UPDATED_CN_NAME)
            .percentMn(UPDATED_PERCENT_MN)
            .percentCr(UPDATED_PERCENT_CR)
            .percentNi(UPDATED_PERCENT_NI)
            .percentCarbon(UPDATED_PERCENT_CARBON)
            .percentOtherAlloys(UPDATED_PERCENT_OTHER_ALLOYS)
            .percentOtherMaterials(UPDATED_PERCENT_OTHER_MATERIALS)
            .percentPreconsumerScrap(UPDATED_PERCENT_PRECONSUMER_SCRAP)
            .scrapPerItem(UPDATED_SCRAP_PER_ITEM)
            .aggregatedGoodsCategory(UPDATED_AGGREGATED_GOODS_CATEGORY)
            .efUnits(UPDATED_EF_UNITS)
            .efScalingFactor(UPDATED_EF_SCALING_FACTOR)
            .supplierEmissionMultipler(UPDATED_SUPPLIER_EMISSION_MULTIPLER);

        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedItem))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertItemUpdatableFieldsEquals(partialUpdatedItem, getPersistedItem(partialUpdatedItem));
    }

    @Test
    @Transactional
    void patchNonExistingItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        item.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(patch(ENTITY_API_URL_ID, item.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(item)))
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        item.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(item))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        item.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(item)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItem() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the item
        restItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, item.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return itemRepository.count();
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

    protected Item getPersistedItem(Item item) {
        return itemRepository.findById(item.getId()).orElseThrow();
    }

    protected void assertPersistedItemToMatchAllProperties(Item expectedItem) {
        assertItemAllPropertiesEquals(expectedItem, getPersistedItem(expectedItem));
    }

    protected void assertPersistedItemToMatchUpdatableProperties(Item expectedItem) {
        assertItemAllUpdatablePropertiesEquals(expectedItem, getPersistedItem(expectedItem));
    }
}
