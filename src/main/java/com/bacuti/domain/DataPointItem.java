package com.bacuti.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import com.bacuti.domain.enumeration.DataPointUploadStatus;

/**
 * Data point Items of the corresponding Data Point.
 *
 * @author Imran Nazir K
 * @since 2024-06-28
 */
@Entity
@Table(name = "data_point_item")
public class DataPointItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "item")
    private String item;

    @Enumerated(EnumType.STRING)
    @Column(name = "upload_status")
    private DataPointUploadStatus uploadStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public DataPointUploadStatus getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(DataPointUploadStatus uploadStatus) {
        this.uploadStatus = uploadStatus;
    }
}
