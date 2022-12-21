package com.zestarr.pluginportal.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class JsonUtils {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type MAP_TYPE = new TypeToken<Map<String, Object>>(){}.getType();

    public static Map<String, Object> readJson(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            return GSON.fromJson(reader, MAP_TYPE);
        }
    }

    public static void writeJson(String filePath, Map<String, Object> data) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            GSON.toJson(data, writer);
        }
    }

    public static void updateJson(String filePath, Map<String, Object> updates) throws IOException {
        Map<String, Object> data = readJson(filePath);
        data.putAll(updates);
        writeJson(filePath, data);
    }
}