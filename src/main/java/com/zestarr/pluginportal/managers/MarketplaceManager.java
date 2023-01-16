package com.zestarr.pluginportal.managers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.utils.JsonUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MarketplaceManager {

    private final HashMap<Integer, String> marketplaceCache = new HashMap<>();
    private final String USER_AGENT = "github.com/Zestarr/PluginPortal";

    public MarketplaceManager(PluginPortal portal) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonData = mapper.readTree(JsonUtil.getPluginJson());

        for (Iterator<Map.Entry<String, JsonNode>> it = jsonData.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            marketplaceCache.put(Integer.parseInt(entry.getKey()), entry.getValue().asText());
        }
    }

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

    public Collection<String> getAllNames() {
        return marketplaceCache.values();
    }

    public HashMap<Integer, String> getMarketplaceCache() {
        return marketplaceCache;
    }

    public int getId(String spigotName) {
        for (Integer number : marketplaceCache.keySet()) {
            if (marketplaceCache.get(number).equals(spigotName)) {
                return number;
            }
        }
        return -1;
    }

}