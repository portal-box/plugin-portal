package com.zestarr.pluginportal.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.google.gson.JsonObject;
import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.types.LocalPlugin;
import com.zestarr.pluginportal.types.OnlinePlugin;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUtils {

    private static String userAgent = "github.com/Zestarr/PluginPortal";

    public static void download(OnlinePlugin onlinePlugin, URL url, File folder) {
        try {

            URLConnection connection = url.openConnection();

            // Set the user agent to "Java" to identify the download as coming from a Java application
            connection.setRequestProperty("User-Agent", userAgent);

            // Get the "Content-Disposition" header field
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

            if (fileName == null) {
                fileName = onlinePlugin.getDefaultFileName();
                if (!fileName.endsWith(".jar")) {
                    fileName = fileName + ".jar";
                }
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

            System.out.println("&7&l[&b&lPPM&7&l] &8&l> Downloaded " + fileName + " to " + folder.getName());
            LocalPlugin localPlugin = new LocalPlugin(onlinePlugin);

            if (PluginPortal.getDeveloperMode()) {
                if (folder == ConfigUtils.getDebugPluginFolder()) {
                    return;
                }
            }

            PluginPortal.getDataManager().getInstalledPlugins().put(localPlugin.getOnlinePlugin().getDisplayName(), localPlugin);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void download(OnlinePlugin onlinePlugin, File folder) {
        try {
            download(onlinePlugin, new URL(onlinePlugin.getDownloadLink()), folder);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void download(OnlinePlugin onlinePlugin) {
        download(onlinePlugin, ConfigUtils.getPluginFolder());
    }

    public static void download(OnlinePlugin plugin, URL url) {
        download(plugin, url, ConfigUtils.getPluginFolder());
    }

    public static void downloadData(URL url, File file) {
        try {
            // Open a connection to the URL
            URLConnection connection = url.openConnection();

            // Set the user agent to "Java" to identify the download as coming from a Java application
            connection.setRequestProperty("User-Agent", userAgent);

            // Get an input stream for reading the data
            InputStream inputStream = connection.getInputStream();

            // Create a file output stream to write the data to the file
            FileOutputStream outputStream = new FileOutputStream(file);

            // Create a buffered writer to write the data to the file
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

            // Read the data and write it to the file
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }

            // Close the input and output streams
            inputStream.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void downloadData(String url, File file) {
        try {
            downloadData(new URL(url), file);
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
    }

    public static void downloadUniversalPlugin(OnlinePlugin plugin) {
        downloadUniversalPlugin(plugin, ConfigUtils.getPluginFolder());
    }

    public static void downloadUniversalPlugin(OnlinePlugin plugin, File folder) {
        String url = plugin.getDownloadLink();
        URL downloadURL = null;

        try {
            downloadURL = new URL(url);
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }

        if (downloadURL == null) { return; }

        // Download from SpigotMC
        if (url.contains("https://www.spigotmc.org/resources/")) {
            try {
                download(plugin, new URL("https://api.spiget.org/v2/resources/" + StringUtils.extractNumbers(url) + "/download"), folder);
            } catch (MalformedURLException exception) {
                exception.printStackTrace();
            }

        } else if (url.contains("https://github.com") && url.endsWith(".jar")) {
            download(plugin, downloadURL, folder);
        } else {
            download(plugin, downloadURL, folder);
        }
    }

}
