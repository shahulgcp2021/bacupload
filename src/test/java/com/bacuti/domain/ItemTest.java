package com.bacuti.domain;

import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.DefaultAverageEFTestSamples.*;
import static com.bacuti.domain.ItemShipmentTestSamples.*;
import static com.bacuti.domain.ItemSupplierTestSamples.*;
import static com.bacuti.domain.ItemTestSamples.*;
import static com.bacuti.domain.PurchasedQtyTestSamples.*;
import static com.bacuti.domain.UnitOfMeasureTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Item.class);
        Item item1 = getItemSample1();
        Item item2 = new Item();
        assertThat(item1).isNotEqualTo(item2);

        item2.setId(item1.getId());
        assertThat(item1).isEqualTo(item2);

        item2 = getItemSample2();
        assertThat(item1).isNotEqualTo(item2);
    }

    @Test
    void companyTest() {
        Item item = getItemRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        item.setCompany(companyBack);
        assertThat(item.getCompany()).isEqualTo(companyBack);

        item.company(null);
        assertThat(item.getCompany()).isNull();
    }

    @Test
    void defaultAverageEFTest() {
        Item item = getItemRandomSampleGenerator();
        DefaultAverageEF defaultAverageEFBack = getDefaultAverageEFRandomSampleGenerator();

        item.setDefaultAverageEF(defaultAverageEFBack);
        assertThat(item.getDefaultAverageEF()).isEqualTo(defaultAverageEFBack);

        item.defaultAverageEF(null);
        assertThat(item.getDefaultAverageEF()).isNull();
    }

    @Test
    void unitOfMeasureTest() {
        Item item = getItemRandomSampleGenerator();
        UnitOfMeasure unitOfMeasureBack = getUnitOfMeasureRandomSampleGenerator();

        item.setUnitOfMeasure(unitOfMeasureBack);
        assertThat(item.getUnitOfMeasure()).isEqualTo(unitOfMeasureBack);

        item.unitOfMeasure(null);
        assertThat(item.getUnitOfMeasure()).isNull();
    }

    @Test
    void itemShipmentTest() {
        Item item = getItemRandomSampleGenerator();
        ItemShipment itemShipmentBack = getItemShipmentRandomSampleGenerator();

        item.addItemShipment(itemShipmentBack);
        assertThat(item.getItemShipments()).containsOnly(itemShipmentBack);
        assertThat(itemShipmentBack.getItem()).isEqualTo(item);

        item.removeItemShipment(itemShipmentBack);
        assertThat(item.getItemShipments()).doesNotContain(itemShipmentBack);
        assertThat(itemShipmentBack.getItem()).isNull();

        item.itemShipments(new HashSet<>(Set.of(itemShipmentBack)));
        assertThat(item.getItemShipments()).containsOnly(itemShipmentBack);
        assertThat(itemShipmentBack.getItem()).isEqualTo(item);

        item.setItemShipments(new HashSet<>());
        assertThat(item.getItemShipments()).doesNotContain(itemShipmentBack);
        assertThat(itemShipmentBack.getItem()).isNull();
    }

    @Test
    void itemSupplierTest() {
        Item item = getItemRandomSampleGenerator();
        ItemSupplier itemSupplierBack = getItemSupplierRandomSampleGenerator();

        item.addItemSupplier(itemSupplierBack);
        assertThat(item.getItemSuppliers()).containsOnly(itemSupplierBack);
        assertThat(itemSupplierBack.getItem()).isEqualTo(item);

        item.removeItemSupplier(itemSupplierBack);
        assertThat(item.getItemSuppliers()).doesNotContain(itemSupplierBack);
        assertThat(itemSupplierBack.getItem()).isNull();

        item.itemSuppliers(new HashSet<>(Set.of(itemSupplierBack)));
        assertThat(item.getItemSuppliers()).containsOnly(itemSupplierBack);
        assertThat(itemSupplierBack.getItem()).isEqualTo(item);

        item.setItemSuppliers(new HashSet<>());
        assertThat(item.getItemSuppliers()).doesNotContain(itemSupplierBack);
        assertThat(itemSupplierBack.getItem()).isNull();
    }

    @Test
    void purchasedQtyTest() {
        Item item = getItemRandomSampleGenerator();
        PurchasedQty purchasedQtyBack = getPurchasedQtyRandomSampleGenerator();

        item.addPurchasedQty(purchasedQtyBack);
        assertThat(item.getPurchasedQties()).containsOnly(purchasedQtyBack);
        assertThat(purchasedQtyBack.getItem()).isEqualTo(item);

        item.removePurchasedQty(purchasedQtyBack);
        assertThat(item.getPurchasedQties()).doesNotContain(purchasedQtyBack);
        assertThat(purchasedQtyBack.getItem()).isNull();

        item.purchasedQties(new HashSet<>(Set.of(purchasedQtyBack)));
        assertThat(item.getPurchasedQties()).containsOnly(purchasedQtyBack);
        assertThat(purchasedQtyBack.getItem()).isEqualTo(item);

        item.setPurchasedQties(new HashSet<>());
        assertThat(item.getPurchasedQties()).doesNotContain(purchasedQtyBack);
        assertThat(purchasedQtyBack.getItem()).isNull();
    }
}
