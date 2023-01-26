package link.portalbox.pluginportal.managers;

import link.portalbox.pluginportal.PluginPortal;
import link.portalbox.pluginportal.type.LocalPlugin;
import link.portalbox.type.SpigetPlugin;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class DownloadManager {

    private final PluginPortal portal;

    public DownloadManager(PluginPortal portal) {
        this.portal = portal;
    }

    public LocalPlugin download(SpigetPlugin plugin) {
        try {
            URL url = new URL("https://api.spiget.org/v2/resources/" + plugin.getId() + "/download");
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "github.com/Zestarr/PluginPortal");

            String fileName = PluginPortal.getMainInstance().getPortalAPI().getMarketplaceManager().getMarketplaceCache().get(plugin.getId());
            InputStream inputStream = connection.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(new File("plugins", fileName + (fileName.endsWith(".jar") ? "" : ".jar")));

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            LocalPlugin localPlugin = new LocalPlugin(plugin, fileName);
            portal.getLocalPluginManager().add(fileName, localPlugin);
            return localPlugin;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}