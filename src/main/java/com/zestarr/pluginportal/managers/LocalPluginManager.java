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

        dataFile = new File(plugin.getDataFolder(), "plugins.json");
        if (!dataFile.exists()) {
            dataFile.createNewFile();
            FileWriter writer = new FileWriter(dataFile);
            writer.write("{}");
            writer.flush();
            writer.close();
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> FileUtil.loadData(plugin, dataFile), 20L);

    }

    public void add(String fileName, LocalPlugin localPlugin) {
        localPlugins.remove(fileName);
        localPlugins.put(fileName, localPlugin);
        localPlugin.setSha256(FileUtil.getSHA256(localPlugin.getFile()));
        FileUtil.saveData(plugin);
    }

    public List<String> getAllNames() {
        List<String> names = new ArrayList<>();
        for (LocalPlugin plugin : localPlugins.values()) {
            names.add(plugin.getFileName().replace(".jar", ""));
        }

        return names;
    }

    public HashMap<String, LocalPlugin> getPlugins() {
        return localPlugins;
    }
}