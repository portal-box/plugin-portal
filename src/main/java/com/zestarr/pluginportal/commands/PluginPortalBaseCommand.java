package com.zestarr.pluginportal.commands;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.commands.commandutil.SubCommandEnum;
import com.zestarr.pluginportal.commands.subcommands.*;
import com.zestarr.pluginportal.commands.commandutil.CommandManager;
import com.zestarr.pluginportal.utils.FlagUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PluginPortalBaseCommand extends CommandManager {

    private final PluginPortal plugin;

    private final HelpSubCommand helpSubCommand = new HelpSubCommand();
    private final InstallSubCommand installSubCommand = new InstallSubCommand();
    private final ListSubCommand listSubCommand = new ListSubCommand();
    private final PreviewSubCommand previewSubCommand = new PreviewSubCommand();
    private final SettingsSubCommand settingsSubCommand = new SettingsSubCommand();
    private final UpdateSubCommand updateSubCommand = new UpdateSubCommand();

    public PluginPortalBaseCommand(PluginPortal plugin) {
        super(
                "ppm",
                new String[]{"PluginPackagemanager"},
                "Primary command to handle the Plugin Portal Package Manager",
                "ppm.primarycommand"
        );

        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            helpSubCommand.execute(sender, args, SubCommandEnum.HELP);
        } else {
            switch (args[0].toLowerCase()) {
                case "install" -> installSubCommand.execute(sender, args, SubCommandEnum.INSTALL);
                case "list" -> listSubCommand.execute(sender, args, SubCommandEnum.LIST);
                case "preview" -> previewSubCommand.execute(sender, args, SubCommandEnum.PREVIEW);
                case "settings" -> settingsSubCommand.execute(sender, args, SubCommandEnum.SETTINGS);
                case "update" -> updateSubCommand.execute(sender, args, SubCommandEnum.UPDATE);
                default -> helpSubCommand.execute(sender, args, SubCommandEnum.HELP);
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {

        if (args.length == 1) {
            List<String> completions = new ArrayList<>(Arrays.stream(SubCommandEnum.values()).map(subCommandEnum -> subCommandEnum.getCommand().toLowerCase()).toList());
            return StringUtil.copyPartialMatches(args[0], completions, new ArrayList<>());
        }

        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "install", "preview" -> {
                    if (args[1].length() <= 2) return List.of("Keep Typing...", args[1]);
                    return StringUtil.copyPartialMatches(args[1], plugin.getMarketplaceManager().getAllNames(), new ArrayList<>());
                }
                case "update" -> {
                    return StringUtil.copyPartialMatches(args[1], plugin.getLocalPluginManager().getAllNames(), new ArrayList<>());
                }
                default -> {
                    return null;
                }
            }
        }

        if (args.length == 3) {
            try {
                return StringUtil.copyPartialMatches(args[2], FlagUtil.getFlagStrings(SubCommandEnum.valueOf(args[0].toUpperCase())), new ArrayList<>());
            } catch (NullPointerException exception) {
                return null;
            }
        }
        return null;
    }
}