package com.zestarr.pluginportal.type;

import lombok.Getter;

public enum SizeUnit {

    NONE("NOT AVAILABLE"),
    KB("KB"),
    MB("MB"),
    GB("GB");

    @Getter
    private final String unit;

    SizeUnit(String unit) {
        this.unit = unit;
    }

}
