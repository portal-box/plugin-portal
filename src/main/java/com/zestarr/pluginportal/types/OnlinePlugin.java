package com.zestarr.pluginportal.types;

import lombok.Data;

import java.io.File;

@Data
public class OnlinePlugin {

    private String defaultFileName; // Optional
    private String displayName;
    private String version;
    private String description;
    private String downloadLink;
    private String sha256;
    private String[] dependencies; // Optional
    private String[] recommendedPlugins; // Optional

}
