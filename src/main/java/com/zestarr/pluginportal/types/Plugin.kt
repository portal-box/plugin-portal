package com.zestarr.pluginportal.types;

import lombok.Data;

@Data
public class Plugin {

    private String fileName; // Optional
    private String displayName;
    private String version;
    private String description;
    private String downloadLink;
    private String sha256;
    private String[] dependencies; // Optional
    private String[] recommendedPlugins; // Optional

}
