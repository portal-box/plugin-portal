package link.portalbox.pluginportal.type;

public enum SizeUnit {

    NONE("NOT AVAILABLE"),
    KB("KB"),
    MB("MB"),
    GB("GB");

    private final String unit;

    SizeUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
}