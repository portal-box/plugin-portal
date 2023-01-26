package link.portalbox.pluginportal.commands.subcommands;

import link.portalbox.pluginportal.PluginPortal;
import link.portalbox.pluginportal.commands.commandutil.Flags;
import link.portalbox.pluginportal.commands.commandutil.SubCommandEnum;
import link.portalbox.pluginportal.commands.commandutil.SubCommandManager;
import link.portalbox.pluginportal.type.LocalPlugin;
import link.portalbox.pluginportal.utils.ChatUtil;
import link.portalbox.pluginportal.utils.FlagUtil;
import link.portalbox.pluginportal.utils.PreviewUtil;
import link.portalbox.type.FileType;
import link.portalbox.type.SpigetPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class InstallSubCommand extends SubCommandManager {

    @Override
    public void execute(CommandSender sender, String[] args) {
        Set<Flags> flags = FlagUtil.getFlags(SubCommandEnum.INSTALL, args);

        if (args.length <= 1) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &cPlease specify a plugin to install!"));
            return;
        }

        String spigotName = args[1];
        int id = PluginPortal.getMainInstance().getPortalAPI().getMarketplaceManager().getId(spigotName);

        if (!PluginPortal.getMainInstance().getPortalAPI().getMarketplaceManager().getAllNames().contains(spigotName)) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &cPlugin does not exist."));
            return;
        }

        SpigetPlugin spigetPlugin = new SpigetPlugin(id);

        if (PluginPortal.getMainInstance().getLocalPluginManager().getPlugins().containsKey(PluginPortal.getMainInstance().getPortalAPI().getMarketplaceManager().getMarketplaceCache().get(id))) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &7Plugin is already installed."));
            return;
        }

        if (spigetPlugin.isPremium()) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &cThis plugin is a premium plugin. Please purchase it on spigotmc.org to install it!"));
            return;
        }

        if (spigetPlugin.getFileType() == FileType.EXTERNAL && !flags.contains(Flags.FORCE)) {
            PreviewUtil.sendPreview((Player) sender, spigetPlugin, true);
            return;
        }

        sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &7Starting to download " + spigotName + "..."));
        Bukkit.getScheduler().runTaskAsynchronously(PluginPortal.getMainInstance(), () -> {
            LocalPlugin plugin = PluginPortal.getMainInstance().getDownloadManager().download(spigetPlugin);
            if (plugin == null) {
                sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &cThere was an error installing " + spigotName + "."));
                return;
            }
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &b" + spigotName + " &7has been installed. Please restart your server for the download to take effect."));
        });
    }
}