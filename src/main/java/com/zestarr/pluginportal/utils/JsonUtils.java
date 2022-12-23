package com.zestarr.pluginportal.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zestarr.pluginportal.types.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class JsonUtils {

    public static void writeMapToJson(HashMap<String, Plugin> map, File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, map);
    }

    public static HashMap<String, Plugin> readMapFromJson(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, new TypeReference<HashMap<String, Plugin>>(){});
    }
}