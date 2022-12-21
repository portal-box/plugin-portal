package com.zestarr.pluginportal.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigUtils {

    public static File getPluginFolder() {
        return new File("plugins");
    }

    public static File getPluginListFile() {
        return new File("Plugins.yml");
    }

    public static YamlConfiguration getPluginListFileConfig() {
        return YamlConfiguration.loadConfiguration(getPluginListFile());
    }

}
