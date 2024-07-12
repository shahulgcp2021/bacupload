package com.bacuti.domain;

import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.ItemTestSamples.*;
import static com.bacuti.domain.ProductionQtyTestSamples.*;
import static com.bacuti.domain.SiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductionQtyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductionQty.class);
        ProductionQty productionQty1 = getProductionQtySample1();
        ProductionQty productionQty2 = new ProductionQty();
        assertThat(productionQty1).isNotEqualTo(productionQty2);

        productionQty2.setId(productionQty1.getId());
        assertThat(productionQty1).isEqualTo(productionQty2);

        productionQty2 = getProductionQtySample2();
        assertThat(productionQty1).isNotEqualTo(productionQty2);
    }

    @Test
    void companyTest() {
        ProductionQty productionQty = getProductionQtyRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        productionQty.setCompany(companyBack);
        assertThat(productionQty.getCompany()).isEqualTo(companyBack);

        productionQty.company(null);
        assertThat(productionQty.getCompany()).isNull();
    }

    @Test
    void siteTest() {
        ProductionQty productionQty = getProductionQtyRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        productionQty.setSite(siteBack);
        assertThat(productionQty.getSite()).isEqualTo(siteBack);

        productionQty.site(null);
        assertThat(productionQty.getSite()).isNull();
    }

    @Test
    void productTest() {
        ProductionQty productionQty = getProductionQtyRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        productionQty.setProduct(itemBack);
        assertThat(productionQty.getProduct()).isEqualTo(itemBack);

        productionQty.product(null);
        assertThat(productionQty.getProduct()).isNull();
    }
}
