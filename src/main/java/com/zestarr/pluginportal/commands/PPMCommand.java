package com.zestarr.pluginportal.commands;

import com.zestarr.pluginportal.commands.ppmSubCommands.*;
import org.bukkit.command.CommandSender;

import java.util.List;

public class PPMCommand extends CommandManager {

    HelpSubCommand helpSubCommand = new HelpSubCommand();
    InstallSubCommand installSubCommand = new InstallSubCommand();
    ListSubCommand listSubCommand = new ListSubCommand();
    PreviewSubCommand previewSubCommand = new PreviewSubCommand();
    SettingsSubCommand settingsSubCommand = new SettingsSubCommand();
    UpdateSubCommand updateSubCommand = new UpdateSubCommand();


    public PPMCommand() {
        super(
                "ppm",
                new String[]{"PluginPackagemanager"},
                "Primary command to handle the Plugin Portal Package Manager",
                "ppm.primarycommand"
        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length < 1) {
            helpSubCommand.execute(sender, args, SubCommandEnum.HELP);
        } else {
            switch (args[0].toLowerCase()) {
                case "install":
                    installSubCommand.execute(sender, args, SubCommandEnum.INSTALL);
                    break;

                case "list":
                    listSubCommand.execute(sender, args, SubCommandEnum.LIST);
                    break;

                case "preview":
                    previewSubCommand.execute(sender, args, SubCommandEnum.PREVIEW);
                    break;

                case "settings":
                    settingsSubCommand.execute(sender, args, SubCommandEnum.SETTINGS);
                    break;

                case "update":
                    updateSubCommand.execute(sender, args, SubCommandEnum.UPDATE);
                    break;

                default:
                    helpSubCommand.execute(sender, args, SubCommandEnum.HELP);
                    break;
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
