package com.bacuti.service.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * @version v1
 */

public class ErrorDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int rowNo;

    private int columnNo;

    private String errorMessage;

    public ErrorDetailDTO(int rowNo, int columnNo, String errorMessage) {
        this.rowNo = rowNo;
        this.columnNo = columnNo;
        this.errorMessage = errorMessage;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public int getColumnNo() {
        return columnNo;
    }

    public void setColumnNo(int columnNo) {
        this.columnNo = columnNo;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ErrorDetailDTO that = (ErrorDetailDTO) o;

        return new EqualsBuilder().append(rowNo, that.rowNo).append(columnNo, that.columnNo).append(errorMessage, that.errorMessage).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(rowNo).append(columnNo).append(errorMessage).toHashCode();
    }

    @Override
    public String toString() {
        return "{" +
            "rowNo=" + rowNo +
            ", columnNo=" + columnNo +
            ", errorMessage='" + errorMessage + '\'' +
            '}';
    }
}
