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

    public LocalPlugin downloadPlugin(int id, String spigotName) {
        try {
            return download(id, spigotName, new URL("https://api.spiget.org/v2/resources/" + id + "/download"), new File("plugins"));
        } catch (MalformedURLException exception) {
            return null;
        }
    }

    private LocalPlugin download(int id, String spigotName, URL url, File folder) {
        try {

            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            String contentDisposition = connection.getHeaderField("Content-Disposition");

            // Extract the default file name from the "Content-Disposition" header field
            String fileName = null;
            if (contentDisposition != null) {
                String[] parts = contentDisposition.split(";");
                for (String part : parts) {
                    if (part.trim().startsWith("filename=")) {
                        fileName = part.substring(part.indexOf('=') + 1);
                        if (fileName.startsWith("\"") && fileName.endsWith("\"")) {
                            fileName = fileName.substring(1, fileName.length() - 1);
                        }
                        break;
                    }
                }
            }


            fileName = spigotName;
            if (!fileName.endsWith(".jar")) {
                fileName = fileName + ".jar";
            }


            // Get an input stream for reading the file
            InputStream inputStream = connection.getInputStream();

            // Create a file output stream to save the file to disk
            FileOutputStream outputStream = new FileOutputStream(new File(folder, fileName));

            // Read the file and save it to disk
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Close the input and output streams
            inputStream.close();
            outputStream.close();

            LocalPlugin localPlugin = new LocalPlugin(id, spigotName, "", new PreviewingPlugin(id).getVersion());
            portal.getLocalPluginManager().add(localPlugin);
            return localPlugin;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}