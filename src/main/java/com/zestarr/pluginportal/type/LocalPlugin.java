package com.zestarr.pluginportal.type;

import com.zestarr.pluginportal.utils.FileUtil;
import lombok.Data;

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

    public boolean matchesVersion(long updateDate) {
        return previewingPlugin.getUpdateDate() == updateDate;
    }

    public boolean updateNeeded() {
        return !matchesVersion(new PreviewingPlugin(previewingPlugin.getId()).getUpdateDate());
    }

    public File getFile() { return new File("plugins", (fileName + (fileName.endsWith(".jar") ? "" : ".jar"))); }

}