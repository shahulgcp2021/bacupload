package com.bacuti.domain;

import static com.bacuti.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class ItemShipmentAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertItemShipmentAllPropertiesEquals(ItemShipment expected, ItemShipment actual) {
        assertItemShipmentAutoGeneratedPropertiesEquals(expected, actual);
        assertItemShipmentAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertItemShipmentAllUpdatablePropertiesEquals(ItemShipment expected, ItemShipment actual) {
        assertItemShipmentUpdatableFieldsEquals(expected, actual);
        assertItemShipmentUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertItemShipmentAutoGeneratedPropertiesEquals(ItemShipment expected, ItemShipment actual) {
        assertThat(expected)
            .as("Verify ItemShipment auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertItemShipmentUpdatableFieldsEquals(ItemShipment expected, ItemShipment actual) {
        assertThat(expected)
            .as("Verify ItemShipment relevant properties")
            .satisfies(e -> assertThat(e.getShipmentdate()).as("check shipmentdate").isEqualTo(actual.getShipmentdate()))
            .satisfies(e -> assertThat(e.getShipper()).as("check shipper").isEqualTo(actual.getShipper()))
            .satisfies(e -> assertThat(e.getUpstream()).as("check upstream").isEqualTo(actual.getUpstream()))
            .satisfies(
                e ->
                    assertThat(e.getQuantityShipped())
                        .as("check quantityShipped")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getQuantityShipped())
            )
            .satisfies(
                e ->
                    assertThat(e.getWeightInKg())
                        .as("check weightInKg")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getWeightInKg())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertItemShipmentUpdatableRelationshipsEquals(ItemShipment expected, ItemShipment actual) {
        assertThat(expected)
            .as("Verify ItemShipment relationships")
            .satisfies(e -> assertThat(e.getCompany()).as("check company").isEqualTo(actual.getCompany()))
            .satisfies(e -> assertThat(e.getItem()).as("check item").isEqualTo(actual.getItem()))
            .satisfies(e -> assertThat(e.getShipmentLane()).as("check shipmentLane").isEqualTo(actual.getShipmentLane()))
            .satisfies(e -> assertThat(e.getSite()).as("check site").isEqualTo(actual.getSite()));
    }
}
