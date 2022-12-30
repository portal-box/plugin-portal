package com.zestarr.pluginportal.commands;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.listeners.PluginStatusListener;
import com.zestarr.pluginportal.type.LocalPlugin;
import com.zestarr.pluginportal.type.PreviewingPlugin;
import com.zestarr.pluginportal.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PPMCommand implements CommandExecutor, TabCompleter {

    private final PluginPortal portal;

    public PPMCommand(PluginPortal portal) {
        this.portal = portal;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            System.out.println(portal.getLocalPluginManager().getPlugins().values());
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &7Listing all plugins..."));
            for (LocalPlugin plugin : portal.getLocalPluginManager().getPlugins().values()) {
                if (plugin.getSpigotName() != null && !plugin.getSpigotName().isEmpty()) {
                    sender.sendMessage(ChatUtil.format(" &a+ &7" + plugin.getSpigotName()));
                }
            }
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cUsage: /ppm <arg> <plugin>"));
            return false;
        }

        String spigotName = args[1];
        int id = portal.getMarketplaceManager().getId(spigotName);
        if (!portal.getMarketplaceManager().getAllNames().contains(spigotName)) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cThere is no plugin with this name. Use tab complete to find all usable plugins."));
            return false;
        }

        switch (args[0]) {
            case "preview":
                PreviewingPlugin previewingPlugin = new PreviewingPlugin(id);
                ArrayList<String> information = new ArrayList<>();
                try {
                    information.add("Name: " + previewingPlugin.getSpigotName());
                    information.add("Description: " + previewingPlugin.getTag());
                    information.add("Downloads: " + previewingPlugin.getDownloads());
                    information.add("Rating: " + previewingPlugin.getRating());
                    information.add("File Size: " + previewingPlugin.getFileSize() + previewingPlugin.getSizeUnit().getUnit());
                    information.add("File Type: " + previewingPlugin.getFileType().getExtension());
                    information.add("Supported: " + previewingPlugin.getFileType().isSupported());
                } catch (Exception exception) {
                    exception.printStackTrace();
                    information.add("Error, ID: " + id + ". Please report this to our discord.");
                }

                sender.sendMessage(ChatUtil.format("&8------------------------&7[&b&lPPM&7]&8------------------------"));
                for (String s : information) {
                    try {
                        sender.sendMessage(ChatUtil.format(" &8- &7" + s.replaceAll(":", ":&b")));
                    } catch (NullPointerException exception) {
                        sender.sendMessage(ChatUtil.format(" &8- &7" + s.replaceAll(":", ":&b") + "null/error"));
                    }
                }
                sender.sendMessage(ChatUtil.format("&8-----------------------------------------------------"));


                    break;
            case "install":
                if (portal.getLocalPluginManager().isInstalled(spigotName)) {
                    sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &c" + spigotName + " is already installed. Did you mean to run /ppm update " + spigotName + "?"));
                    return false;
                }

                if (new PreviewingPlugin(id).isPremium()) {
                    sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cThis plugin is premium. Please purchase it on spigotmc.org to install it."));
                    return false;
                }

                sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cStarting to download " + spigotName + "..."));

                Bukkit.getScheduler().runTaskAsynchronously(portal, () -> {
                    LocalPlugin plugin = portal.getDownloadManager().downloadPlugin(portal.getMarketplaceManager().getId(spigotName), spigotName);
                    if (plugin == null) {
                        sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cThere was an error installing " + spigotName + "."));
                        return;
                    }
                    sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &c" + spigotName + " (version " + plugin.getVersion() + ") has been installed."));
                });
                break;
            case "uninstall":
                try {

                    sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &c&lWARNING: THIS DOES NOT WORK."));
                    if (portal.getPluginStatusListener().getPluginMap().containsKey(spigotName)) {
                        Bukkit.getPluginManager().disablePlugin(portal.getPluginStatusListener().getPluginMap().get(spigotName));
                        portal.getPluginStatusListener().getCanDelete().put(portal.getPluginStatusListener().getPluginMap().get(spigotName), true);

                    } else {
                        sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &c" + args[1] + " is not installed, or was installed using third-party means."));
                    }
                } catch (Exception exception) {
                    sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cThere was an error uninstalling " + args[1] + "."));
                }

                break;
            case "update":

                if (portal.getLocalPluginManager().getPlugins().get(spigotName) == null) {
                    sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &c" + spigotName + " is not installed. Did you mean to run /ppm install " + spigotName + "?"));
                    return false;
                }

                LocalPlugin plugin = portal.getLocalPluginManager().getPlugins().get(spigotName);
                if (plugin.matchesVersion(new PreviewingPlugin(id).getVersion())) {
                    sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &c" + spigotName + " is already up to date."));
                    return false;
                } else {
                    File file = new File("plugins", spigotName + ".jar");
                    file.delete();
                    // def will cause no issues/errors! we need a better detection system for deleting plugins btw
                    sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &c" + spigotName + " has been updated to version " + new PreviewingPlugin(id).getVersion() + "."));
                }

                break;
        }

