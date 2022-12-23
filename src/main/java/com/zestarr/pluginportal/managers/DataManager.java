package com.zestarr.pluginportal.managers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.zestarr.pluginportal.types.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {

    private static HashMap<String, Plugin> downloadedPlugins = new HashMap<>();

    public void setupData() {
        createPluginDataFile();
        loadPlugins();
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
        try {
            plugin.setIsInstalled(wasThisInstalled);
            downloadedPlugins.put(plugin.getDisplayName(), plugin);
            Writer writer = new FileWriter(createPluginDataFile(), false);
            Gson gson = new Gson();
            System.out.println(downloadedPlugins);
            gson.toJson(downloadedPlugins.values(), writer);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPlugins() {
        try {
            Gson gson = new Gson();
            Reader reader = new FileReader(createPluginDataFile());
            ArrayList<Object> objectArrayList = gson.fromJson(reader, new TypeToken<List<Plugin>>(){}.getType());
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

    }



}
