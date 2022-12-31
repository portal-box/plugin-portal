package com.zestarr.pluginportal.managers;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.type.LocalPlugin;
import com.zestarr.pluginportal.type.PreviewingPlugin;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class DownloadManager {

    private String USER_AGENT = "github.com/Zestarr/PluginPortal";

    private PluginPortal portal;

    public DownloadManager(PluginPortal portal) {
        this.portal = portal;
    }

    public LocalPlugin download(PreviewingPlugin plugin, String fileName) {
        try {
            URL url = new URL("https://api.spiget.org/v2/resources/" + plugin.getId() + "/download");
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            String contentDisposition = connection.getHeaderField("Content-Disposition");

            if (!fileName.endsWith(".jar")) {
                fileName = fileName + ".jar";
            }

            InputStream inputStream = connection.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(new File("plugins", fileName));

            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            LocalPlugin localPlugin = new LocalPlugin(plugin, fileName);
            portal.getLocalPluginManager().add(localPlugin);
            return localPlugin;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LocalPlugin update(LocalPlugin plugin) {
        portal.getLocalPluginManager().getPlugins().remove(plugin.getPreviewingPlugin().getSpigotName());
        return download(plugin.getPreviewingPlugin(), plugin.findFileName());
    }

    public LocalPlugin download(PreviewingPlugin plugin) {
        return download(plugin, plugin.getSpigotName() + ".jar");
    }

}