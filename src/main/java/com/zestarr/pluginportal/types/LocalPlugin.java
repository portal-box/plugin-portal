package com.zestarr.pluginportal.types;

import lombok.Data;

import java.io.File;

@Data
public class LocalPlugin {

    private OnlinePlugin onlinePlugin;
    private Boolean isInstalled = false; // Not inside PluginList.yml
    private File file; // Not inside PluginList.yml

    public LocalPlugin(OnlinePlugin plugin) {
        onlinePlugin = plugin;
    }

}
