package com.zestarr.pluginportal.type;

import lombok.Getter;

public enum SizeUnit {

    KB("KB"),
    MB("MB"),
    GB("GB");

    @Getter
    private String unit;

    SizeUnit(String unit) {
        this.unit = unit;
    }

}
