package com.bacuti.domain;

import static com.bacuti.domain.BillofMaterialTestSamples.*;
import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.ItemTestSamples.*;
import static com.bacuti.domain.UnitOfMeasureTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillofMaterialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillofMaterial.class);
        BillofMaterial billofMaterial1 = getBillofMaterialSample1();
        BillofMaterial billofMaterial2 = new BillofMaterial();
        assertThat(billofMaterial1).isNotEqualTo(billofMaterial2);

        billofMaterial2.setId(billofMaterial1.getId());
        assertThat(billofMaterial1).isEqualTo(billofMaterial2);

        billofMaterial2 = getBillofMaterialSample2();
        assertThat(billofMaterial1).isNotEqualTo(billofMaterial2);
    }

    @Test
    void companyTest() {
        BillofMaterial billofMaterial = getBillofMaterialRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        billofMaterial.setCompany(companyBack);
        assertThat(billofMaterial.getCompany()).isEqualTo(companyBack);

        billofMaterial.company(null);
        assertThat(billofMaterial.getCompany()).isNull();
    }

    @Test
    void productTest() {
        BillofMaterial billofMaterial = getBillofMaterialRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        billofMaterial.setProduct(itemBack);
        assertThat(billofMaterial.getProduct()).isEqualTo(itemBack);

        billofMaterial.product(null);
        assertThat(billofMaterial.getProduct()).isNull();
    }

    @Test
    void componentTest() {
        BillofMaterial billofMaterial = getBillofMaterialRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        billofMaterial.setComponent(itemBack);
        assertThat(billofMaterial.getComponent()).isEqualTo(itemBack);

        billofMaterial.component(null);
        assertThat(billofMaterial.getComponent()).isNull();
    }

    @Test
    void unitOfMeasureTest() {
        BillofMaterial billofMaterial = getBillofMaterialRandomSampleGenerator();
        UnitOfMeasure unitOfMeasureBack = getUnitOfMeasureRandomSampleGenerator();

        billofMaterial.setUnitOfMeasure(unitOfMeasureBack);
        assertThat(billofMaterial.getUnitOfMeasure()).isEqualTo(unitOfMeasureBack);

        billofMaterial.unitOfMeasure(null);
        assertThat(billofMaterial.getUnitOfMeasure()).isNull();
    }
}
