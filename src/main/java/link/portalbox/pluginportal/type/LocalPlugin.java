package link.portalbox.pluginportal.type;

import link.portalbox.pluginportal.utils.FileUtil;

import java.io.File;

public class LocalPlugin {

    private final PreviewingPlugin previewingPlugin;
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

    public File getFile() {
        return new File("plugins", (fileName + (fileName.endsWith(".jar") ? "" : ".jar")));
    }

    // Getters/Setters ------------------------------------------------------------------------------------------------

    public PreviewingPlugin getPreviewingPlugin() {
        return previewingPlugin;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}