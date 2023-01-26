package link.portalbox.pluginportal;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import link.portalbox.PortalAPI;
import link.portalbox.pluginportal.commands.PluginPortalBaseCommand;
import link.portalbox.pluginportal.listener.StatusListener;
import link.portalbox.pluginportal.managers.DownloadManager;
import link.portalbox.pluginportal.managers.LocalPluginManager;
import link.portalbox.pluginportal.utils.Metrics;
import link.portalbox.util.JsonUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluginPortal extends JavaPlugin {

    private static boolean LATEST_VERSION = true;

    private LocalPluginManager localPluginManager;
    private DownloadManager downloadManager;
    private PortalAPI portalAPI;

    @Override
    public void onEnable() {

        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        Bukkit.getPluginManager().registerEvents(localPluginManager = new LocalPluginManager(this), this);
        Bukkit.getPluginManager().registerEvents(new StatusListener(), this);
        downloadManager = new DownloadManager(this);

        new PluginPortalBaseCommand(this);
        new Metrics(this, 17273);
        portalAPI = new PortalAPI();

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            Gson gson = new Gson();
            JsonElement root;
            root = gson.fromJson(JsonUtil.getJson("https://raw.githubusercontent.com/portal-box/plugin-portal/master/resources/Data.json"), JsonElement.class);
            if (!root.getAsJsonObject().get("latestVersion").getAsString().equals(getDescription().getVersion())) {
                getLogger().severe("You are running an outdated version of PluginPortal! Please update to the latest version!");
                LATEST_VERSION = false;
            }
        });
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

    public boolean isUpdated() {
        return LATEST_VERSION;
    }

    public PortalAPI getPortalAPI() {
        return portalAPI;
    }
}