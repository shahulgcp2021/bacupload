package com.bacuti.domain;

import com.bacuti.domain.enumeration.Mode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A ShipmentLeg.
 */
@Entity
@Table(name = "shipment_leg")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShipmentLeg extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "segment")
    private Long segment;

    @Column(name = "carrier")
    private String carrier;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode")
    private Mode mode;

    @Column(name = "distance", precision = 21, scale = 2)
    private BigDecimal distance;

    @Column(name = "from_coordinate_lat", precision = 21, scale = 2)
    private BigDecimal fromCoordinateLat;

    @Column(name = "from_coordinate_long", precision = 21, scale = 2)
    private BigDecimal fromCoordinateLong;

    @Column(name = "to_coordinate_lat", precision = 21, scale = 2)
    private BigDecimal toCoordinateLat;

    @Column(name = "to_coordinate_long", precision = 21, scale = 2)
    private BigDecimal toCoordinateLong;

    @Column(name = "from_iata")
    private String fromIata;

    @Column(name = "to_iata")
    private String toIata;

    @Column(name = "emissions_factor", precision = 21, scale = 2)
    private BigDecimal emissionsFactor;

    @Column(name = "ef_source")
    private String efSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "company", "itemShipments", "shipmentLegs" }, allowSetters = true)
    private ShipmentLane shipmentLane;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShipmentLeg id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSegment() {
        return this.segment;
    }

    public ShipmentLeg segment(Long segment) {
        this.setSegment(segment);
        return this;
    }

    public void setSegment(Long segment) {
        this.segment = segment;
    }

    public String getCarrier() {
        return this.carrier;
    }

    public ShipmentLeg carrier(String carrier) {
        this.setCarrier(carrier);
        return this;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public Mode getMode() {
        return this.mode;
    }

    public ShipmentLeg mode(Mode mode) {
        this.setMode(mode);
        return this;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public BigDecimal getDistance() {
        return this.distance;
    }

    public ShipmentLeg distance(BigDecimal distance) {
        this.setDistance(distance);
        return this;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public BigDecimal getFromCoordinateLat() {
        return this.fromCoordinateLat;
    }

    public ShipmentLeg fromCoordinateLat(BigDecimal fromCoordinateLat) {
        this.setFromCoordinateLat(fromCoordinateLat);
        return this;
    }

    public void setFromCoordinateLat(BigDecimal fromCoordinateLat) {
        this.fromCoordinateLat = fromCoordinateLat;
    }

    public BigDecimal getFromCoordinateLong() {
        return this.fromCoordinateLong;
    }

    public ShipmentLeg fromCoordinateLong(BigDecimal fromCoordinateLong) {
        this.setFromCoordinateLong(fromCoordinateLong);
        return this;
    }

    public void setFromCoordinateLong(BigDecimal fromCoordinateLong) {
        this.fromCoordinateLong = fromCoordinateLong;
    }

    public BigDecimal getToCoordinateLat() {
        return this.toCoordinateLat;
    }

    public ShipmentLeg toCoordinateLat(BigDecimal toCoordinateLat) {
        this.setToCoordinateLat(toCoordinateLat);
        return this;
    }

    public void setToCoordinateLat(BigDecimal toCoordinateLat) {
        this.toCoordinateLat = toCoordinateLat;
    }

    public BigDecimal getToCoordinateLong() {
        return this.toCoordinateLong;
    }

    public ShipmentLeg toCoordinateLong(BigDecimal toCoordinateLong) {
        this.setToCoordinateLong(toCoordinateLong);
        return this;
    }

    public void setToCoordinateLong(BigDecimal toCoordinateLong) {
        this.toCoordinateLong = toCoordinateLong;
    }

    public String getFromIata() {
        return this.fromIata;
    }

    public ShipmentLeg fromIata(String fromIata) {
        this.setFromIata(fromIata);
        return this;
    }

    public void setFromIata(String fromIata) {
        this.fromIata = fromIata;
    }

    public String getToIata() {
        return this.toIata;
    }

    public ShipmentLeg toIata(String toIata) {
        this.setToIata(toIata);
        return this;
    }

    public void setToIata(String toIata) {
        this.toIata = toIata;
    }

    public BigDecimal getEmissionsFactor() {
        return this.emissionsFactor;
    }

    public ShipmentLeg emissionsFactor(BigDecimal emissionsFactor) {
        this.setEmissionsFactor(emissionsFactor);
        return this;
    }

    public void setEmissionsFactor(BigDecimal emissionsFactor) {
        this.emissionsFactor = emissionsFactor;
    }

    public String getEfSource() {
        return this.efSource;
    }

    public ShipmentLeg efSource(String efSource) {
        this.setEfSource(efSource);
        return this;
    }

    public void setEfSource(String efSource) {
        this.efSource = efSource;
    }

    public ShipmentLane getShipmentLane() {
        return this.shipmentLane;
    }

    public void setShipmentLane(ShipmentLane shipmentLane) {
        this.shipmentLane = shipmentLane;
    }

    public ShipmentLeg shipmentLane(ShipmentLane shipmentLane) {
        this.setShipmentLane(shipmentLane);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShipmentLeg)) {
            return false;
        }
        return getId() != null && getId().equals(((ShipmentLeg) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShipmentLeg{" +
            "id=" + getId() +
            ", segment=" + getSegment() +
            ", carrier='" + getCarrier() + "'" +
            ", mode='" + getMode() + "'" +
            ", distance=" + getDistance() +
            ", fromCoordinateLat=" + getFromCoordinateLat() +
            ", fromCoordinateLong=" + getFromCoordinateLong() +
            ", toCoordinateLat=" + getToCoordinateLat() +
            ", toCoordinateLong=" + getToCoordinateLong() +
            ", fromIata='" + getFromIata() + "'" +
            ", toIata='" + getToIata() + "'" +
            ", emissionsFactor=" + getEmissionsFactor() +
            ", efSource='" + getEfSource() + "'" +
            "}";
    }
}
