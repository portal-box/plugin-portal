package com.zestarr.pluginportal.listeners;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.type.LocalPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PluginStatusListener implements Listener {

    public PluginPortal portal;
    ArrayList<Plugin> plugins = new ArrayList<>(Arrays.asList(Bukkit.getPluginManager().getPlugins()));
    @Getter
    private final HashMap<String, Plugin> pluginMap = new HashMap<>();
    @Getter
    private final HashMap<Plugin, Boolean> canDelete = new HashMap<>();

    public PluginStatusListener(PluginPortal portal) {
        this.portal = portal;

        for (Plugin plugin : plugins) {
            pluginMap.put(plugin.getName(), plugin);
            canDelete.put(plugin, false);
        }
    }

    @EventHandler
    public void onPluginDeload(PluginDisableEvent event) {
        try {
            if (canDelete.containsKey(event.getPlugin())) {
                if (canDelete.get(event.getPlugin())) {
                    Field field = JavaPlugin.class.getDeclaredField("file");
                    field.setAccessible(true);
                    File file = ((File) field.get(event.getPlugin()));
                    file.delete();
                    canDelete.remove(event.getPlugin());

                    for (LocalPlugin localPlugin : portal.getLocalPluginManager().getPlugins().values()) {
                        if (localPlugin.getSpigotName().equals(file.getName().replace(".jar", ""))) {
                            portal.getLocalPluginManager().getPlugins().remove(localPlugin.getSpigotName());
                            break;
                        }
                    }

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
