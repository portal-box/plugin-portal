package link.portalbox.pluginportal.commands.subcommands;

import link.portalbox.manager.MarketplaceManager;
import link.portalbox.pluginportal.PluginPortal;
import link.portalbox.pluginportal.commands.commandutil.SubCommandManager;
import link.portalbox.pluginportal.utils.ChatUtil;
import link.portalbox.pluginportal.utils.PreviewUtil;
import link.portalbox.type.SpigetPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PreviewSubCommand extends SubCommandManager {
    @Override
    public void execute(CommandSender sender, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(PluginPortal.getMainInstance(), () -> {
            MarketplaceManager marketplaceManager = PluginPortal.getMainInstance().getPortalAPI().getMarketplaceManager();

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
            SpigetPlugin spigetPlugin = new SpigetPlugin(id);

            if (!marketplaceManager.getAllNames().contains(spigotName)) {
                sender.sendMessage(ChatUtil.format("&7&l[&b&lPPM&7&l] &8&l> &cPlugin does not exist."));
                return;
            }

            PreviewUtil.sendPreview((Player) sender, spigetPlugin, true);
        });
    }
}