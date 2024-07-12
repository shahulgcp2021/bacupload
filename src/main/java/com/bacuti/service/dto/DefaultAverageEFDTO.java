package com.bacuti.service.dto;

import java.math.BigDecimal;

public class DefaultAverageEFDTO extends BaseDTO {

    private String domain;

    private String detail;

    private String countryOrRegion;

    private BigDecimal emissionFactor;

    private String efSource;

    private String code;

    private String codeType;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCountryOrRegion() {
        return countryOrRegion;
    }

    public void setCountryOrRegion(String countryOrRegion) {
        this.countryOrRegion = countryOrRegion;
    }

    public BigDecimal getEmissionFactor() {
        return emissionFactor;
    }

    public void setEmissionFactor(BigDecimal emissionFactor) {
        this.emissionFactor = emissionFactor;
    }

    public String getEfSource() {
        return efSource;
    }

    public void setEfSource(String efSource) {
        this.efSource = efSource;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }
}
