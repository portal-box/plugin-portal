package link.portalbox.pluginportal.type;

import link.portalbox.pluginportal.utils.FileUtil;
import link.portalbox.type.SpigetPlugin;

import java.io.File;

public class LocalPlugin {

    private final SpigetPlugin spigetPlugin;
    private String sha256, fileName;

    public LocalPlugin(SpigetPlugin spigetPlugin, String fileName) {
        this.spigetPlugin = spigetPlugin;
        this.fileName = fileName;
        this.sha256 = FileUtil.getSHA256(new File("plugins", fileName + (fileName.endsWith(".jar") ? "" : ".jar")));

    }

    public boolean matchesVersion(long updateDate) {
        return spigetPlugin.getUpdateDate() == updateDate;
    }

    public boolean updateNeeded() {
        return !matchesVersion(new SpigetPlugin(spigetPlugin.getId()).getUpdateDate());
    }

    public File getFile() {
        return new File("plugins", (fileName + (fileName.endsWith(".jar") ? "" : ".jar")));
    }


    public SpigetPlugin getSpigetPlugin() {
        return spigetPlugin;
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