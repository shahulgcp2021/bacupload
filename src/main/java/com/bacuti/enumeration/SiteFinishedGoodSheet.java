package com.bacuti.enumeration;

public enum SiteFinishedGoodSheet {
    SiteName("Site Name", 0, 255),
    FinishedGood("Finished Good", 1, 1000);

    public final Integer index;
    public final String fieldName;
    public final Integer maxLength;

    SiteFinishedGoodSheet(String fieldName, Integer index, Integer maxLength) {
        this.fieldName = fieldName;
        this.index = index;
        this.maxLength = maxLength;
    }

    public static SiteFinishedGoodSheet getByIndex(int index) {
        for (SiteFinishedGoodSheet module : SiteFinishedGoodSheet.values()) {
            if (module.index == index) {
                return module;
            }
        }
        return null;
    }
}
