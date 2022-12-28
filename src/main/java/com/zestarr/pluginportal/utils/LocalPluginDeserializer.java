package com.zestarr.pluginportal.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.zestarr.pluginportal.types.LocalPlugin;
import com.zestarr.pluginportal.types.OnlinePlugin;

import java.io.File;
import java.io.IOException;

public class LocalPluginDeserializer extends StdDeserializer<LocalPlugin> {
    public LocalPluginDeserializer() {
        this(null);
    }

    public LocalPluginDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalPlugin deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        OnlinePlugin onlinePlugin = jsonParser.getCodec().treeToValue(node.get("onlinePlugin"), OnlinePlugin.class);
        boolean isInstalled = node.get("isInstalled").asBoolean();
        File file = jsonParser.getCodec().treeToValue(node.get("file"), File.class);

        LocalPlugin localPlugin = new LocalPlugin(onlinePlugin);
        localPlugin.setInstalled(isInstalled);
        localPlugin.setFile(file);
        
        return localPlugin;
    }
}