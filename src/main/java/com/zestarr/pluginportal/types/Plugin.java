package com.zestarr.pluginportal.types;

import lombok.Data;

@Data
public class Plugin {

    private String fileName;
    private String displayName;
    private String version;
    private String description;
    private String downloadLink;
    private String sha256;

}
