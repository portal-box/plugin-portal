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
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &7Listing all plugins..."));
            for (LocalPlugin plugin : portal.getLocalPluginManager().getPlugins().values()) {
                if (plugin.getPreviewingPlugin().getSpigotName() != null && !plugin.getPreviewingPlugin().getSpigotName().isEmpty()) {
                    sender.sendMessage(ChatUtil.format(" &a+ &7" + plugin.getPreviewingPlugin().getSpigotName()));
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
                    LocalPlugin plugin = portal.getDownloadManager().download(new PreviewingPlugin(id));
                    if (plugin == null) {
                        sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cThere was an error installing " + spigotName + "."));
                        return;
                    }
                    sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &c" + spigotName + " has been installed."));
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
