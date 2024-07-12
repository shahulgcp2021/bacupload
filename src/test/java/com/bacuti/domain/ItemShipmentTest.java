package com.bacuti.domain;

import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.ItemShipmentTestSamples.*;
import static com.bacuti.domain.ItemTestSamples.*;
import static com.bacuti.domain.ShipmentLaneTestSamples.*;
import static com.bacuti.domain.SiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemShipmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemShipment.class);
        ItemShipment itemShipment1 = getItemShipmentSample1();
        ItemShipment itemShipment2 = new ItemShipment();
        assertThat(itemShipment1).isNotEqualTo(itemShipment2);

        itemShipment2.setId(itemShipment1.getId());
        assertThat(itemShipment1).isEqualTo(itemShipment2);

        itemShipment2 = getItemShipmentSample2();
        assertThat(itemShipment1).isNotEqualTo(itemShipment2);
    }

    @Test
    void companyTest() {
        ItemShipment itemShipment = getItemShipmentRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        itemShipment.setCompany(companyBack);
        assertThat(itemShipment.getCompany()).isEqualTo(companyBack);

        itemShipment.company(null);
        assertThat(itemShipment.getCompany()).isNull();
    }

    @Test
    void itemTest() {
        ItemShipment itemShipment = getItemShipmentRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        itemShipment.setItem(itemBack);
        assertThat(itemShipment.getItem()).isEqualTo(itemBack);

        itemShipment.item(null);
        assertThat(itemShipment.getItem()).isNull();
    }

    @Test
    void shipmentLaneTest() {
        ItemShipment itemShipment = getItemShipmentRandomSampleGenerator();
        ShipmentLane shipmentLaneBack = getShipmentLaneRandomSampleGenerator();

        itemShipment.setShipmentLane(shipmentLaneBack);
        assertThat(itemShipment.getShipmentLane()).isEqualTo(shipmentLaneBack);

        itemShipment.shipmentLane(null);
        assertThat(itemShipment.getShipmentLane()).isNull();
    }

    @Test
    void siteTest() {
        ItemShipment itemShipment = getItemShipmentRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        itemShipment.setSite(siteBack);
        assertThat(itemShipment.getSite()).isEqualTo(siteBack);

        itemShipment.site(null);
        assertThat(itemShipment.getSite()).isNull();
    }
}
