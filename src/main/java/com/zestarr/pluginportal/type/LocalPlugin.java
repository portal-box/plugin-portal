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

    public LocalPlugin(PreviewingPlugin previewingPlugin, String fileName) {
        this.previewingPlugin = previewingPlugin;
        this.fileName = fileName;
        this.sha256 = FileUtil.getSHA256(new File("plugins", fileName + (fileName.endsWith(".jar") ? "" : ".jar")));

    }

    public boolean isInstalled() {
        return PluginPortal.getMainInstance().getDataFolder().getParentFile().listFiles().toString().contains(fileName);
    }

    public boolean matchesVersion(long releaseDate) { return previewingPlugin.getReleaseData() == releaseDate; }

    public boolean updateNeeded() {
        return !matchesVersion(new PreviewingPlugin(previewingPlugin.getId()).getReleaseData());
    }

    public File getFile() { return new File("plugins", (fileName + (fileName.endsWith(".jar") ? "" : ".jar"))); }

}