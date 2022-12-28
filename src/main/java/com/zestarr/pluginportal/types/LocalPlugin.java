package com.zestarr.pluginportal.types;

import lombok.Data;

import java.io.File;

@Data
public class LocalPlugin {

    private OnlinePlugin onlinePlugin;
    private boolean isInstalled = false; // Useless until we get uninstall/delete working
    private String filePath;

    public LocalPlugin(OnlinePlugin onlinePlugin) {
        this.onlinePlugin = onlinePlugin;
    }

}
