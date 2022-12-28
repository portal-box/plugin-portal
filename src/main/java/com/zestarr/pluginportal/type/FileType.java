package com.zestarr.pluginportal.type;

import lombok.Getter;

public enum FileType {

    JAR(".jar", true),
    EXTERNAL("", false),
    ZIP(".zip", false),
    SKRIPT(".sk", false);

    @Getter
    private String extension;
    @Getter
    private boolean supported;

    FileType(String extension, boolean supported) {
        this.extension = extension;
    }

}
