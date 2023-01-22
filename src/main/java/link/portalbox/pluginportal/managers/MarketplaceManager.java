package link.portalbox.pluginportal.managers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import link.portalbox.pluginportal.utils.JsonUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class MarketplaceManager {

    private final HashMap<Integer, String> marketplaceCache = new HashMap<>();

    public MarketplaceManager() {
        Gson gson = new Gson();
        JsonElement jsonData = gson.fromJson(JsonUtil.getPluginJson(), JsonElement.class);
        try {
            for (Map.Entry<String, JsonElement> entry : jsonData.getAsJsonObject().entrySet()) {
                marketplaceCache.put(Integer.parseInt(entry.getKey()), entry.getValue().getAsString());
            }
        } catch (Exception ignored) {}
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