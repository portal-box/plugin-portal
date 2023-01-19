package link.portalbox.pluginportal;

import link.portalbox.pluginportal.commands.PluginPortalBaseCommand;
import link.portalbox.pluginportal.managers.DownloadManager;
import link.portalbox.pluginportal.managers.LocalPluginManager;
import link.portalbox.pluginportal.managers.MarketplaceManager;
import link.portalbox.pluginportal.utils.Metrics;
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
            marketplaceManager = new MarketplaceManager();
            Bukkit.getPluginManager().registerEvents(localPluginManager = new LocalPluginManager(this), this);
            downloadManager = new DownloadManager(this);
        } catch (Exception x) {
            x.printStackTrace();
        }

        new PluginPortalBaseCommand(this);
        new Metrics(this, 17273);
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

    public static PluginPortal getMainInstance() {
        return JavaPlugin.getPlugin(PluginPortal.class);
    }
}