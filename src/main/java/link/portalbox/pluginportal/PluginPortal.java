package link.portalbox.pluginportal;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import link.portalbox.pluginportal.commands.PluginPortalBaseCommand;
import link.portalbox.pluginportal.listener.StatusListener;
import link.portalbox.pluginportal.managers.DownloadManager;
import link.portalbox.pluginportal.managers.LocalPluginManager;
import link.portalbox.pluginportal.managers.MarketplaceManager;
import link.portalbox.pluginportal.utils.JsonUtil;
import link.portalbox.pluginportal.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluginPortal extends JavaPlugin {

    private static boolean IS_PLUGIN_LATEST_VERSION = true;

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
            Bukkit.getPluginManager().registerEvents(new StatusListener(), this);
            downloadManager = new DownloadManager(this);
        } catch (Exception x) {
            x.printStackTrace();
        }

        new PluginPortalBaseCommand(this);
        new Metrics(this, 17273);

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            Gson gson = new Gson();
            JsonElement root;
            root = gson.fromJson(JsonUtil.getDataJson(), JsonElement.class);
            if (!root.getAsJsonObject().get("latestVersion").getAsString().equals(getDescription().getVersion())) {
                getLogger().severe("You are running an outdated version of PluginPortal! Please update to the latest version!");
                IS_PLUGIN_LATEST_VERSION = false;
            }
        });
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

    public boolean isIsPluginLatestVersion() {
        return IS_PLUGIN_LATEST_VERSION;
    }
}