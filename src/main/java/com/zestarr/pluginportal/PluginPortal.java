package com.zestarr.pluginportal;

import com.zestarr.pluginportal.managers.PluginManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class PluginPortal extends JavaPlugin {

    @Override
    public void onEnable() {

        System.out.println(getDataFolder().getAbsolutePath());
        PluginManager pluginManager = new PluginManager();
        pluginManager.setupFolder(new File("plugins"));

        try {
            pluginManager.loadPlugins(getDataFolder() + "Plugins.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }


        pluginManager.downloadPlugins();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
