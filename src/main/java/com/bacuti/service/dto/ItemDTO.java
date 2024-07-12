package com.bacuti.service.dto;

import com.bacuti.domain.enumeration.AggregatedGoodsCategory;
import com.bacuti.domain.enumeration.ItemType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author shahul.Buhari
 * @version v1
 */
public class ItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String itemName;

    private String description;

    private ItemType itemType;

    private String itemCategory;

    private Boolean purchasedItem;

    private Boolean cbamImpacted;

    private String cnCode;

    private String cnName;

    private BigDecimal percentMn;

    private BigDecimal percentCr;

    private BigDecimal percentNi;

    private BigDecimal percentCarbon;

    private BigDecimal percentOtherAlloys;

    private BigDecimal percentOtherMaterials;

    private BigDecimal percentPreconsumerScrap;

    private BigDecimal scrapPerItem;

    private AggregatedGoodsCategory aggregatedGoodsCategory;

    private BigDecimal efUnits;

    private BigDecimal efScalingFactor;

    private BigDecimal supplierEmissionMultipler;

    private CompanyDTO companyDTO;

    private DefaultAverageEFDTO defaultAverageEFDTO;

    private UnitOfMeasureDTO unitOfMeasureDTO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public Boolean getPurchasedItem() {
        return purchasedItem;
    }

    public void setPurchasedItem(Boolean purchasedItem) {
        this.purchasedItem = purchasedItem;
    }

    public Boolean getCbamImpacted() {
        return cbamImpacted;
    }

    public void setCbamImpacted(Boolean cbamImpacted) {
        this.cbamImpacted = cbamImpacted;
    }

    public String getCnCode() {
        return cnCode;
    }

    public void setCnCode(String cnCode) {
        this.cnCode = cnCode;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public BigDecimal getPercentMn() {
        return percentMn;
    }

    public void setPercentMn(BigDecimal percentMn) {
        this.percentMn = percentMn;
    }

    public BigDecimal getPercentCr() {
        return percentCr;
    }

    public void setPercentCr(BigDecimal percentCr) {
        this.percentCr = percentCr;
    }

    public BigDecimal getPercentNi() {
        return percentNi;
    }

    public void setPercentNi(BigDecimal percentNi) {
        this.percentNi = percentNi;
    }

    public BigDecimal getPercentCarbon() {
        return percentCarbon;
    }

    public void setPercentCarbon(BigDecimal percentCarbon) {
        this.percentCarbon = percentCarbon;
    }

    public BigDecimal getPercentOtherAlloys() {
        return percentOtherAlloys;
    }

    public void setPercentOtherAlloys(BigDecimal percentOtherAlloys) {
        this.percentOtherAlloys = percentOtherAlloys;
    }

    public BigDecimal getPercentOtherMaterials() {
        return percentOtherMaterials;
    }

    public void setPercentOtherMaterials(BigDecimal percentOtherMaterials) {
        this.percentOtherMaterials = percentOtherMaterials;
    }

    public BigDecimal getPercentPreconsumerScrap() {
        return percentPreconsumerScrap;
    }

    public void setPercentPreconsumerScrap(BigDecimal percentPreconsumerScrap) {
        this.percentPreconsumerScrap = percentPreconsumerScrap;
    }

    public BigDecimal getScrapPerItem() {
        return scrapPerItem;
    }

    public void setScrapPerItem(BigDecimal scrapPerItem) {
        this.scrapPerItem = scrapPerItem;
    }

    public AggregatedGoodsCategory getAggregatedGoodsCategory() {
        return aggregatedGoodsCategory;
    }

    public void setAggregatedGoodsCategory(AggregatedGoodsCategory aggregatedGoodsCategory) {
        this.aggregatedGoodsCategory = aggregatedGoodsCategory;
    }

    public BigDecimal getEfUnits() {
        return efUnits;
    }

    public void setEfUnits(BigDecimal efUnits) {
        this.efUnits = efUnits;
    }

    public BigDecimal getEfScalingFactor() {
        return efScalingFactor;
    }

    public void setEfScalingFactor(BigDecimal efScalingFactor) {
        this.efScalingFactor = efScalingFactor;
    }

    public BigDecimal getSupplierEmissionMultipler() {
        return supplierEmissionMultipler;
    }

    public void setSupplierEmissionMultipler(BigDecimal supplierEmissionMultipler) {
        this.supplierEmissionMultipler = supplierEmissionMultipler;
    }

    public CompanyDTO getCompanyDTO() {
        return companyDTO;
    }

    public void setCompanyDTO(CompanyDTO companyDTO) {
        this.companyDTO = companyDTO;
    }

    public DefaultAverageEFDTO getDefaultAverageEFDTO() {
        return defaultAverageEFDTO;
    }

    public void setDefaultAverageEFDTO(DefaultAverageEFDTO defaultAverageEFDTO) {
        this.defaultAverageEFDTO = defaultAverageEFDTO;
    }

    public UnitOfMeasureDTO getUnitOfMeasureDTO() {
        return unitOfMeasureDTO;
    }

    public void setUnitOfMeasureDTO(UnitOfMeasureDTO unitOfMeasureDTO) {
        this.unitOfMeasureDTO = unitOfMeasureDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDTO itemDTO = (ItemDTO) o;
        return Objects.equals(id, itemDTO.id) && Objects.equals(itemName, itemDTO.itemName) && Objects.equals(description, itemDTO.description) && itemType == itemDTO.itemType && Objects.equals(itemCategory, itemDTO.itemCategory) && Objects.equals(purchasedItem, itemDTO.purchasedItem) && Objects.equals(cbamImpacted, itemDTO.cbamImpacted) && Objects.equals(cnCode, itemDTO.cnCode) && Objects.equals(cnName, itemDTO.cnName) && Objects.equals(percentMn, itemDTO.percentMn) && Objects.equals(percentCr, itemDTO.percentCr) && Objects.equals(percentNi, itemDTO.percentNi) && Objects.equals(percentCarbon, itemDTO.percentCarbon) && Objects.equals(percentOtherAlloys, itemDTO.percentOtherAlloys) && Objects.equals(percentOtherMaterials, itemDTO.percentOtherMaterials) && Objects.equals(percentPreconsumerScrap, itemDTO.percentPreconsumerScrap) && Objects.equals(scrapPerItem, itemDTO.scrapPerItem) && aggregatedGoodsCategory == itemDTO.aggregatedGoodsCategory && Objects.equals(efUnits, itemDTO.efUnits) && Objects.equals(efScalingFactor, itemDTO.efScalingFactor) && Objects.equals(supplierEmissionMultipler, itemDTO.supplierEmissionMultipler) && Objects.equals(companyDTO, itemDTO.companyDTO) && Objects.equals(defaultAverageEFDTO, itemDTO.defaultAverageEFDTO) && Objects.equals(unitOfMeasureDTO, itemDTO.unitOfMeasureDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemName, description, itemType, itemCategory, purchasedItem, cbamImpacted, cnCode, cnName, percentMn, percentCr, percentNi, percentCarbon, percentOtherAlloys, percentOtherMaterials, percentPreconsumerScrap, scrapPerItem, aggregatedGoodsCategory, efUnits, efScalingFactor, supplierEmissionMultipler, companyDTO, defaultAverageEFDTO, unitOfMeasureDTO);
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
            "id=" + id +
            ", itemName='" + itemName + '\'' +
            ", description='" + description + '\'' +
            ", itemType=" + itemType +
            ", itemCategory='" + itemCategory + '\'' +
            ", purchasedItem=" + purchasedItem +
            ", cbamImpacted=" + cbamImpacted +
            ", cnCode='" + cnCode + '\'' +
            ", cnName='" + cnName + '\'' +
            ", percentMn=" + percentMn +
            ", percentCr=" + percentCr +
            ", percentNi=" + percentNi +
            ", percentCarbon=" + percentCarbon +
            ", percentOtherAlloys=" + percentOtherAlloys +
            ", percentOtherMaterials=" + percentOtherMaterials +
            ", percentPreconsumerScrap=" + percentPreconsumerScrap +
            ", scrapPerItem=" + scrapPerItem +
            ", aggregatedGoodsCategory=" + aggregatedGoodsCategory +
            ", efUnits=" + efUnits +
            ", efScalingFactor=" + efScalingFactor +
            ", supplierEmissionMultipler=" + supplierEmissionMultipler +
            ", company=" + companyDTO +
            ", defaultAverageEF=" + defaultAverageEFDTO +
            ", unitOfMeasure=" + unitOfMeasureDTO +
            '}';
    }
}
