package link.portalbox.pluginportal.commands.subcommands;

import link.portalbox.pluginportal.commands.commandutil.SubCommandEnum;
import link.portalbox.pluginportal.commands.commandutil.SubCommandManager;
import link.portalbox.pluginportal.utils.ChatUtil;
import org.bukkit.command.CommandSender;

public class HelpSubCommand extends SubCommandManager {
    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(ChatUtil.format("&8<---------------------- &7[&b&lPP&7]&8 ---------------------->\n"));
        sender.sendMessage(" "); // \n didnt work???

        for (SubCommandEnum subEnum : SubCommandEnum.values()) {
            sender.sendMessage(ChatUtil.format("&8- &b&l" + subEnum.getCommand() + "&8: &7" + subEnum.getCommandUsage()));
        }
        sender.sendMessage(ChatUtil.format("\n&8-----------------------------------------------------"));
    }
}