package com.zestarr.pluginportal.managers;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.type.LocalPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocalPluginManager implements Listener {

    private HashMap<String, LocalPlugin> localPlugins = new HashMap<>();

    private File dataFile;
    private YamlConfiguration dataConfig;

    public LocalPluginManager(PluginPortal portal) throws IOException {
        dataFile = new File(portal.getDataFolder(), "plugins.yml");
        if (!dataFile.exists()) {
            dataFile.createNewFile();
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    @EventHandler
    public void onServerEnable(ServerLoadEvent e) {

        PluginManager pluginManager = Bukkit.getPluginManager();
        for (String idString : dataConfig.getConfigurationSection("").getKeys(false)) {
            String serverName = dataConfig.getString(idString + ".server-name");
            if (!pluginManager.isPluginEnabled(serverName)) {
                dataConfig.set(idString, null);
                continue;
            }
            String spigotName = dataConfig.getString(idString + ".spigot-name");
            localPlugins.put(spigotName, new LocalPlugin(Integer.parseInt(idString), spigotName, serverName, dataConfig.getString(idString + ".version")));
        }

    }

    public List<String> getAllNames() {
        List<String> names = new ArrayList<>();
        for (LocalPlugin plugin : localPlugins.values()) {
            names.add(plugin.getSpigotName());
        }
        return names;
    }

    public boolean isInstalled(String spigotName) { return localPlugins.containsKey(spigotName); }
    public boolean isLatestVersion(String spigotName, String latestVersion) { return localPlugins.get(spigotName).matchesVersion(latestVersion); }

    public void add(LocalPlugin localPlugin) { localPlugins.put(localPlugin.getSpigotName(), localPlugin); }

    public void update(String spigotName) {

    }

}