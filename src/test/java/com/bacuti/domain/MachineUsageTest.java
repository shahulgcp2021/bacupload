package com.bacuti.domain;

import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.MachineUsageTestSamples.*;
import static com.bacuti.domain.SiteTestSamples.*;
import static com.bacuti.domain.UnitOfMeasureTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MachineUsageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MachineUsage.class);
        MachineUsage machineUsage1 = getMachineUsageSample1();
        MachineUsage machineUsage2 = new MachineUsage();
        assertThat(machineUsage1).isNotEqualTo(machineUsage2);

        machineUsage2.setId(machineUsage1.getId());
        assertThat(machineUsage1).isEqualTo(machineUsage2);

        machineUsage2 = getMachineUsageSample2();
        assertThat(machineUsage1).isNotEqualTo(machineUsage2);
    }

    @Test
    void companyTest() {
        MachineUsage machineUsage = getMachineUsageRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        machineUsage.setCompany(companyBack);
        assertThat(machineUsage.getCompany()).isEqualTo(companyBack);

        machineUsage.company(null);
        assertThat(machineUsage.getCompany()).isNull();
    }

    @Test
    void siteTest() {
        MachineUsage machineUsage = getMachineUsageRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        machineUsage.setSite(siteBack);
        assertThat(machineUsage.getSite()).isEqualTo(siteBack);

        machineUsage.site(null);
        assertThat(machineUsage.getSite()).isNull();
    }

    @Test
    void unitOfMeasureTest() {
        MachineUsage machineUsage = getMachineUsageRandomSampleGenerator();
        UnitOfMeasure unitOfMeasureBack = getUnitOfMeasureRandomSampleGenerator();

        machineUsage.setUnitOfMeasure(unitOfMeasureBack);
        assertThat(machineUsage.getUnitOfMeasure()).isEqualTo(unitOfMeasureBack);

        machineUsage.unitOfMeasure(null);
        assertThat(machineUsage.getUnitOfMeasure()).isNull();
    }
}
