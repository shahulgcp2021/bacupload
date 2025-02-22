package com.bacuti.domain;

import static com.bacuti.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class ItemAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertItemAllPropertiesEquals(Item expected, Item actual) {
        assertItemAutoGeneratedPropertiesEquals(expected, actual);
        assertItemAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertItemAllUpdatablePropertiesEquals(Item expected, Item actual) {
        assertItemUpdatableFieldsEquals(expected, actual);
        assertItemUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertItemAutoGeneratedPropertiesEquals(Item expected, Item actual) {
        assertThat(expected)
            .as("Verify Item auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertItemUpdatableFieldsEquals(Item expected, Item actual) {
        assertThat(expected)
            .as("Verify Item relevant properties")
            .satisfies(e -> assertThat(e.getItemName()).as("check itemName").isEqualTo(actual.getItemName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getItemType()).as("check itemType").isEqualTo(actual.getItemType()))
            .satisfies(e -> assertThat(e.getItemCategory()).as("check itemCategory").isEqualTo(actual.getItemCategory()))
            .satisfies(e -> assertThat(e.getPurchasedItem()).as("check purchasedItem").isEqualTo(actual.getPurchasedItem()))
            .satisfies(e -> assertThat(e.getCbamImpacted()).as("check cbamImpacted").isEqualTo(actual.getCbamImpacted()))
            .satisfies(e -> assertThat(e.getCnCode()).as("check cnCode").isEqualTo(actual.getCnCode()))
            .satisfies(e -> assertThat(e.getCnName()).as("check cnName").isEqualTo(actual.getCnName()))
            .satisfies(
                e ->
                    assertThat(e.getPercentMn()).as("check percentMn").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getPercentMn())
            )
            .satisfies(
                e ->
                    assertThat(e.getPercentCr()).as("check percentCr").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getPercentCr())
            )
            .satisfies(
                e ->
                    assertThat(e.getPercentNi()).as("check percentNi").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getPercentNi())
            )
            .satisfies(
                e ->
                    assertThat(e.getPercentCarbon())
                        .as("check percentCarbon")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getPercentCarbon())
            )
            .satisfies(
                e ->
                    assertThat(e.getPercentOtherAlloys())
                        .as("check percentOtherAlloys")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getPercentOtherAlloys())
            )
            .satisfies(
                e ->
                    assertThat(e.getPercentOtherMaterials())
                        .as("check percentOtherMaterials")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getPercentOtherMaterials())
            )
            .satisfies(
                e ->
                    assertThat(e.getPercentPreconsumerScrap())
                        .as("check percentPreconsumerScrap")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getPercentPreconsumerScrap())
            )
            .satisfies(
                e ->
                    assertThat(e.getScrapPerItem())
                        .as("check scrapPerItem")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getScrapPerItem())
            )
            .satisfies(
                e ->
                    assertThat(e.getAggregatedGoodsCategory())
                        .as("check aggregatedGoodsCategory")
                        .isEqualTo(actual.getAggregatedGoodsCategory())
            )
            .satisfies(
                e -> assertThat(e.getEfUnits()).as("check efUnits").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getEfUnits())
            )
            .satisfies(
                e ->
                    assertThat(e.getEfScalingFactor())
                        .as("check efScalingFactor")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getEfScalingFactor())
            )
            .satisfies(
                e ->
                    assertThat(e.getSupplierEmissionMultipler())
                        .as("check supplierEmissionMultipler")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getSupplierEmissionMultipler())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertItemUpdatableRelationshipsEquals(Item expected, Item actual) {
        assertThat(expected)
            .as("Verify Item relationships")
            .satisfies(e -> assertThat(e.getCompany()).as("check company").isEqualTo(actual.getCompany()))
            .satisfies(e -> assertThat(e.getDefaultAverageEF()).as("check defaultAverageEF").isEqualTo(actual.getDefaultAverageEF()))
            .satisfies(e -> assertThat(e.getUnitOfMeasure()).as("check unitOfMeasure").isEqualTo(actual.getUnitOfMeasure()));
    }
}
