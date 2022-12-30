package com.zestarr.pluginportal.managers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.utils.HttpUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class MarketplaceManager {

    private final HashMap<Integer, String> marketplaceCache = new HashMap<>();

    public MarketplaceManager(PluginPortal portal) throws IOException {
        File file = new File(portal.getDataFolder(), "temp.yml");
        file.createNewFile();
        downloadFile(new URL("https://raw.githubusercontent.com/Zestarr/PluginPortal/master/PluginsList.yml"), file = new File("temp.yml"));
        YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
        for (String id : data.getConfigurationSection("").getKeys(false)) {
            marketplaceCache.put(Integer.parseInt(id), data.getString(id));
        }
        file.delete();
    }

    private String USER_AGENT = "github.com/Zestarr/PluginPortal";

    private void downloadFile(URL url, File file) {
        try {
            // Open a connection to the URL
            URLConnection connection = url.openConnection();

            // Set the user agent to "Java" to identify the download as coming from a Java application
            connection.setRequestProperty("User-Agent", USER_AGENT);

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

    public Collection<String> getAllNames() { return marketplaceCache.values(); }
    public HashMap<Integer, String> getMarketplaceCache() { return marketplaceCache; }

    public int getId(String spigotName) {
        for (Integer number : marketplaceCache.keySet()) {
            if (marketplaceCache.get(number).equals(spigotName)) {
                return number;
            }
        }
        return -1;
    }

}