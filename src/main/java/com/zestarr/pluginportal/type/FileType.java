package com.zestarr.pluginportal.type;

import lombok.Getter;

public enum FileType {

    JAR(".jar", true),
    EXTERNAL("External", false),
    ZIP(".zip", false),
    SKRIPT(".sk", false);

    @Getter
    private final String extension;
    @Getter
    private final boolean supported;

    FileType(String extension, boolean supported) {
        this.extension = extension;
        this.supported = supported;
    }

}
