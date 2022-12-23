package com.zestarr.pluginportal;

import co.aikar.commands.PaperCommandManager;
import com.zestarr.pluginportal.commands.PPMTab;
import com.zestarr.pluginportal.commands.PPMTestingCommand;
import com.zestarr.pluginportal.managers.DataManager;
import com.zestarr.pluginportal.managers.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class PluginPortal extends JavaPlugin {

    public static PluginManager pluginManager;
    public static DataManager dataManager;

    @Override
    public void onEnable() {

        pluginManager = new PluginManager();
        try {
            pluginManager.loadPlugins();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dataManager = new DataManager();
        dataManager.setupData();

        // Register Testing Command

        getCommand("ppm").setExecutor(new PPMTestingCommand());
        getCommand("ppm").setTabCompleter(new PPMTab());
    }


    public static PluginManager getPluginManager() {
        return pluginManager;
    }
    public static DataManager getDataManager() {
        return dataManager;
    }
}
