package com.zestarr.pluginportal.managers;

import com.zestarr.pluginportal.types.LocalPlugin;
import com.zestarr.pluginportal.types.OnlinePlugin;
import com.zestarr.pluginportal.utils.ConfigUtils;
import com.zestarr.pluginportal.utils.JsonUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataManager {

    public static final Map<String, LocalPlugin> downloadedPlugins = new HashMap<>();

    public void setupData() throws IOException {
        JsonUtils.loadData();
    }

    public File createPluginDataFile() {
        File file = new File("PluginData.json");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return file;
    }

    public Boolean isPluginInstalled(OnlinePlugin plugin) {
        return downloadedPlugins.get(plugin.getDisplayName()) != null;
    }

    public Map<String, LocalPlugin> getInstalledPlugins() {
        return downloadedPlugins;
    }

}
