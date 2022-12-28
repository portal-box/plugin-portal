package com.zestarr.pluginportal.types;

import lombok.Data;

import java.io.File;

@Data
public class LocalPlugin {

    private OnlinePlugin onlinePlugin;
    private boolean isInstalled = false; // Not inside PluginList.yml
    private String filePath; // Not inside PluginList.yml

    public LocalPlugin(OnlinePlugin onlinePlugin) {
        this.onlinePlugin = onlinePlugin;
    }

}
