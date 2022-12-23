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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUtils {

    private static String userAgent = "github.com/Zestarr/PluginPortal";

    public static void download(URL url, File folder) {
        try {

            URLConnection connection = url.openConnection();

            // Set the user agent to "Java" to identify the download as coming from a Java application
            connection.setRequestProperty("User-Agent", userAgent);

            // Get the "Content-Disposition" header field
            String contentDisposition = connection.getHeaderField("Content-Disposition");

            // Extract the default file name from the "Content-Disposition" header field
            String fileName = null;
            if (contentDisposition != null) {
                Pattern pattern = Pattern.compile("filename=\"(.+)\"");
                Matcher matcher = pattern.matcher(contentDisposition);
                if (matcher.find()) {
                    fileName = matcher.group(1);
                }
            }

            // If the file name was not found, use the default file name from the URL
            if (fileName == null) {
                fileName = url.getFile();
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

            System.out.println("File downloaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void download(OnlinePlugin plugin) {
        download(plugin.getDownloadLink());
    }

    public static void download(String url) {
        try {
            download(new URL(url), ConfigUtils.getPluginFolder());
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
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
    public static void download(URL url) {
        download(url, ConfigUtils.getPluginFolder());
    }


    public static void downloadUniversalPlugin(OnlinePlugin plugin) {
        String url = plugin.getDownloadLink();
        // Download from SpigotMC
        if (url.contains("https://www.spigotmc.org/resources/")) {
            download("https://api.spiget.org/v2/resources/" + StringUtils.extractNumbers(url) + "/download");
        } else if (url.contains("https://github.com") && url.endsWith(".jar")) {
            download(url);
        } else {
            download(url);
        }
    }

}
