package com.bacuti.service.dto;

public class SiteFinishedGoodDTO {
    private Long id;
    private CompanyDTO company;
    private SiteDTO site;
    private ItemDTO finishedGood;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    public SiteDTO getSite() {
        return site;
    }

    public void setSite(SiteDTO site) {
        this.site = site;
    }

    public ItemDTO getFinishedGood() {
        return finishedGood;
    }

    public void setFinishedGood(ItemDTO finishedGood) {
        this.finishedGood = finishedGood;
    }
}
