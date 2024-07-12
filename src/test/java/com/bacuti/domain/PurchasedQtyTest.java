package com.bacuti.domain;

import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.ItemSupplierTestSamples.*;
import static com.bacuti.domain.ItemTestSamples.*;
import static com.bacuti.domain.PurchasedQtyTestSamples.*;
import static com.bacuti.domain.UnitOfMeasureTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PurchasedQtyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchasedQty.class);
        PurchasedQty purchasedQty1 = getPurchasedQtySample1();
        PurchasedQty purchasedQty2 = new PurchasedQty();
        assertThat(purchasedQty1).isNotEqualTo(purchasedQty2);

        purchasedQty2.setId(purchasedQty1.getId());
        assertThat(purchasedQty1).isEqualTo(purchasedQty2);

        purchasedQty2 = getPurchasedQtySample2();
        assertThat(purchasedQty1).isNotEqualTo(purchasedQty2);
    }

    @Test
    void companyTest() {
        PurchasedQty purchasedQty = getPurchasedQtyRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        purchasedQty.setCompany(companyBack);
        assertThat(purchasedQty.getCompany()).isEqualTo(companyBack);

        purchasedQty.company(null);
        assertThat(purchasedQty.getCompany()).isNull();
    }

    @Test
    void itemTest() {
        PurchasedQty purchasedQty = getPurchasedQtyRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        purchasedQty.setItem(itemBack);
        assertThat(purchasedQty.getItem()).isEqualTo(itemBack);

        purchasedQty.item(null);
        assertThat(purchasedQty.getItem()).isNull();
    }

    @Test
    void itemSupplierTest() {
        PurchasedQty purchasedQty = getPurchasedQtyRandomSampleGenerator();
        ItemSupplier itemSupplierBack = getItemSupplierRandomSampleGenerator();

        purchasedQty.setItemSupplier(itemSupplierBack);
        assertThat(purchasedQty.getItemSupplier()).isEqualTo(itemSupplierBack);

        purchasedQty.itemSupplier(null);
        assertThat(purchasedQty.getItemSupplier()).isNull();
    }

    @Test
    void unitOfMeasureTest() {
        PurchasedQty purchasedQty = getPurchasedQtyRandomSampleGenerator();
        UnitOfMeasure unitOfMeasureBack = getUnitOfMeasureRandomSampleGenerator();

        purchasedQty.setUnitOfMeasure(unitOfMeasureBack);
        assertThat(purchasedQty.getUnitOfMeasure()).isEqualTo(unitOfMeasureBack);

        purchasedQty.unitOfMeasure(null);
        assertThat(purchasedQty.getUnitOfMeasure()).isNull();
    }
}