//        if (args.length >= 1) {
//            if (args[0].equalsIgnoreCase("install")) {
//                if (args[1] == null) {
//                    sender.sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> &cPlease specify a plugin to install!"));
//                    return true;
//                }
//                Map<String, OnlinePlugin> plugins = getPluginManager().getPlugins();
//
//                if (plugins.get(args[1]) == null) {
//                    sender.sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> &cPlugin Not Found! &7Plugin List: "));
//
//                    for (OnlinePlugin plugin : plugins.values()) {
//                        sender.sendMessage(format("&a + &7" + plugin.getDisplayName()));
//                    }
//
//                    return true;
//                } else {
//
//                    if (PluginPortal.getDataManager().isPluginInstalled(PluginPortal.getPluginManager().getPlugins().get(args[1]))) {
//                        if (args.length == 3 && args[2].equalsIgnoreCase("-f")) {
//                            sender.sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> &cPlugin already downloaded, but forcing install..."));
//                            HttpUtils.downloadUniversalPlugin(getPluginManager().getPlugins().get(args[1]));
//                            sender.sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> &7Plugin has been downloaded!"));
//                        }
//                        sender.sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> &cPlugin already installed! &7Use &c/ppm install " + args[1] + " -f &7to force install!"));
//                        return true;
//                    } else {
//                        HttpUtils.downloadUniversalPlugin(getPluginManager().getPlugins().get(args[1]));
//                        sender.sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> &7Plugin has been downloaded!"));
//                    }
//                }
//            } else if (args[0].equalsIgnoreCase("uninstall")) {
//
//                if (PluginPortal.getDataManager().getInstalledPlugins().containsKey(args[1])) {
//                    File file = new File(getPluginFolder(), args[1] + ".jar");
//                    file.delete();
//                    sender.sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> &7Plugin has been uninstalled!"));
//                    PluginPortal.getDataManager().getInstalledPlugins().get(args[1]).setInstalled(false);
//                    JsonUtils.saveData();
//
//                } else if (new File(getPluginFolder(), args[1] + ".jar").exists()) {
//                    File file = new File(getPluginFolder(), args[1] + ".jar");
//                    sender.sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> &7Uninstalled Third-Party plugin... &8(" + file.getName() + ")"));
//                    file.delete();
//
//                } else {
//                    sender.sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> &cPlugin not installed..."));
//                }
//            } else if (args[0].equalsIgnoreCase("list")) {
//                sender.sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> &7Plugin List: "));
//
//                for (LocalPlugin plugin : PluginPortal.getDataManager().getInstalledPlugins().values()) {
//                    sender.sendMessage(format(plugin.isInstalled() ? "&c- &7PluginName &7(&aInstalled&7)" : "&a+ &7PluginName &7(&cUninstalled&7)").replace("PluginName", plugin.getOnlinePlugin().getDisplayName()));
//                }
//            } else if (args[0].equalsIgnoreCase("update")) {
//                sender.sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> &cThis Feature may not work correctly."));
//                if (PluginPortal.getDataManager().isPluginInstalled(PluginPortal.getPluginManager().getPlugins().get(args[1]))) {
//                    try {
//                        PluginPortal.getPluginManager().updatePlugin(PluginPortal.getDataManager().getInstalledPlugins().get(args[1]));
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    sender.sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> &7Updated plugin..."));
//                } else {
//                    sender.sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> &cPlugin not installed..."));
//                }
//            } else if (args[0].equalsIgnoreCase("search")) {
//                Map<String, OnlinePlugin> plugins = getPluginManager().getPlugins();
//
//                if (plugins.containsKey(args[1])) {
//                    sender.sendMessage("&7&l[&b&lPPM&7&l] &8&l> &7Plugin Found: &7" + plugins.get(args[1]).getDisplayName());
//                } else {
//                    sender.sendMessage("&7&l[&b&lPPM&7&l] &8&l> &cPlugin not found!");
//                }
//
//            } else {
//                sender.sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> &l&cUsage: /ppm <arg> <plugin>"));
//            }
//
//        } else {
//            sender.sendMessage(format("&7&l[&b&lPPM&7&l] &8&l> &cUsage: /ppm <arg> <plugin>"));
//        }

        return false;

    }


    @Override

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList("install", "update", "list", "uninstall", "preview"), new ArrayList<>());
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "preview":
                case "install":
                    if (args[1].length() <= 2) return List.of("Keep Typing...", args[1]);
                    return StringUtil.copyPartialMatches(args[1], portal.getMarketplaceManager().getAllNames(), new ArrayList<>());
                case "uninstall":
                    return StringUtil.copyPartialMatches(args[1], portal.getPluginStatusListener().getPluginMap().keySet(), new ArrayList<>());
                case "update":
                    return StringUtil.copyPartialMatches(args[1], portal.getLocalPluginManager().getAllNames(), new ArrayList<>());
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("install")) {
            return StringUtil.copyPartialMatches(args[2], Arrays.asList("-f", "--force"), new ArrayList<>());
        }

        return new ArrayList<>();
    }
}
