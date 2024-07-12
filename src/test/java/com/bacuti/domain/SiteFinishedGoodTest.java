package com.bacuti.domain;

import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.ItemTestSamples.*;
import static com.bacuti.domain.SiteFinishedGoodTestSamples.*;
import static com.bacuti.domain.SiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SiteFinishedGoodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SiteFinishedGood.class);
        SiteFinishedGood siteFinishedGood1 = getSiteFinishedGoodSample1();
        SiteFinishedGood siteFinishedGood2 = new SiteFinishedGood();
        assertThat(siteFinishedGood1).isNotEqualTo(siteFinishedGood2);

        siteFinishedGood2.setId(siteFinishedGood1.getId());
        assertThat(siteFinishedGood1).isEqualTo(siteFinishedGood2);

        siteFinishedGood2 = getSiteFinishedGoodSample2();
        assertThat(siteFinishedGood1).isNotEqualTo(siteFinishedGood2);
    }

    @Test
    void companyTest() {
        SiteFinishedGood siteFinishedGood = getSiteFinishedGoodRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        siteFinishedGood.setCompany(companyBack);
        assertThat(siteFinishedGood.getCompany()).isEqualTo(companyBack);

        siteFinishedGood.company(null);
        assertThat(siteFinishedGood.getCompany()).isNull();
    }

    @Test
    void siteTest() {
        SiteFinishedGood siteFinishedGood = getSiteFinishedGoodRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        siteFinishedGood.setSite(siteBack);
        assertThat(siteFinishedGood.getSite()).isEqualTo(siteBack);

        siteFinishedGood.site(null);
        assertThat(siteFinishedGood.getSite()).isNull();
    }

    @Test
    void finishedGoodTest() {
        SiteFinishedGood siteFinishedGood = getSiteFinishedGoodRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        siteFinishedGood.setFinishedGood(itemBack);
        assertThat(siteFinishedGood.getFinishedGood()).isEqualTo(itemBack);

        siteFinishedGood.finishedGood(null);
        assertThat(siteFinishedGood.getFinishedGood()).isNull();
    }
}
