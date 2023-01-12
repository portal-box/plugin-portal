package com.zestarr.pluginportal.commands;

public enum SubCommandEnum {

    HELP("Help", "/ppm help", "pluginportal.ppm.help", new String[]{}),
    INSTALL("Install", "/ppm install (Plugin Name) <Optional Flags>", "pluginportal.ppm.install", new String[]{"-f", "-force"}),
    LIST("List", "/ppm list", "pluginportal.ppm.list", new String[]{}),
    PREVIEW("Preview", "/ppm preview (Plugin Name)", "pluginportal.ppm.preview", new String[]{}),
    SETTINGS("Settings", "/ppm settings", "pluginportal.ppm.settings", new String[]{}),
    UPDATE("Update", "/ppm update (Plugin Name) <Optional Flags>", "pluginportal.ppm.update", new String[]{"-f", "-force"});


    SubCommandEnum(String command, String commandUsage, String permission, String[] flags) {
        this.command = command;
        this.commandUsage = commandUsage;
        this.permission = permission;
        this.flags = flags;
    }

    private String command;
    private String commandUsage;
    private String permission;
    private String[] flags;

    public String getCommand() {
        return command;
    }

    public String getCommandUsage() {
        return commandUsage;
    }

    public String getPermission() {
        return permission;
    }

    public String[] getFlags() {
        return flags;
    }

}
