package com.zestarr.pluginportal.type;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.utils.FileUtil;
import lombok.Data;
import org.bukkit.Bukkit;

import java.io.File;

@Data
public class LocalPlugin {

    private PreviewingPlugin previewingPlugin;
    private String sha256, fileName;

    /*
    public LocalPlugin(int id, String spigotName, String serverName, String version) {
        this.id = id;
        this.previewingPlugin = previewingPlugin;
        this.serverName = serverName;
    }

     */

    public LocalPlugin(PreviewingPlugin previewingPlugin, String fileName) {
        this.previewingPlugin = previewingPlugin;
        this.fileName = fileName;
        this.sha256 = FileUtil.getSHA256(new File("plugins", fileName));

    }

    public String findFileName() {

        for (File file : new File("plugins").listFiles()) {
            if (file.getName().endsWith(".jar")) {
                if (FileUtil.getSHA256(file).equals(sha256)) {
                    this.fileName = file.getName();
                    return file.getName();
                }
            }
        }
        return null;
    }

    public boolean isInstalled() { return Bukkit.getPluginManager().isPluginEnabled(findFileName()); }
    public boolean matchesVersion(String latestVersion) { return previewingPlugin.getVersion().equals(latestVersion); }
    public boolean updateNeeded(PluginPortal plugin) {
        return matchesVersion(plugin.getMarketplaceManager().getMarketplaceCache().get(previewingPlugin.getId()));
    }

}