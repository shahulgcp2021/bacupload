package com.bacuti.domain;

import static com.bacuti.domain.CapitalGoodTestSamples.*;
import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.DefaultAverageEFTestSamples.*;
import static com.bacuti.domain.SiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CapitalGoodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CapitalGood.class);
        CapitalGood capitalGood1 = getCapitalGoodSample1();
        CapitalGood capitalGood2 = new CapitalGood();
        assertThat(capitalGood1).isNotEqualTo(capitalGood2);

        capitalGood2.setId(capitalGood1.getId());
        assertThat(capitalGood1).isEqualTo(capitalGood2);

        capitalGood2 = getCapitalGoodSample2();
        assertThat(capitalGood1).isNotEqualTo(capitalGood2);
    }

    @Test
    void companyTest() {
        CapitalGood capitalGood = getCapitalGoodRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        capitalGood.setCompany(companyBack);
        assertThat(capitalGood.getCompany()).isEqualTo(companyBack);

        capitalGood.company(null);
        assertThat(capitalGood.getCompany()).isNull();
    }

    @Test
    void siteTest() {
        CapitalGood capitalGood = getCapitalGoodRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        capitalGood.setSite(siteBack);
        assertThat(capitalGood.getSite()).isEqualTo(siteBack);

        capitalGood.site(null);
        assertThat(capitalGood.getSite()).isNull();
    }

    @Test
    void emissionDbTest() {
        CapitalGood capitalGood = getCapitalGoodRandomSampleGenerator();
        DefaultAverageEF defaultAverageEFBack = getDefaultAverageEFRandomSampleGenerator();

        capitalGood.setEmissionDb(defaultAverageEFBack);
        assertThat(capitalGood.getEmissionDb()).isEqualTo(defaultAverageEFBack);

        capitalGood.emissionDb(null);
        assertThat(capitalGood.getEmissionDb()).isNull();
    }
}
