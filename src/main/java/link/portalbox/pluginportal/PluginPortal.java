package link.portalbox.pluginportal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private MarketplaceManager marketplaceManager;
    private LocalPluginManager localPluginManager;
    private DownloadManager downloadManager;

    private static boolean IS_PLUGIN_LATEST_VERSION = true;

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
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root;
            try {
                root = mapper.readValue(JsonUtil.getDataJson(), JsonNode.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            if (!root.get("latestVersion").equals(getDescription().getVersion())) {
                getLogger().warning("You are running an outdated version of PluginPortal! Please update to the latest version!");
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