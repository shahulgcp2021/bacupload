package com.bacuti.domain;

import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.ItemTestSamples.*;
import static com.bacuti.domain.ProductUsageDetailTestSamples.*;
import static com.bacuti.domain.UnitOfMeasureTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductUsageDetailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductUsageDetail.class);
        ProductUsageDetail productUsageDetail1 = getProductUsageDetailSample1();
        ProductUsageDetail productUsageDetail2 = new ProductUsageDetail();
        assertThat(productUsageDetail1).isNotEqualTo(productUsageDetail2);

        productUsageDetail2.setId(productUsageDetail1.getId());
        assertThat(productUsageDetail1).isEqualTo(productUsageDetail2);

        productUsageDetail2 = getProductUsageDetailSample2();
        assertThat(productUsageDetail1).isNotEqualTo(productUsageDetail2);
    }

    @Test
    void companyTest() {
        ProductUsageDetail productUsageDetail = getProductUsageDetailRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        productUsageDetail.setCompany(companyBack);
        assertThat(productUsageDetail.getCompany()).isEqualTo(companyBack);

        productUsageDetail.company(null);
        assertThat(productUsageDetail.getCompany()).isNull();
    }

    @Test
    void productTest() {
        ProductUsageDetail productUsageDetail = getProductUsageDetailRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        productUsageDetail.setProduct(itemBack);
        assertThat(productUsageDetail.getProduct()).isEqualTo(itemBack);

        productUsageDetail.product(null);
        assertThat(productUsageDetail.getProduct()).isNull();
    }

    @Test
    void unitOfMeasureTest() {
        ProductUsageDetail productUsageDetail = getProductUsageDetailRandomSampleGenerator();
        UnitOfMeasure unitOfMeasureBack = getUnitOfMeasureRandomSampleGenerator();

        productUsageDetail.setUnitOfMeasure(unitOfMeasureBack);
        assertThat(productUsageDetail.getUnitOfMeasure()).isEqualTo(unitOfMeasureBack);

        productUsageDetail.unitOfMeasure(null);
        assertThat(productUsageDetail.getUnitOfMeasure()).isNull();
    }
}
