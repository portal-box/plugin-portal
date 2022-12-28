package com.zestarr.pluginportal.types;

import lombok.Data;

import java.io.File;

@Data
public class LocalPlugin extends OnlinePlugin {

    private boolean isInstalled = false; // Not inside PluginList.yml
    private File file; // Not inside PluginList.yml

}
