package com.zestarr.pluginportal.types;

import lombok.Data;

@Data
public class Plugin {

    private String name;
    private String displayName;
    private String version;
    private String description;
    private String downloadLink;
    private String sha256;

}
