package com.zestarr.pluginportal;

import com.zestarr.pluginportal.commands.PPMTab;
import com.zestarr.pluginportal.commands.PPMCommand;
import com.zestarr.pluginportal.managers.DataManager;
import com.zestarr.pluginportal.managers.PluginManager;
import com.zestarr.pluginportal.utils.ConfigUtils;
import com.zestarr.pluginportal.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class PluginPortal extends JavaPlugin {

    public static PluginManager pluginManager;
    public static DataManager dataManager;

    @Getter
    @Setter
    public static Boolean developerMode = false;

    @Override
    public void onEnable() {
        pluginManager = new PluginManager();

        try {
            ConfigUtils.getPluginListFile().createNewFile();
            pluginManager.loadPluginList();
            pluginManager.loadPlugins();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dataManager = new DataManager();
        try {
            dataManager.setupData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Register Testing Command

        getCommand("ppm").setExecutor(new PPMCommand());
        getCommand("ppm").setTabCompleter(new PPMTab());
    }

    @Override
    public void onDisable() {
        JsonUtils.saveData(dataManager.getInstalledPlugins(), ConfigUtils.createPluginDataFile().getAbsolutePath());
    }

    public static PluginManager getPluginManager() {
        return pluginManager;
    }
    public static DataManager getDataManager() {
        return dataManager;
    }
}
