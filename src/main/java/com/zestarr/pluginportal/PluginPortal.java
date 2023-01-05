package com.zestarr.pluginportal;

import com.zestarr.pluginportal.commands.PPMCommand;
import com.zestarr.pluginportal.managers.DownloadManager;
import com.zestarr.pluginportal.managers.LocalPluginManager;
import com.zestarr.pluginportal.managers.MarketplaceManager;
import com.zestarr.pluginportal.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluginPortal extends JavaPlugin {

    private MarketplaceManager marketplaceManager;
    private LocalPluginManager localPluginManager;
    private DownloadManager downloadManager;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        try {
            marketplaceManager = new MarketplaceManager(this);
            Bukkit.getPluginManager().registerEvents(localPluginManager = new LocalPluginManager(this), this);
            //Bukkit.getPluginManager().registerEvents(pluginStatusListener = new PluginStatusListener(this), this);
            downloadManager = new DownloadManager(this);
        } catch (Exception x) {
            x.printStackTrace();
        }

        PPMCommand command = new PPMCommand(this);
        getCommand("ppm").setExecutor(command);
        getCommand("ppm").setTabCompleter(command);

        Metrics metrics = new Metrics(this, 17273);

        Bukkit.getScheduler().runTaskLaterAsynchronously( this, () -> {
            localPluginManager.updateAllPlugins();
        }, 20);
    }

    public MarketplaceManager getMarketplaceManager() {
        return marketplaceManager;
    }

    public LocalPluginManager getLocalPluginManager() {
        return localPluginManager;
    }

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }

}