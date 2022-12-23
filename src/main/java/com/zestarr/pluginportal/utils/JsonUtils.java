package com.zestarr.pluginportal.utils;

import com.google.gson.Gson;
import com.zestarr.pluginportal.types.Plugin;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class JsonUtils {

    public static void writeMapToJson(HashMap<String, Plugin> map, String filePath) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(map, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}