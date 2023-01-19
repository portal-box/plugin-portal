package link.portalbox.pluginportal.commands.subcommands;

import link.portalbox.pluginportal.commands.commandutil.SubCommandManager;
import link.portalbox.pluginportal.utils.ChatUtil;
import org.bukkit.command.CommandSender;

public class SettingsSubCommand extends SubCommandManager {
    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(ChatUtil.format("&7[&bPP&7] &cSettings are not yet implemented."));
    }
}