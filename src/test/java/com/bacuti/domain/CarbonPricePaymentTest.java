package com.bacuti.domain;

import static com.bacuti.domain.CarbonPricePaymentTestSamples.*;
import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.SiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarbonPricePaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarbonPricePayment.class);
        CarbonPricePayment carbonPricePayment1 = getCarbonPricePaymentSample1();
        CarbonPricePayment carbonPricePayment2 = new CarbonPricePayment();
        assertThat(carbonPricePayment1).isNotEqualTo(carbonPricePayment2);

        carbonPricePayment2.setId(carbonPricePayment1.getId());
        assertThat(carbonPricePayment1).isEqualTo(carbonPricePayment2);

        carbonPricePayment2 = getCarbonPricePaymentSample2();
        assertThat(carbonPricePayment1).isNotEqualTo(carbonPricePayment2);
    }

    @Test
    void companyTest() {
        CarbonPricePayment carbonPricePayment = getCarbonPricePaymentRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        carbonPricePayment.setCompany(companyBack);
        assertThat(carbonPricePayment.getCompany()).isEqualTo(companyBack);

        carbonPricePayment.company(null);
        assertThat(carbonPricePayment.getCompany()).isNull();
    }

    @Test
    void siteTest() {
        CarbonPricePayment carbonPricePayment = getCarbonPricePaymentRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        carbonPricePayment.setSite(siteBack);
        assertThat(carbonPricePayment.getSite()).isEqualTo(siteBack);

        carbonPricePayment.site(null);
        assertThat(carbonPricePayment.getSite()).isNull();
    }
}
