package com.zestarr.pluginportal.type;

import lombok.Data;
import org.bukkit.Bukkit;

@Data
public class LocalPlugin {

    private int id;
    private String spigotName, serverName, version;

    public LocalPlugin(int id, String spigotName, String serverName, String version) {
        this.id = id;
        this.spigotName = spigotName;
        this.serverName = serverName;
        this.version = version;
    }

    public boolean isInstalled() { return Bukkit.getPluginManager().isPluginEnabled(serverName); }
    public boolean matchesVersion(String latestVersion) { return version.equals(latestVersion); }

}