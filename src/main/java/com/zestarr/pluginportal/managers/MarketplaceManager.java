package com.zestarr.pluginportal.managers;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.utils.HttpUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MarketplaceManager {

    private final HashMap<Integer, String> marketplaceCache = new HashMap<>();

    public MarketplaceManager(PluginPortal portal) throws IOException {
        File file = new File(portal.getDataFolder(), "temp.yml");
        file.createNewFile();
        HttpUtil.downloadData("https://raw.githubusercontent.com/Zestarr/PluginPortal/master/PluginsList.yml", file = new File("temp.yml"));
        YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
        for (String id : data.getConfigurationSection("").getKeys(false)) {
            marketplaceCache.put(Integer.parseInt(id), data.getString(id));
        }
        file.delete();
    }

    public Collection<String> getAllNames() { return marketplaceCache.values(); }

}