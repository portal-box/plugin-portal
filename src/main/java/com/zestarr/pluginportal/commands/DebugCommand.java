package com.zestarr.pluginportal.commands;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.type.PreviewingPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Random;

import static com.zestarr.pluginportal.utils.ChatUtil.format;

public class DebugCommand implements CommandExecutor {

    private final PluginPortal plugin;

    public DebugCommand(PluginPortal plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        player.sendMessage("▉");

        if (args.length == 0) {
            sender.sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> &cPlease specify a debug command!"));
            return true;
        }

        if (args[0].equalsIgnoreCase("testdownload")) {
            Random random = new Random();
            for (int i = 0; i < 100; i++) {
                try {
                    HashMap<Integer, String> marketplaceCache = plugin.getMarketplaceManager().getMarketplaceCache();
                    int randomKey = (int) marketplaceCache.keySet().toArray()[random.nextInt(marketplaceCache.size())];
                    sender.sendMessage(format("Attempting to download ID: " + randomKey));
                    plugin.getDownloadManager().download(new PreviewingPlugin(randomKey));
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }

//        if (args.length >= 1) {
//            if (args[0].equalsIgnoreCase("download")) {
//                if (args[1] == null || plugin.getPluginManager().getPlugins().get(args[1]) == null) {
//                    sender.sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> &cPlease specify a plugin to download!"));
//                    return true;
//                } else {
//                    HttpUtils.downloadUniversalPlugin(PluginPortal.getPluginManager().getPlugins().get(args[1]));
//                    sender.sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> &7Plugin has been downloaded!"));
//                    for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
//                        System.out.println("\n---------------------------------------\n");
//                        System.out.println("plugin#getName(): " + plugin.getName());
//                        System.out.println("getDescription#getName(): " + plugin.getDescription().getName());
//                        System.out.println("Version: " + plugin.getDescription().getVersion());
//                        System.out.println("Authors: " + plugin.getDescription().getAuthors());
//                        System.out.println("Description: " + plugin.getDescription().getDescription());
//                    }
//
//
//                }
//            } else {
//                sender.sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> &cPlease specify a debug command!"));
//                return true;
//            }
//        }
//


        return false;
    }
}
