package com.zestarr.pluginportal;

import com.zestarr.pluginportal.commands.PPMCommand;
import com.zestarr.pluginportal.managers.LocalPluginManager;
import com.zestarr.pluginportal.managers.MarketplaceManager;
import com.zestarr.pluginportal.managers.PluginManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluginPortal extends JavaPlugin {

    private MarketplaceManager marketplaceManager;
    private LocalPluginManager localPluginManager;

    @Override
    public void onEnable() {
        try {
            marketplaceManager = new MarketplaceManager(this);
            Bukkit.getPluginManager().registerEvents(localPluginManager = new LocalPluginManager(this), this);
        } catch (Exception x) {

        }

        PPMCommand command = new PPMCommand(this);
        getCommand("ppm").setExecutor(command);
        getCommand("ppm").setTabCompleter(command);
    }

    @Override
    public void onDisable() {

    }

    public MarketplaceManager getMarketplaceManager() { return marketplaceManager; }
    public LocalPluginManager getLocalPluginManager() { return localPluginManager; }

}