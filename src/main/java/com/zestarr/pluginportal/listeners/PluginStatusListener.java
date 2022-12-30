package com.zestarr.pluginportal.listeners;

import com.zestarr.pluginportal.PluginPortal;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PluginStatusListener implements Listener {

    @Getter
    private final HashMap<String, Plugin> pluginMap = new HashMap<>();
    @Getter
    private final HashMap<Plugin, Boolean> canDelete = new HashMap<>();
    public PluginPortal portal;
    ArrayList<Plugin> plugins = new ArrayList<>(Arrays.asList(Bukkit.getPluginManager().getPlugins()));

    public PluginStatusListener(PluginPortal portal) {
        this.portal = portal;

        for (Plugin plugin : plugins) {
            pluginMap.put(plugin.getName(), plugin);
            canDelete.put(plugin, false);
        }
    }

    /*
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

                    // This isnt gonna work
                    new LocalPlugin(plugin, fileName); new LocalPlugin(plugin, fileName);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     */


}
