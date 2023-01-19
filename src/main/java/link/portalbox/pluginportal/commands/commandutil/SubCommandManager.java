package link.portalbox.pluginportal.commands.commandutil;

import link.portalbox.pluginportal.utils.ChatUtil;
import org.bukkit.command.CommandSender;

public abstract class SubCommandManager {

    public boolean execute(CommandSender sender, String[] args, SubCommandEnum subCommandEnum) {
        if (sender.hasPermission(subCommandEnum.getPermission())) {
            execute(sender, args);
            return true;
        } else {
            sender.sendMessage(ChatUtil.format("No Permission."));
        }

        return false;
    }

    public abstract void execute(CommandSender sender, String[] args);
}