package com.bacuti.domain.enumeration;

/**
 * The AggregatedGoodsCategory enumeration.
 */
public enum AggregatedGoodsCategory {
    CATEG1("Category 1"),
    CATEG2("Category 2");
    private  final String displayName ;

    private AggregatedGoodsCategory(String displayName) {
        this.displayName = displayName;
    }

    public  String getDisplayName() {
        return displayName;
    }
}
