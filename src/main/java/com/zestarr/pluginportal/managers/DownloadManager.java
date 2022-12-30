package com.zestarr.pluginportal.managers;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.type.LocalPlugin;
import com.zestarr.pluginportal.type.PreviewingPlugin;
import com.zestarr.pluginportal.utils.SpigetUtil;
import org.bukkit.Bukkit;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class DownloadManager {

    private String USER_AGENT = "github.com/Zestarr/PluginPortal";

    private PluginPortal portal;

    public DownloadManager(PluginPortal portal) {
        this.portal = portal;
    }

    private LocalPlugin download(PreviewingPlugin plugin) {
        try {
            URL url = new URL("https://api.spiget.org/v2/resources/" + plugin.getId() + "/download");
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            String contentDisposition = connection.getHeaderField("Content-Disposition");

            String fileName = plugin.getSpigotName();
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

}