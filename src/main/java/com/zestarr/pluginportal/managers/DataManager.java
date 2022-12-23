package com.zestarr.pluginportal.managers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.zestarr.pluginportal.types.Plugin;
import com.zestarr.pluginportal.utils.JsonUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {

    private static HashMap<String, Plugin> downloadedPlugins = new HashMap<>();

    public void setupData() throws IOException {
        createPluginDataFile();
        try {
            for (Plugin plugin : JsonUtils.readMapFromJson(createPluginDataFile()).values()) {
                downloadedPlugins.put(plugin.getDisplayName(), plugin);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public File createPluginDataFile() {
        File file = new File("PluginData.json");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return file;
    }

    public Boolean isPluginInstalled(Plugin plugin) {
        return downloadedPlugins.get(plugin.getDisplayName()) != null;
    }

    public Map<String, Plugin> getInstalledPlugins() {
        return downloadedPlugins;
    }

    public void savePluginToFile(Plugin plugin, Boolean wasThisInstalled) {
        plugin.setIsInstalled(wasThisInstalled);
        downloadedPlugins.put(plugin.getDisplayName(), plugin);
        try {
            JsonUtils.writeMapToJson(downloadedPlugins, createPluginDataFile());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void loadPlugins() {
        /*
        System.out.println("loading plugins");
        try {
            Gson gson = new Gson();
            Reader reader = new FileReader(createPluginDataFile());
            System.out.println("heres the problem");
            HashMap<String, Object> objectArrayList = gson.fromJson(reader, new TypeToken<HashMap<Plugin>>(){}.getType());
            if (objectArrayList != null) {
                for (Object object : objectArrayList) {
                    if (object instanceof Plugin) {
                        downloadedPlugins.put(((Plugin) object).getDisplayName(), (Plugin) object);
                    }
                }
            }


            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

         */

    }



}
