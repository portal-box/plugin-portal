package com.zestarr.pluginportal.commands.subcommands;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.commands.commandutil.Flags;
import com.zestarr.pluginportal.commands.commandutil.SubCommandEnum;
import com.zestarr.pluginportal.commands.commandutil.SubCommandManager;
import com.zestarr.pluginportal.type.FileType;
import com.zestarr.pluginportal.type.LocalPlugin;
import com.zestarr.pluginportal.type.PreviewingPlugin;
import com.zestarr.pluginportal.utils.ChatUtil;
import com.zestarr.pluginportal.utils.FlagUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

// TODO: This needs to implement PreviewingPlugin#sendPreview
public class InstallSubCommand extends SubCommandManager {

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandEnum subCommandEnum) {

        Set<Flags> flags = FlagUtil.getFlags(SubCommandEnum.INSTALL, args);

        if (args.length <= 1) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &cPlease specify a plugin to install!"));
            return;
        }

        String spigotName = args[1];
        int id = PluginPortal.getMainInstance().getMarketplaceManager().getId(spigotName);

        if (!PluginPortal.getMainInstance().getMarketplaceManager().getAllNames().contains(spigotName)) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &cPlugin does not exist."));
            return;
        }

        PreviewingPlugin previewingPlugin = new PreviewingPlugin(id);

        if (previewingPlugin.isPremium()) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &cThis plugin is a premium plugin. Please purchase it on spigotmc.org to install it!"));
            return;
        }

        if (previewingPlugin.getFileType() == FileType.EXTERNAL && !flags.contains(Flags.FORCE)) {
            previewingPlugin.sendPreview((Player) sender, true);
            return;
        }

        sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &7Starting to download " + spigotName + "..."));
        Bukkit.getScheduler().runTaskAsynchronously(PluginPortal.getMainInstance(), () -> {
            LocalPlugin plugin = PluginPortal.getMainInstance().getDownloadManager().download(previewingPlugin);
            if (plugin == null) {
                sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &cThere was an error installing " + spigotName + "."));
                return;
            }
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &b" + spigotName + " &7has been installed."));
        });
    }
}