package com.bacuti.domain;

import static com.bacuti.domain.CompanyPublicEmissionTestSamples.*;
import static com.bacuti.domain.EnergySourceTestSamples.*;
import static com.bacuti.domain.ItemSupplierTestSamples.*;
import static com.bacuti.domain.UnitOfMeasureTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CompanyPublicEmissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyPublicEmission.class);
        CompanyPublicEmission companyPublicEmission1 = getCompanyPublicEmissionSample1();
        CompanyPublicEmission companyPublicEmission2 = new CompanyPublicEmission();
        assertThat(companyPublicEmission1).isNotEqualTo(companyPublicEmission2);

        companyPublicEmission2.setId(companyPublicEmission1.getId());
        assertThat(companyPublicEmission1).isEqualTo(companyPublicEmission2);

        companyPublicEmission2 = getCompanyPublicEmissionSample2();
        assertThat(companyPublicEmission1).isNotEqualTo(companyPublicEmission2);
    }

    @Test
    void revenueUnitOfMeasureTest() {
        CompanyPublicEmission companyPublicEmission = getCompanyPublicEmissionRandomSampleGenerator();
        UnitOfMeasure unitOfMeasureBack = getUnitOfMeasureRandomSampleGenerator();

        companyPublicEmission.setRevenueUnitOfMeasure(unitOfMeasureBack);
        assertThat(companyPublicEmission.getRevenueUnitOfMeasure()).isEqualTo(unitOfMeasureBack);

        companyPublicEmission.revenueUnitOfMeasure(null);
        assertThat(companyPublicEmission.getRevenueUnitOfMeasure()).isNull();
    }

    @Test
    void emissionsUnitOfMeasureTest() {
        CompanyPublicEmission companyPublicEmission = getCompanyPublicEmissionRandomSampleGenerator();
        UnitOfMeasure unitOfMeasureBack = getUnitOfMeasureRandomSampleGenerator();

        companyPublicEmission.setEmissionsUnitOfMeasure(unitOfMeasureBack);
        assertThat(companyPublicEmission.getEmissionsUnitOfMeasure()).isEqualTo(unitOfMeasureBack);

        companyPublicEmission.emissionsUnitOfMeasure(null);
        assertThat(companyPublicEmission.getEmissionsUnitOfMeasure()).isNull();
    }

    @Test
    void emissionIntensityUnitOfMeasureTest() {
        CompanyPublicEmission companyPublicEmission = getCompanyPublicEmissionRandomSampleGenerator();
        UnitOfMeasure unitOfMeasureBack = getUnitOfMeasureRandomSampleGenerator();

        companyPublicEmission.setEmissionIntensityUnitOfMeasure(unitOfMeasureBack);
        assertThat(companyPublicEmission.getEmissionIntensityUnitOfMeasure()).isEqualTo(unitOfMeasureBack);

        companyPublicEmission.emissionIntensityUnitOfMeasure(null);
        assertThat(companyPublicEmission.getEmissionIntensityUnitOfMeasure()).isNull();
    }

    @Test
    void activitylevelUnitOfMeasureTest() {
        CompanyPublicEmission companyPublicEmission = getCompanyPublicEmissionRandomSampleGenerator();
        UnitOfMeasure unitOfMeasureBack = getUnitOfMeasureRandomSampleGenerator();

        companyPublicEmission.setActivitylevelUnitOfMeasure(unitOfMeasureBack);
        assertThat(companyPublicEmission.getActivitylevelUnitOfMeasure()).isEqualTo(unitOfMeasureBack);

        companyPublicEmission.activitylevelUnitOfMeasure(null);
        assertThat(companyPublicEmission.getActivitylevelUnitOfMeasure()).isNull();
    }

    @Test
    void energySourceTest() {
        CompanyPublicEmission companyPublicEmission = getCompanyPublicEmissionRandomSampleGenerator();
        EnergySource energySourceBack = getEnergySourceRandomSampleGenerator();

        companyPublicEmission.addEnergySource(energySourceBack);
        assertThat(companyPublicEmission.getEnergySources()).containsOnly(energySourceBack);
        assertThat(energySourceBack.getCompanyPublicEmission()).isEqualTo(companyPublicEmission);

        companyPublicEmission.removeEnergySource(energySourceBack);
        assertThat(companyPublicEmission.getEnergySources()).doesNotContain(energySourceBack);
        assertThat(energySourceBack.getCompanyPublicEmission()).isNull();

        companyPublicEmission.energySources(new HashSet<>(Set.of(energySourceBack)));
        assertThat(companyPublicEmission.getEnergySources()).containsOnly(energySourceBack);
        assertThat(energySourceBack.getCompanyPublicEmission()).isEqualTo(companyPublicEmission);

        companyPublicEmission.setEnergySources(new HashSet<>());
        assertThat(companyPublicEmission.getEnergySources()).doesNotContain(energySourceBack);
        assertThat(energySourceBack.getCompanyPublicEmission()).isNull();
    }

    @Test
    void itemSupplierTest() {
        CompanyPublicEmission companyPublicEmission = getCompanyPublicEmissionRandomSampleGenerator();
        ItemSupplier itemSupplierBack = getItemSupplierRandomSampleGenerator();

        companyPublicEmission.addItemSupplier(itemSupplierBack);
        assertThat(companyPublicEmission.getItemSuppliers()).containsOnly(itemSupplierBack);
        assertThat(itemSupplierBack.getCompanyPublicEmission()).isEqualTo(companyPublicEmission);

        companyPublicEmission.removeItemSupplier(itemSupplierBack);
        assertThat(companyPublicEmission.getItemSuppliers()).doesNotContain(itemSupplierBack);
        assertThat(itemSupplierBack.getCompanyPublicEmission()).isNull();

        companyPublicEmission.itemSuppliers(new HashSet<>(Set.of(itemSupplierBack)));
        assertThat(companyPublicEmission.getItemSuppliers()).containsOnly(itemSupplierBack);
        assertThat(itemSupplierBack.getCompanyPublicEmission()).isEqualTo(companyPublicEmission);

        companyPublicEmission.setItemSuppliers(new HashSet<>());
        assertThat(companyPublicEmission.getItemSuppliers()).doesNotContain(itemSupplierBack);
        assertThat(itemSupplierBack.getCompanyPublicEmission()).isNull();
    }
}
