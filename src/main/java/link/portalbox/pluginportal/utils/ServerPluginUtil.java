package link.portalbox.pluginportal.utils;

import link.portalbox.pluginportal.PluginPortal;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class ServerPluginUtil {

    public static Plugin getPluginByName(String name) {
        for (Plugin loopedPlugin : PluginPortal.getMainInstance().getServer().getPluginManager().getPlugins()) {
            if (loopedPlugin.getName().equalsIgnoreCase(name)) {
                return loopedPlugin;
            }
        }

        return null;
    }

    public static boolean disableServerPlugin(Plugin plugin) {
        if (!plugin.isEnabled()) {
            return false;
        }

        Set<Thread> threads = Thread.getAllStackTraces().keySet().stream()
                .filter(t -> isThreadFromClassLoader(t, plugin.getClass().getClassLoader()))
                .collect(Collectors.toSet());

        threads.forEach(t -> {
            t.interrupt();
            try {
                t.join(2000);
                if (t.isAlive()) {
                    t.stop();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });

        Bukkit.getPluginManager().disablePlugin(plugin);
        return true;
    }

    private static boolean isThreadFromClassLoader(Thread thread, ClassLoader classLoader) {
        return classLoader.equals(thread.getContextClassLoader())
                || (thread.getUncaughtExceptionHandler() != null
                && classLoader.equals(thread.getUncaughtExceptionHandler().getClass().getClassLoader()));
    }

    public static ArrayList<String> getAllPlugins() {
        ArrayList<String> pluginNames = new ArrayList<>();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            pluginNames.add(plugin.getName());
        }
        return pluginNames;
    }

    public static void deletePlugin(JavaPlugin plugin, PluginLoader loader) {
        PluginDescriptionFile desc = plugin.getDescription();
        String name = desc.getName();

        // Disable the plugin
        plugin.onDisable();

        // Remove the plugin from the server's list of plugins
        Plugin target = Bukkit.getServer().getPluginManager().getPlugin(name);
        Bukkit.getServer().getPluginManager().disablePlugin(target);

        // Delete the plugin's jar file from the plugins directory
        File pluginFile = target.getDataFolder().getParentFile();
        pluginFile.delete();

        // remove the plugin from the PluginLoader
        loader.disablePlugin(target);
    }


}