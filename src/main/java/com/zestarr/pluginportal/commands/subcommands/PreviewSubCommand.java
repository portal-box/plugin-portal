package com.zestarr.pluginportal.commands.subcommands;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.commands.commandutil.SubCommandManager;
import com.zestarr.pluginportal.managers.MarketplaceManager;
import com.zestarr.pluginportal.type.PreviewingPlugin;
import com.zestarr.pluginportal.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PreviewSubCommand extends SubCommandManager {
    @Override
    public void execute(CommandSender sender, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(PluginPortal.getMainInstance(), () -> {
            MarketplaceManager marketplaceManager = PluginPortal.getMainInstance().getMarketplaceManager();

            if (args.length <= 1) {
                sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cPlease specify a plugin to preview!"));
                return;
            }

            String spigotName = args[1];

            if (!marketplaceManager.getAllNames().contains(spigotName)) {
                sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cPlugin does not exist."));
                return;
            }

            int id = marketplaceManager.getId(spigotName);
            PreviewingPlugin previewingPlugin = new PreviewingPlugin(id);

            if (!marketplaceManager.getAllNames().contains(spigotName)) {
                sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cPlugin does not exist."));
                return;
            }

            previewingPlugin.sendPreview((Player) sender, true);
        });
    }
}