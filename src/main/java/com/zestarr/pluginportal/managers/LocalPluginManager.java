package com.zestarr.pluginportal.managers;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.type.LocalPlugin;
import com.zestarr.pluginportal.utils.FileUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocalPluginManager implements Listener {

    private HashMap<String, LocalPlugin> localPlugins = new HashMap<>();
    private PluginPortal plugin;

    @Getter
    private File dataFile;

    public LocalPluginManager(PluginPortal plugin) throws IOException {
        this.plugin = plugin;
        dataFile = new File(plugin.getDataFolder(), "plugins.yml");
        if (!dataFile.exists()) {
            dataFile.createNewFile();
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> FileUtil.loadData(plugin, dataFile), 20L);

    }

    public void add(LocalPlugin localPlugin) {
        localPlugins.put(localPlugin.getFileName(), localPlugin);
        FileUtil.saveData(plugin, dataFile);
    }

    public List<String> getAllNames() {
        List<String> names = new ArrayList<>();
        for (LocalPlugin plugin : localPlugins.values()) {
            names.add(plugin.getPreviewingPlugin().getSpigotName());
        }
        return names;
    }

    public boolean isInstalled(String spigotName) { return localPlugins.containsKey(spigotName); }
    public boolean isLatestVersion(String spigotName, String latestVersion) { return localPlugins.get(spigotName).matchesVersion(latestVersion); }
    public HashMap<String, LocalPlugin> getPlugins() { return localPlugins; }

}