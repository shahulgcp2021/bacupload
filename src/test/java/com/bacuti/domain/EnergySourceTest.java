package com.bacuti.domain;

import static com.bacuti.domain.AggregateEnergyUsageTestSamples.*;
import static com.bacuti.domain.CompanyPublicEmissionTestSamples.*;
import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.DefaultAverageEFTestSamples.*;
import static com.bacuti.domain.EnergySourceTestSamples.*;
import static com.bacuti.domain.SiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EnergySourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EnergySource.class);
        EnergySource energySource1 = getEnergySourceSample1();
        EnergySource energySource2 = new EnergySource();
        assertThat(energySource1).isNotEqualTo(energySource2);

        energySource2.setId(energySource1.getId());
        assertThat(energySource1).isEqualTo(energySource2);

        energySource2 = getEnergySourceSample2();
        assertThat(energySource1).isNotEqualTo(energySource2);
    }

    @Test
    void companyTest() {
        EnergySource energySource = getEnergySourceRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        energySource.setCompany(companyBack);
        assertThat(energySource.getCompany()).isEqualTo(companyBack);

        energySource.company(null);
        assertThat(energySource.getCompany()).isNull();
    }

    @Test
    void siteTest() {
        EnergySource energySource = getEnergySourceRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        energySource.setSite(siteBack);
        assertThat(energySource.getSite()).isEqualTo(siteBack);

        energySource.site(null);
        assertThat(energySource.getSite()).isNull();
    }

    @Test
    void companyPublicEmissionTest() {
        EnergySource energySource = getEnergySourceRandomSampleGenerator();
        CompanyPublicEmission companyPublicEmissionBack = getCompanyPublicEmissionRandomSampleGenerator();

        energySource.setCompanyPublicEmission(companyPublicEmissionBack);
        assertThat(energySource.getCompanyPublicEmission()).isEqualTo(companyPublicEmissionBack);

        energySource.companyPublicEmission(null);
        assertThat(energySource.getCompanyPublicEmission()).isNull();
    }

    @Test
    void defaultAverageEFTest() {
        EnergySource energySource = getEnergySourceRandomSampleGenerator();
        DefaultAverageEF defaultAverageEFBack = getDefaultAverageEFRandomSampleGenerator();

        energySource.setDefaultAverageEF(defaultAverageEFBack);
        assertThat(energySource.getDefaultAverageEF()).isEqualTo(defaultAverageEFBack);

        energySource.defaultAverageEF(null);
        assertThat(energySource.getDefaultAverageEF()).isNull();
    }

    @Test
    void aggregateEnergyUsageTest() {
        EnergySource energySource = getEnergySourceRandomSampleGenerator();
        AggregateEnergyUsage aggregateEnergyUsageBack = getAggregateEnergyUsageRandomSampleGenerator();

        energySource.addAggregateEnergyUsage(aggregateEnergyUsageBack);
        assertThat(energySource.getAggregateEnergyUsages()).containsOnly(aggregateEnergyUsageBack);
        assertThat(aggregateEnergyUsageBack.getEnergySource()).isEqualTo(energySource);

        energySource.removeAggregateEnergyUsage(aggregateEnergyUsageBack);
        assertThat(energySource.getAggregateEnergyUsages()).doesNotContain(aggregateEnergyUsageBack);
        assertThat(aggregateEnergyUsageBack.getEnergySource()).isNull();

        energySource.aggregateEnergyUsages(new HashSet<>(Set.of(aggregateEnergyUsageBack)));
        assertThat(energySource.getAggregateEnergyUsages()).containsOnly(aggregateEnergyUsageBack);
        assertThat(aggregateEnergyUsageBack.getEnergySource()).isEqualTo(energySource);

        energySource.setAggregateEnergyUsages(new HashSet<>());
        assertThat(energySource.getAggregateEnergyUsages()).doesNotContain(aggregateEnergyUsageBack);
        assertThat(aggregateEnergyUsageBack.getEnergySource()).isNull();
    }
}
