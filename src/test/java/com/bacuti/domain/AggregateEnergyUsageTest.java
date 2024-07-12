package com.bacuti.domain;

import static com.bacuti.domain.AggregateEnergyUsageTestSamples.*;
import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.EnergySourceTestSamples.*;
import static com.bacuti.domain.SiteTestSamples.*;
import static com.bacuti.domain.UnitOfMeasureTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AggregateEnergyUsageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AggregateEnergyUsage.class);
        AggregateEnergyUsage aggregateEnergyUsage1 = getAggregateEnergyUsageSample1();
        AggregateEnergyUsage aggregateEnergyUsage2 = new AggregateEnergyUsage();
        assertThat(aggregateEnergyUsage1).isNotEqualTo(aggregateEnergyUsage2);

        aggregateEnergyUsage2.setId(aggregateEnergyUsage1.getId());
        assertThat(aggregateEnergyUsage1).isEqualTo(aggregateEnergyUsage2);

        aggregateEnergyUsage2 = getAggregateEnergyUsageSample2();
        assertThat(aggregateEnergyUsage1).isNotEqualTo(aggregateEnergyUsage2);
    }

    @Test
    void companyTest() {
        AggregateEnergyUsage aggregateEnergyUsage = getAggregateEnergyUsageRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        aggregateEnergyUsage.setCompany(companyBack);
        assertThat(aggregateEnergyUsage.getCompany()).isEqualTo(companyBack);

        aggregateEnergyUsage.company(null);
        assertThat(aggregateEnergyUsage.getCompany()).isNull();
    }

    @Test
    void siteTest() {
        AggregateEnergyUsage aggregateEnergyUsage = getAggregateEnergyUsageRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        aggregateEnergyUsage.setSite(siteBack);
        assertThat(aggregateEnergyUsage.getSite()).isEqualTo(siteBack);

        aggregateEnergyUsage.site(null);
        assertThat(aggregateEnergyUsage.getSite()).isNull();
    }

    @Test
    void energySourceTest() {
        AggregateEnergyUsage aggregateEnergyUsage = getAggregateEnergyUsageRandomSampleGenerator();
        EnergySource energySourceBack = getEnergySourceRandomSampleGenerator();

        aggregateEnergyUsage.setEnergySource(energySourceBack);
        assertThat(aggregateEnergyUsage.getEnergySource()).isEqualTo(energySourceBack);

        aggregateEnergyUsage.energySource(null);
        assertThat(aggregateEnergyUsage.getEnergySource()).isNull();
    }

    @Test
    void unitOfMeasureTest() {
        AggregateEnergyUsage aggregateEnergyUsage = getAggregateEnergyUsageRandomSampleGenerator();
        UnitOfMeasure unitOfMeasureBack = getUnitOfMeasureRandomSampleGenerator();

        aggregateEnergyUsage.setUnitOfMeasure(unitOfMeasureBack);
        assertThat(aggregateEnergyUsage.getUnitOfMeasure()).isEqualTo(unitOfMeasureBack);

        aggregateEnergyUsage.unitOfMeasure(null);
        assertThat(aggregateEnergyUsage.getUnitOfMeasure()).isNull();
    }
}
