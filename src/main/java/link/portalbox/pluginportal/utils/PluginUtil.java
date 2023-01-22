package link.portalbox.pluginportal.utils;

import link.portalbox.pluginportal.PluginPortal;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class PluginUtil {

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
}