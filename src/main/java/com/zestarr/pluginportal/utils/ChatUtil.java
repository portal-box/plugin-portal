package com.zestarr.pluginportal.utils;

import org.bukkit.ChatColor;

public class ChatUtil {

    public static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
