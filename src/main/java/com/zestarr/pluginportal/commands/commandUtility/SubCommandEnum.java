package com.zestarr.pluginportal.commands.commandUtility;


import java.util.List;

public enum SubCommandEnum {
    HELP("Help", "/ppm help", "pluginportal.ppm.help", null),
    INSTALL("Install", "/ppm install (Plugin Name) <Optional Flags>", "pluginportal.ppm.install", List.of(Flags.FORCE)),
    LIST("List", "/ppm list", "pluginportal.ppm.list", null),
    PREVIEW("Preview", "/ppm preview (Plugin Name)", "pluginportal.ppm.preview", null),
    SETTINGS("Settings", "/ppm settings", "pluginportal.ppm.settings", null),
    UPDATE("Update", "/ppm update (Plugin Name) <Optional Flags>", "pluginportal.ppm.update", List.of(Flags.FORCE));


    SubCommandEnum(String command, String commandUsage, String permission, List<Flags> flags) {
        this.command = command;
        this.commandUsage = commandUsage;
        this.permission = permission;
        this.flags = flags;
    }

    private String command;
    private String commandUsage;
    private String permission;
    private List<Flags> flags;

    public String getCommand() {
        return command;
    }

    public String getCommandUsage() {
        return commandUsage;
    }

    public String getPermission() {
        return permission;
    }

    public List<Flags> getFlags() {
        return flags;
    }

}
