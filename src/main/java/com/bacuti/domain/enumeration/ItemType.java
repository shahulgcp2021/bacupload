package com.bacuti.domain.enumeration;

/**
 * The ItemType enumeration.
 */
public enum ItemType {
    FINISHED_GOOD("Finished good"),
    SUB_ASSEMBLY("Sub assembly"),
    COMPONENT("Component"),
    INGREDIENT("Ingredient");

    private final String displayName;

    private ItemType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
