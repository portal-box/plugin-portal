package com.zestarr.pluginportal.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.types.Plugin;
import com.zestarr.pluginportal.utils.HttpUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zestarr.pluginportal.utils.ConfigUtils.getPluginFolder;
import static com.zestarr.pluginportal.utils.ConfigUtils.getPluginListFileConfig;

public class PluginManager {
    private HashMap<String, Plugin> plugins = new HashMap<>();

    public void loadPlugins() throws IOException {
        FileConfiguration config = getPluginListFileConfig();
        for (String str : getPluginListFileConfig().getConfigurationSection("Plugins.").getKeys(false)) {
            Plugin plugin = new Plugin();
            plugin.setDisplayName(str);
            plugin.setDefaultName(config.getString("Plugins." + str + ".fileName"));
            plugin.setDescription(config.getString("Plugins." + str + ".description"));
            plugin.setDownloadLink(config.getString("Plugins." + str + ".downloadLink"));
            plugin.setVersion(config.getString("Plugins." + str + ".version"));
            plugin.setSha256(config.getString("Plugins." + str + ".sha256"));

            plugins.put(str, plugin);
        }

    }

    public void loadPluginList() {

        HttpUtils.copyWebsite(
                "https://raw.githubusercontent.com/Zestarr/PluginPortal/master/PluginsList.yml",
                "PluginPortalPlugins.yml"
        );

    }

    public Boolean isPluginUpToDate(Plugin plugin) {

        if (PluginPortal.getDataManager().isPluginInstalled(plugin)) {
            if (plugin.getVersion().equals(PluginPortal.getDataManager().getInstalledPlugins().get(plugin.getDisplayName()).getVersion())) {
                return true;
            }
        }

        return false;
    }

    public void updatePlugin(Plugin plugin) {
        if (!isPluginUpToDate(plugin)) {
            plugin.getFile().delete();
            HttpUtils.download(plugin, getPluginFolder());
        }
    }


    public Map<String, Plugin> getPlugins() { return plugins; }

}