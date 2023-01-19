package link.portalbox.pluginportal.commands.subcommands;

import link.portalbox.pluginportal.PluginPortal;
import link.portalbox.pluginportal.commands.commandutil.SubCommandManager;
import link.portalbox.pluginportal.type.LocalPlugin;
import link.portalbox.pluginportal.utils.ChatUtil;
import org.bukkit.command.CommandSender;

public class ListSubCommand extends SubCommandManager {
    @Override
    public void execute(CommandSender sender, String[] args) {

        if (PluginPortal.getMainInstance().getLocalPluginManager().getPlugins().values().size() == 0) {
            sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &7You have no plugins installed via Plugin Portal."));
            return;
        }

        sender.sendMessage(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &7Listing all installed plugins..."));
        for (LocalPlugin plugin : PluginPortal.getMainInstance().getLocalPluginManager().getPlugins().values()) {
            if (plugin.getPreviewingPlugin().getSpigotName() != null && !plugin.getPreviewingPlugin().getSpigotName().isEmpty()) {
                sender.sendMessage(ChatUtil.format(" &a+ &7" + plugin.getPreviewingPlugin().getSpigotName()));
            }
        }
    }
}