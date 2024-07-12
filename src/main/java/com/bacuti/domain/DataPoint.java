package com.bacuti.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Data point related to data upload operation.
 *
 * @author Imran Nazir K
 * @since 2024-06-28
 */
@Entity
@Table(name = "data_point")
public class DataPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_selected")
    private Boolean isSelected;

    @Column(name="operation")
    private String operation;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "data_item",
        joinColumns = @JoinColumn(name = "data_point_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id"))
    @OrderBy("id ASC")
    private List<DataPointItem> dataPointItems = new ArrayList<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public List<DataPointItem> getDataPointItems() {
        return dataPointItems;
    }

    public void setDataPointItems(List<DataPointItem> dataPointItems) {
        this.dataPointItems = dataPointItems;
    }
}
