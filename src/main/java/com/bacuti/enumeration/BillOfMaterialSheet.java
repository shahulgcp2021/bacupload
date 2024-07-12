package com.bacuti.enumeration;

public enum BillOfMaterialSheet {
    Product("Product", 0, 1000, 0),
    Component("Component", 1, 1000, 0),
    Quantity("Quantity", 2, 1000000000, 2),
    UnitOfMeasure("Unit of Measure", 3, 1000000000, 0),
    Yield("Yield", 4, 1000000000, 2);

    private final Integer index;
    public final Integer maxLength;
    public final String fieldName;
    public final Integer precision;

    BillOfMaterialSheet(String fieldName, Integer index, Integer maxLength, int precision) {
        this.fieldName = fieldName;
        this.index = index;
        this.maxLength = maxLength;
        this.precision = precision;
    }

    public Integer getIndex() {
        return index;
    }

    public static BillOfMaterialSheet getByIndex(int index) {
        for (BillOfMaterialSheet module : BillOfMaterialSheet.values()) {
            if (module.getIndex() == index) {
                return module;
            }
        }
        return null;
    }
}
