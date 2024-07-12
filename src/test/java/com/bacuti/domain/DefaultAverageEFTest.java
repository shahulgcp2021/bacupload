package com.bacuti.domain;

import static com.bacuti.domain.DefaultAverageEFTestSamples.*;
import static com.bacuti.domain.EnergySourceTestSamples.*;
import static com.bacuti.domain.ItemTestSamples.*;
import static com.bacuti.domain.UnitOfMeasureTestSamples.*;
import static com.bacuti.domain.WastedisposalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DefaultAverageEFTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DefaultAverageEF.class);
        DefaultAverageEF defaultAverageEF1 = getDefaultAverageEFSample1();
        DefaultAverageEF defaultAverageEF2 = new DefaultAverageEF();
        assertThat(defaultAverageEF1).isNotEqualTo(defaultAverageEF2);

        defaultAverageEF2.setId(defaultAverageEF1.getId());
        assertThat(defaultAverageEF1).isEqualTo(defaultAverageEF2);

        defaultAverageEF2 = getDefaultAverageEFSample2();
        assertThat(defaultAverageEF1).isNotEqualTo(defaultAverageEF2);
    }

    @Test
    void unitOfMeasureTest() {
        DefaultAverageEF defaultAverageEF = getDefaultAverageEFRandomSampleGenerator();
        UnitOfMeasure unitOfMeasureBack = getUnitOfMeasureRandomSampleGenerator();

        defaultAverageEF.setUnitOfMeasure(unitOfMeasureBack);
        assertThat(defaultAverageEF.getUnitOfMeasure()).isEqualTo(unitOfMeasureBack);

        defaultAverageEF.unitOfMeasure(null);
        assertThat(defaultAverageEF.getUnitOfMeasure()).isNull();
    }

    @Test
    void energySourceTest() {
        DefaultAverageEF defaultAverageEF = getDefaultAverageEFRandomSampleGenerator();
        EnergySource energySourceBack = getEnergySourceRandomSampleGenerator();

        defaultAverageEF.addEnergySource(energySourceBack);
        assertThat(defaultAverageEF.getEnergySources()).containsOnly(energySourceBack);
        assertThat(energySourceBack.getDefaultAverageEF()).isEqualTo(defaultAverageEF);

        defaultAverageEF.removeEnergySource(energySourceBack);
        assertThat(defaultAverageEF.getEnergySources()).doesNotContain(energySourceBack);
        assertThat(energySourceBack.getDefaultAverageEF()).isNull();

        defaultAverageEF.energySources(new HashSet<>(Set.of(energySourceBack)));
        assertThat(defaultAverageEF.getEnergySources()).containsOnly(energySourceBack);
        assertThat(energySourceBack.getDefaultAverageEF()).isEqualTo(defaultAverageEF);

        defaultAverageEF.setEnergySources(new HashSet<>());
        assertThat(defaultAverageEF.getEnergySources()).doesNotContain(energySourceBack);
        assertThat(energySourceBack.getDefaultAverageEF()).isNull();
    }

    @Test
    void itemTest() {
        DefaultAverageEF defaultAverageEF = getDefaultAverageEFRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        defaultAverageEF.addItem(itemBack);
        assertThat(defaultAverageEF.getItems()).containsOnly(itemBack);
        assertThat(itemBack.getDefaultAverageEF()).isEqualTo(defaultAverageEF);

        defaultAverageEF.removeItem(itemBack);
        assertThat(defaultAverageEF.getItems()).doesNotContain(itemBack);
        assertThat(itemBack.getDefaultAverageEF()).isNull();

        defaultAverageEF.items(new HashSet<>(Set.of(itemBack)));
        assertThat(defaultAverageEF.getItems()).containsOnly(itemBack);
        assertThat(itemBack.getDefaultAverageEF()).isEqualTo(defaultAverageEF);

        defaultAverageEF.setItems(new HashSet<>());
        assertThat(defaultAverageEF.getItems()).doesNotContain(itemBack);
        assertThat(itemBack.getDefaultAverageEF()).isNull();
    }

    @Test
    void wastedisposalTest() {
        DefaultAverageEF defaultAverageEF = getDefaultAverageEFRandomSampleGenerator();
        Wastedisposal wastedisposalBack = getWastedisposalRandomSampleGenerator();

        defaultAverageEF.addWastedisposal(wastedisposalBack);
        assertThat(defaultAverageEF.getWastedisposals()).containsOnly(wastedisposalBack);
        assertThat(wastedisposalBack.getDefaultAverageEF()).isEqualTo(defaultAverageEF);

        defaultAverageEF.removeWastedisposal(wastedisposalBack);
        assertThat(defaultAverageEF.getWastedisposals()).doesNotContain(wastedisposalBack);
        assertThat(wastedisposalBack.getDefaultAverageEF()).isNull();

        defaultAverageEF.wastedisposals(new HashSet<>(Set.of(wastedisposalBack)));
        assertThat(defaultAverageEF.getWastedisposals()).containsOnly(wastedisposalBack);
        assertThat(wastedisposalBack.getDefaultAverageEF()).isEqualTo(defaultAverageEF);

        defaultAverageEF.setWastedisposals(new HashSet<>());
        assertThat(defaultAverageEF.getWastedisposals()).doesNotContain(wastedisposalBack);
        assertThat(wastedisposalBack.getDefaultAverageEF()).isNull();
    }
}
