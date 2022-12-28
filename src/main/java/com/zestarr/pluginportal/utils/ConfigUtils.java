package com.zestarr.pluginportal.utils;

import com.zestarr.pluginportal.PluginPortal;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigUtils {

    public static File getPluginFolder() {
        return new File("plugins");
    }

    public static File getPluginListFile() {
        return new File("PluginPortalPlugins.yml");
    }

    public static YamlConfiguration getPluginListFileConfig() {
        return YamlConfiguration.loadConfiguration(getPluginListFile());
    }

    public static File createPluginDataFile() {
        File file = new File("PluginData.json");
        try {
            if (!file.exists()) {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write("{}");
                writer.flush();
                writer.close();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return file;
    }

}
