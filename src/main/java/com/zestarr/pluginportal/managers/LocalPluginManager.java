package com.zestarr.pluginportal.managers;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.type.LocalPlugin;
import com.zestarr.pluginportal.utils.FileUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocalPluginManager implements Listener {

    private final HashMap<String, LocalPlugin> localPlugins = new HashMap<>();
    private final PluginPortal plugin;

    @Getter
    private final File dataFile;

    public LocalPluginManager(PluginPortal plugin) throws IOException {
        this.plugin = plugin;
        dataFile = new File(plugin.getDataFolder(), "plugins.yml");
        if (!dataFile.exists()) {
            dataFile.createNewFile();
            FileWriter writer = new FileWriter(dataFile);
            writer.write("{}");
            writer.flush();
            writer.close();
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> FileUtil.loadData(plugin, dataFile), 20L);

    }

    public void add(LocalPlugin localPlugin) {
        localPlugins.put(localPlugin.getPreviewingPlugin().getSpigotName(), localPlugin);
        FileUtil.saveData(plugin, dataFile);
    }

    public void updateAllPlugins() {
        for (LocalPlugin localPlugin : localPlugins.values()) {
            plugin.getDownloadManager().update(localPlugin);
        }

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