package com.bacuti.domain;

import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.ItemTestSamples.*;
import static com.bacuti.domain.MachineTestSamples.*;
import static com.bacuti.domain.RoutingTestSamples.*;
import static com.bacuti.domain.UnitOfMeasureTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RoutingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Routing.class);
        Routing routing1 = getRoutingSample1();
        Routing routing2 = new Routing();
        assertThat(routing1).isNotEqualTo(routing2);

        routing2.setId(routing1.getId());
        assertThat(routing1).isEqualTo(routing2);

        routing2 = getRoutingSample2();
        assertThat(routing1).isNotEqualTo(routing2);
    }

    @Test
    void companyTest() {
        Routing routing = getRoutingRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        routing.setCompany(companyBack);
        assertThat(routing.getCompany()).isEqualTo(companyBack);

        routing.company(null);
        assertThat(routing.getCompany()).isNull();
    }

    @Test
    void productTest() {
        Routing routing = getRoutingRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        routing.setProduct(itemBack);
        assertThat(routing.getProduct()).isEqualTo(itemBack);

        routing.product(null);
        assertThat(routing.getProduct()).isNull();
    }

    @Test
    void unitOfMeasureTest() {
        Routing routing = getRoutingRandomSampleGenerator();
        UnitOfMeasure unitOfMeasureBack = getUnitOfMeasureRandomSampleGenerator();

        routing.setUnitOfMeasure(unitOfMeasureBack);
        assertThat(routing.getUnitOfMeasure()).isEqualTo(unitOfMeasureBack);

        routing.unitOfMeasure(null);
        assertThat(routing.getUnitOfMeasure()).isNull();
    }

    @Test
    void machineTest() {
        Routing routing = getRoutingRandomSampleGenerator();
        Machine machineBack = getMachineRandomSampleGenerator();

        routing.addMachine(machineBack);
        assertThat(routing.getMachines()).containsOnly(machineBack);
        assertThat(machineBack.getRouting()).isEqualTo(routing);

        routing.removeMachine(machineBack);
        assertThat(routing.getMachines()).doesNotContain(machineBack);
        assertThat(machineBack.getRouting()).isNull();

        routing.machines(new HashSet<>(Set.of(machineBack)));
        assertThat(routing.getMachines()).containsOnly(machineBack);
        assertThat(machineBack.getRouting()).isEqualTo(routing);

        routing.setMachines(new HashSet<>());
        assertThat(routing.getMachines()).doesNotContain(machineBack);
        assertThat(machineBack.getRouting()).isNull();
    }
}
