package link.portalbox.pluginportal.managers;

import link.portalbox.pluginportal.PluginPortal;
import link.portalbox.pluginportal.type.LocalPlugin;
import link.portalbox.pluginportal.utils.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocalPluginManager implements Listener {

    private final HashMap<String, LocalPlugin> localPlugins = new HashMap<>();
    private final PluginPortal plugin;

    private File dataFile;

    public LocalPluginManager(PluginPortal plugin) {
        this.plugin = plugin;
        try {
            dataFile = new File(plugin.getDataFolder(), "plugins.json");
            if (!dataFile.exists()) {
                dataFile.createNewFile();
                FileWriter writer = new FileWriter(dataFile);
                writer.write("{}");
                writer.flush();
                writer.close();
            }
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> FileUtil.loadData(plugin, dataFile), 20L);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(String fileName, LocalPlugin localPlugin) {
        localPlugins.remove(fileName);
        localPlugins.put(fileName, localPlugin);
        localPlugin.setSha256(FileUtil.getSHA256(localPlugin.getFile()));
        FileUtil.saveData(plugin);
    }

    public List<String> getAllNames() {
        List<String> names = new ArrayList<>();
        for (LocalPlugin plugin : localPlugins.values()) {
            names.add(plugin.getFileName().replace(".jar", ""));
        }

        return names;
    }

    public HashMap<String, LocalPlugin> getPlugins() {
        return localPlugins;
    }

    public File getDataFile() {
        return dataFile;
    }
}