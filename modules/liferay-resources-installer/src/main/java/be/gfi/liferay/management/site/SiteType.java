package be.gfi.liferay.management.site;

public enum SiteType {
    OPEN(1), PRIVATE(3), RESTRICTED(2), SYSTEM(4);

    private final int value;

    SiteType(final int value) {
        this.value = value;
    }
}
