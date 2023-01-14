package com.zestarr.pluginportal.commands.ppmSubCommands;

import com.zestarr.pluginportal.commands.commandUtility.SubCommandEnum;
import com.zestarr.pluginportal.commands.commandUtility.SubCommandManager;
import com.zestarr.pluginportal.utils.ChatUtil;
import org.bukkit.command.CommandSender;

public class HelpSubCommand extends SubCommandManager {
    @Override
    public void execute(CommandSender sender, String[] args, SubCommandEnum subCommandEnum) {
        sender.sendMessage(ChatUtil.format("&8<---------------------- &7[&b&lPPM&7]&8 ---------------------->\n"));
        sender.sendMessage(" "); // \n didnt work???

        for (SubCommandEnum subEnum : SubCommandEnum.values()) {
            sender.sendMessage(ChatUtil.format("&8- &b&l" + subEnum.getCommand() + "&8: &7" + subEnum.getCommandUsage()));
        }
        sender.sendMessage(ChatUtil.format("\n&8-----------------------------------------------------"));
    }
}
