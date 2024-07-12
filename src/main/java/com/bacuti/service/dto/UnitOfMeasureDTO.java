package com.bacuti.service.dto;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * A UnitOfMeasure.
 */

public class UnitOfMeasureDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UnitOfMeasureDTO that = (UnitOfMeasureDTO) o;

        return new EqualsBuilder().append(id, that.id).append(name, that.name).append(key, that.key).append(value, that.value).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).append(key).append(value).toHashCode();
    }

    @Override
    public String toString() {
        return "UnitOfMeasureDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", key='" + key + '\'' +
            ", value='" + value + '\'' +
            '}';
    }
}
