package com.zestarr.pluginportal.managers;

import com.zestarr.pluginportal.types.LocalPlugin;
import com.zestarr.pluginportal.types.OnlinePlugin;
import com.zestarr.pluginportal.utils.JsonUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataManager {

    private static final HashMap<String, LocalPlugin> downloadedPlugins = new HashMap<>();

    public void setupData() throws IOException {
        createPluginDataFile();
        try {
            for (LocalPlugin plugin : JsonUtils.readMapFromJson(createPluginDataFile()).values()) {
                downloadedPlugins.put(plugin.getOnlinePlugin().getDisplayName(), plugin);
                System.out.println(" - Loaded plugin: " + plugin.getOnlinePlugin().getDisplayName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public void savePluginToFile(OnlinePlugin plugin, Boolean wasThisInstalled) {
        downloadedPlugins.put(plugin.getDisplayName(), new LocalPlugin(plugin));
        downloadedPlugins.get(plugin.getDisplayName()).setIsInstalled(true);

        try {
            JsonUtils.writeMapToJson(downloadedPlugins, createPluginDataFile());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
