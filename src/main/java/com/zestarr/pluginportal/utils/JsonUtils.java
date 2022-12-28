package com.zestarr.pluginportal.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.zestarr.pluginportal.types.LocalPlugin;
import com.zestarr.pluginportal.utils.LocalPluginDeserializer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalPlugin.class, new LocalPluginDeserializer());
        OBJECT_MAPPER.registerModule(module);
    }

    public static void saveData(Map<String, LocalPlugin> data, String filePath) {
        try {
            String json = OBJECT_MAPPER.writeValueAsString(data);

            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(json);
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static Map<String, LocalPlugin> loadData(String filePath) {
        try {

            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String json = reader.readLine();
            reader.close();

            return OBJECT_MAPPER.readValue(json, new TypeReference<Map<String, LocalPlugin>>() {
            });
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }
}