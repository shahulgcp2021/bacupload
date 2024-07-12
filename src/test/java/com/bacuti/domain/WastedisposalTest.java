package com.bacuti.domain;

import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.DefaultAverageEFTestSamples.*;
import static com.bacuti.domain.ItemTestSamples.*;
import static com.bacuti.domain.UnitOfMeasureTestSamples.*;
import static com.bacuti.domain.WastedisposalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WastedisposalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Wastedisposal.class);
        Wastedisposal wastedisposal1 = getWastedisposalSample1();
        Wastedisposal wastedisposal2 = new Wastedisposal();
        assertThat(wastedisposal1).isNotEqualTo(wastedisposal2);

        wastedisposal2.setId(wastedisposal1.getId());
        assertThat(wastedisposal1).isEqualTo(wastedisposal2);

        wastedisposal2 = getWastedisposalSample2();
        assertThat(wastedisposal1).isNotEqualTo(wastedisposal2);
    }

    @Test
    void companyTest() {
        Wastedisposal wastedisposal = getWastedisposalRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        wastedisposal.setCompany(companyBack);
        assertThat(wastedisposal.getCompany()).isEqualTo(companyBack);

        wastedisposal.company(null);
        assertThat(wastedisposal.getCompany()).isNull();
    }

    @Test
    void productTest() {
        Wastedisposal wastedisposal = getWastedisposalRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        wastedisposal.setProduct(itemBack);
        assertThat(wastedisposal.getProduct()).isEqualTo(itemBack);

        wastedisposal.product(null);
        assertThat(wastedisposal.getProduct()).isNull();
    }

    @Test
    void defaultAverageEFTest() {
        Wastedisposal wastedisposal = getWastedisposalRandomSampleGenerator();
        DefaultAverageEF defaultAverageEFBack = getDefaultAverageEFRandomSampleGenerator();

        wastedisposal.setDefaultAverageEF(defaultAverageEFBack);
        assertThat(wastedisposal.getDefaultAverageEF()).isEqualTo(defaultAverageEFBack);

        wastedisposal.defaultAverageEF(null);
        assertThat(wastedisposal.getDefaultAverageEF()).isNull();
    }

    @Test
    void unitOfMeasureTest() {
        Wastedisposal wastedisposal = getWastedisposalRandomSampleGenerator();
        UnitOfMeasure unitOfMeasureBack = getUnitOfMeasureRandomSampleGenerator();

        wastedisposal.setUnitOfMeasure(unitOfMeasureBack);
        assertThat(wastedisposal.getUnitOfMeasure()).isEqualTo(unitOfMeasureBack);

        wastedisposal.unitOfMeasure(null);
        assertThat(wastedisposal.getUnitOfMeasure()).isNull();
    }
}
