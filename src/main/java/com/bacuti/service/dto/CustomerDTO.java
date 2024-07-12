package com.bacuti.service.dto;

/**
 * Customer Data Transfer Object file
 *
 * @author Premkalyan Sekar
 * @since 03/07/2023
 * @version 1.0
 */
public class CustomerDTO {

    private Long id;

    private String customerName;

    private String description;

    private String website;

    private String country;

    private String sustainabilityContactName;

    private String sustainabilityContactEmail;

    private CompanyDTO company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSustainabilityContactName() {
        return sustainabilityContactName;
    }

    public void setSustainabilityContactName(String sustainabilityContactName) {
        this.sustainabilityContactName = sustainabilityContactName;
    }

    public String getSustainabilityContactEmail() {
        return sustainabilityContactEmail;
    }

    public void setSustainabilityContactEmail(String sustainabilityContactEmail) {
        this.sustainabilityContactEmail = sustainabilityContactEmail;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }
}
