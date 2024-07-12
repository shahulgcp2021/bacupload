package com.bacuti.domain;

import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.EmployeeTravelAvgTestSamples.*;
import static com.bacuti.domain.SiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeTravelAvgTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeTravelAvg.class);
        EmployeeTravelAvg employeeTravelAvg1 = getEmployeeTravelAvgSample1();
        EmployeeTravelAvg employeeTravelAvg2 = new EmployeeTravelAvg();
        assertThat(employeeTravelAvg1).isNotEqualTo(employeeTravelAvg2);

        employeeTravelAvg2.setId(employeeTravelAvg1.getId());
        assertThat(employeeTravelAvg1).isEqualTo(employeeTravelAvg2);

        employeeTravelAvg2 = getEmployeeTravelAvgSample2();
        assertThat(employeeTravelAvg1).isNotEqualTo(employeeTravelAvg2);
    }

    @Test
    void companyTest() {
        EmployeeTravelAvg employeeTravelAvg = getEmployeeTravelAvgRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        employeeTravelAvg.setCompany(companyBack);
        assertThat(employeeTravelAvg.getCompany()).isEqualTo(companyBack);

        employeeTravelAvg.company(null);
        assertThat(employeeTravelAvg.getCompany()).isNull();
    }

    @Test
    void siteTest() {
        EmployeeTravelAvg employeeTravelAvg = getEmployeeTravelAvgRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        employeeTravelAvg.setSite(siteBack);
        assertThat(employeeTravelAvg.getSite()).isEqualTo(siteBack);

        employeeTravelAvg.site(null);
        assertThat(employeeTravelAvg.getSite()).isNull();
    }
}
