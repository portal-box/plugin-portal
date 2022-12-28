package com.zestarr.pluginportal.utils;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.types.LocalPlugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class JsonUtils {

    public static void saveData() {
        try {
            Moshi moshi = new Moshi.Builder()
                    .build();
            JsonAdapter<Map<String, LocalPlugin>> jsonAdapater = moshi.adapter(Types.newParameterizedType(Map.class, String.class, LocalPlugin.class));
            String json = jsonAdapater.toJson(PluginPortal.getDataManager().getInstalledPlugins());
            System.out.println(json);
            BufferedWriter writer = new BufferedWriter(new FileWriter(PluginPortal.getDataManager().createPluginDataFile()));
            writer.write(json);
            writer.flush();
            writer.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void loadData() {
        try {
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Map<String, LocalPlugin>> jsonAdapater = moshi.adapter(Types.newParameterizedType(Map.class, String.class, LocalPlugin.class));
            BufferedReader reader = new BufferedReader(new FileReader(ConfigUtils.createPluginDataFile()));
            String json = reader.readLine();
            Map<String, LocalPlugin> map = jsonAdapater.fromJson(json);
            System.out.println(map);
            if (!(map == null)) {
                PluginPortal.getDataManager().getInstalledPlugins().putAll(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}