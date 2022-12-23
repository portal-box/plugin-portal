package com.zestarr.pluginportal.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
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

}
