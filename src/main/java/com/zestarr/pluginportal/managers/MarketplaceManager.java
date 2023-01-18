package com.zestarr.pluginportal.managers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zestarr.pluginportal.utils.JsonUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MarketplaceManager {

    private final HashMap<Integer, String> marketplaceCache = new HashMap<>();

    public MarketplaceManager() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonData = mapper.readTree(JsonUtil.getPluginJson());

        for (Iterator<Map.Entry<String, JsonNode>> it = jsonData.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            marketplaceCache.put(Integer.parseInt(entry.getKey()), entry.getValue().asText());
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