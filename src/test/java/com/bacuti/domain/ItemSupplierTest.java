package com.bacuti.domain;

import static com.bacuti.domain.CompanyPublicEmissionTestSamples.*;
import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.ItemSupplierTestSamples.*;
import static com.bacuti.domain.ItemTestSamples.*;
import static com.bacuti.domain.PurchasedQtyTestSamples.*;
import static com.bacuti.domain.SupplierTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ItemSupplierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemSupplier.class);
        ItemSupplier itemSupplier1 = getItemSupplierSample1();
        ItemSupplier itemSupplier2 = new ItemSupplier();
        assertThat(itemSupplier1).isNotEqualTo(itemSupplier2);

        itemSupplier2.setId(itemSupplier1.getId());
        assertThat(itemSupplier1).isEqualTo(itemSupplier2);

        itemSupplier2 = getItemSupplierSample2();
        assertThat(itemSupplier1).isNotEqualTo(itemSupplier2);
    }

    @Test
    void companyTest() {
        ItemSupplier itemSupplier = getItemSupplierRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        itemSupplier.setCompany(companyBack);
        assertThat(itemSupplier.getCompany()).isEqualTo(companyBack);

        itemSupplier.company(null);
        assertThat(itemSupplier.getCompany()).isNull();
    }

    @Test
    void itemTest() {
        ItemSupplier itemSupplier = getItemSupplierRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        itemSupplier.setItem(itemBack);
        assertThat(itemSupplier.getItem()).isEqualTo(itemBack);

        itemSupplier.item(null);
        assertThat(itemSupplier.getItem()).isNull();
    }

    @Test
    void supplierTest() {
        ItemSupplier itemSupplier = getItemSupplierRandomSampleGenerator();
        Supplier supplierBack = getSupplierRandomSampleGenerator();

        itemSupplier.setSupplier(supplierBack);
        assertThat(itemSupplier.getSupplier()).isEqualTo(supplierBack);

        itemSupplier.supplier(null);
        assertThat(itemSupplier.getSupplier()).isNull();
    }

    @Test
    void companyPublicEmissionTest() {
        ItemSupplier itemSupplier = getItemSupplierRandomSampleGenerator();
        CompanyPublicEmission companyPublicEmissionBack = getCompanyPublicEmissionRandomSampleGenerator();

        itemSupplier.setCompanyPublicEmission(companyPublicEmissionBack);
        assertThat(itemSupplier.getCompanyPublicEmission()).isEqualTo(companyPublicEmissionBack);

        itemSupplier.companyPublicEmission(null);
        assertThat(itemSupplier.getCompanyPublicEmission()).isNull();
    }

    @Test
    void purchasedQtyTest() {
        ItemSupplier itemSupplier = getItemSupplierRandomSampleGenerator();
        PurchasedQty purchasedQtyBack = getPurchasedQtyRandomSampleGenerator();

        itemSupplier.addPurchasedQty(purchasedQtyBack);
        assertThat(itemSupplier.getPurchasedQties()).containsOnly(purchasedQtyBack);
        assertThat(purchasedQtyBack.getItemSupplier()).isEqualTo(itemSupplier);

        itemSupplier.removePurchasedQty(purchasedQtyBack);
        assertThat(itemSupplier.getPurchasedQties()).doesNotContain(purchasedQtyBack);
        assertThat(purchasedQtyBack.getItemSupplier()).isNull();

        itemSupplier.purchasedQties(new HashSet<>(Set.of(purchasedQtyBack)));
        assertThat(itemSupplier.getPurchasedQties()).containsOnly(purchasedQtyBack);
        assertThat(purchasedQtyBack.getItemSupplier()).isEqualTo(itemSupplier);

        itemSupplier.setPurchasedQties(new HashSet<>());
        assertThat(itemSupplier.getPurchasedQties()).doesNotContain(purchasedQtyBack);
        assertThat(purchasedQtyBack.getItemSupplier()).isNull();
    }
}
