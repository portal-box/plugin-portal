package com.zestarr.pluginportal.managers;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.types.LocalPlugin;
import com.zestarr.pluginportal.types.OnlinePlugin;
import com.zestarr.pluginportal.utils.HttpUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.zestarr.pluginportal.utils.ConfigUtils.getPluginListFileConfig;

public class PluginManager {
    private HashMap<String, OnlinePlugin> plugins = new HashMap<>();

    public void loadPlugins() throws IOException {
        FileConfiguration config = getPluginListFileConfig();
        for (String str : getPluginListFileConfig().getConfigurationSection("Plugins.").getKeys(false)) {
            OnlinePlugin plugin = new OnlinePlugin();
            plugin.setDisplayName(str);
            plugin.setDefaultFileName(config.getString("Plugins." + str + ".fileName"));
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

    public Boolean isPluginUpToDate(LocalPlugin plugin) {
        if (plugin.getIsInstalled()) {
            if (plugin.getOnlinePlugin().getVersion().equals(PluginPortal.getPluginManager().getPlugins().get(plugin.getOnlinePlugin().getDisplayName()).getVersion())) {
                return true;
            }
        }

        return false;
    }

    public void updatePlugin(LocalPlugin plugin) throws IOException {
        if (!isPluginUpToDate(plugin)) {
            plugin.getFile().delete();
            plugin.setIsInstalled(false);
            HttpUtils.downloadPlugin(plugin.getOnlinePlugin());
        }
    }


    public Map<String, OnlinePlugin> getPlugins() { return plugins; }

}