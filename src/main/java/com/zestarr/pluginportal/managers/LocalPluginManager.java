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
        for (String idString : dataConfig.getConfigurationSection("").getKeys(false)) {
            String serverName = dataConfig.getString(idString + ".server-name");
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
    public HashMap<String, LocalPlugin> getPlugins() { return localPlugins; }

    public void add(LocalPlugin localPlugin) {
        localPlugins.put(localPlugin.getSpigotName(), localPlugin);
        dataConfig.createSection(localPlugin.getId() + "");
        dataConfig.set(localPlugin.getId() + ".server-name", localPlugin.getSpigotName());
        dataConfig.set(localPlugin.getId() + ".spigot-name", localPlugin.getServerName());
        dataConfig.set(localPlugin.getId() + ".version-name", localPlugin.getVersion());
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(String spigotName) {

    }

}