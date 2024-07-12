package com.bacuti.domain;

import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.MachineTestSamples.*;
import static com.bacuti.domain.RoutingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MachineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Machine.class);
        Machine machine1 = getMachineSample1();
        Machine machine2 = new Machine();
        assertThat(machine1).isNotEqualTo(machine2);

        machine2.setId(machine1.getId());
        assertThat(machine1).isEqualTo(machine2);

        machine2 = getMachineSample2();
        assertThat(machine1).isNotEqualTo(machine2);
    }

    @Test
    void companyTest() {
        Machine machine = getMachineRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        machine.setCompany(companyBack);
        assertThat(machine.getCompany()).isEqualTo(companyBack);

        machine.company(null);
        assertThat(machine.getCompany()).isNull();
    }

    @Test
    void routingTest() {
        Machine machine = getMachineRandomSampleGenerator();
        Routing routingBack = getRoutingRandomSampleGenerator();

        machine.setRouting(routingBack);
        assertThat(machine.getRouting()).isEqualTo(routingBack);

        machine.routing(null);
        assertThat(machine.getRouting()).isNull();
    }
}
