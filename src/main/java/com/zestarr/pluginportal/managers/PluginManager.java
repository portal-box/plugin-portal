package com.zestarr.pluginportal.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zestarr.pluginportal.types.Plugin;
import com.zestarr.pluginportal.utils.HttpUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.zestarr.pluginportal.utils.ConfigUtils.getPluginListFileConfig;

public class PluginManager {
    private HashMap<String, Plugin> plugins = new HashMap<>();
    private File downloadFolder;

    public void loadPlugins() throws IOException {
        FileConfiguration config = getPluginListFileConfig();
        for (String str : getPluginListFileConfig().getConfigurationSection("Plugins.").getKeys(false)) {
            Plugin plugin = new Plugin();
            plugin.setFileName(config.getString("Plugins." + str + ".fileName"));
            plugin.setDisplayName(str);
            plugin.setDescription(config.getString("Plugins." + str + ".description"));
            plugin.setDownloadLink(config.getString("Plugins." + str + ".downloadLink"));
            plugin.setVersion(config.getString("Plugins." + str + ".version"));
            plugin.setSha256(config.getString("Plugins." + str + ".sha256"));

            plugins.put(str, plugin);
        }

    }

    public void downloadPlugins(File downloadFolder) {
        setupFolder(downloadFolder);
        for (Plugin plugin : plugins.values()) {
            HttpUtils.download(plugin, downloadFolder);
        }
    }

    public void downloadPlugins() {
        downloadPlugins(downloadFolder);
    }

    public File setupFolder(File folder) {
        folder.mkdirs();
        downloadFolder = folder;

        HttpUtils.copyWebsite(
                "https://raw.githubusercontent.com/Zestarr/PluginPortal/master/PluginsList.yml",
                "Plugins.yml"
        );

        return folder;
    }

    public File setupFolder(String path) {
        return setupFolder(new File(path));
    }

    public File getDownloadFolder() { return downloadFolder; }
    public Map<String, Plugin> getPlugins() { return plugins; }
}