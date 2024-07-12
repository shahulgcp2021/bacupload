package com.bacuti.domain;

import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.ItemSupplierTestSamples.*;
import static com.bacuti.domain.SupplierTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SupplierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Supplier.class);
        Supplier supplier1 = getSupplierSample1();
        Supplier supplier2 = new Supplier();
        assertThat(supplier1).isNotEqualTo(supplier2);

        supplier2.setId(supplier1.getId());
        assertThat(supplier1).isEqualTo(supplier2);

        supplier2 = getSupplierSample2();
        assertThat(supplier1).isNotEqualTo(supplier2);
    }

    @Test
    void companyTest() {
        Supplier supplier = getSupplierRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        supplier.setCompany(companyBack);
        assertThat(supplier.getCompany()).isEqualTo(companyBack);

        supplier.company(null);
        assertThat(supplier.getCompany()).isNull();
    }

    @Test
    void itemSupplierTest() {
        Supplier supplier = getSupplierRandomSampleGenerator();
        ItemSupplier itemSupplierBack = getItemSupplierRandomSampleGenerator();

        supplier.addItemSupplier(itemSupplierBack);
        assertThat(supplier.getItemSuppliers()).containsOnly(itemSupplierBack);
        assertThat(itemSupplierBack.getSupplier()).isEqualTo(supplier);

        supplier.removeItemSupplier(itemSupplierBack);
        assertThat(supplier.getItemSuppliers()).doesNotContain(itemSupplierBack);
        assertThat(itemSupplierBack.getSupplier()).isNull();

        supplier.itemSuppliers(new HashSet<>(Set.of(itemSupplierBack)));
        assertThat(supplier.getItemSuppliers()).containsOnly(itemSupplierBack);
        assertThat(itemSupplierBack.getSupplier()).isEqualTo(supplier);

        supplier.setItemSuppliers(new HashSet<>());
        assertThat(supplier.getItemSuppliers()).doesNotContain(itemSupplierBack);
        assertThat(itemSupplierBack.getSupplier()).isNull();
    }
}
