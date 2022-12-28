package com.zestarr.pluginportal;

import com.zestarr.pluginportal.commands.DebugCommand;
import com.zestarr.pluginportal.commands.PPMCommand;
import com.zestarr.pluginportal.commands.PPMTab;
import com.zestarr.pluginportal.managers.DataManager;
import com.zestarr.pluginportal.managers.PluginManager;
import com.zestarr.pluginportal.utils.JsonUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluginPortal extends JavaPlugin {

    public static PluginManager pluginManager;
    public static DataManager dataManager;

    @Override
    public void onEnable() {

        pluginManager = new PluginManager();
        pluginManager.setup();

        dataManager = new DataManager();
        JsonUtils.loadData();

        getCommand("ppm").setExecutor(new PPMCommand());
        getCommand("ppm").setTabCompleter(new PPMTab());

        getCommand("debug").setExecutor(new DebugCommand());
    }

    @Override
    public void onDisable() {
        JsonUtils.saveData(); // Just in case, not needed for sure though. Data should save on install.
    }

    public static PluginManager getPluginManager() {
        return pluginManager;
    }

    public static DataManager getDataManager() {
        return dataManager;
    }

}
