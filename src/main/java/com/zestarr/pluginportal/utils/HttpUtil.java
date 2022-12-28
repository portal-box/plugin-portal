package com.zestarr.pluginportal.utils;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.type.LocalPlugin;
import org.bukkit.Bukkit;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zestarr.pluginportal.utils.ChatUtils.format;

public class HttpUtil {

    private static String userAgent = "github.com/Zestarr/PluginPortal";

    public static void download(onlinePlugin, URL url, File folder) {
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

            Bukkit.getConsoleSender().sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> Downloaded " + fileName + " to " + folder.getName()));
            LocalPlugin localPlugin = new LocalPlugin(onlinePlugin);
            localPlugin.setInstalled(true);
            PluginPortal.getDataManager().getInstalledPlugins().put(localPlugin.getOnlinePlugin().getDisplayName(), localPlugin);
            JsonUtils.saveData();

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

    public static void downloadPlugin(int id) {
        try {
            download(new URL("https://api.spiget.org/v2/resources/" + id + "/download"));
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
    }

    private static String extractNumbers(String input) {
        // Use a regular expression to find all digits in the input string
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(input);

        // Append all the digits to a StringBuilder
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            sb.append(m.group());
        }

        // Return the result as a string
        return sb.toString();
    }

}
