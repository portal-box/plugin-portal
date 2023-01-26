package link.portalbox.pluginportal.commands.commandutil;


import java.util.List;

public enum SubCommandEnum {

    DELETE("Delete", "/pp delete (File Name)", "pluginportal.pp.delete", null),
    HELP("Help", "/pp help", "pluginportal.pp.help", null),
    INSTALL("Install", "/pp install (Plugin Name) <Optional Flags>", "pluginportal.pp.install", List.of(Flags.FORCE)),
    LIST("List", "/pp list", "pluginportal.pp.list", null),
    PREVIEW("Preview", "/pp preview (Plugin Name)", "pluginportal.pp.preview", null),
    SETTINGS("Settings", "/pp settings", "pluginportal.pp.settings", null),
    UPDATE("Update", "/pp update (Plugin Name) <Optional Flags>", "pluginportal.pp.update", List.of(Flags.FORCE));

    SubCommandEnum(String command, String commandUsage, String permission, List<Flags> flags) {
        this.command = command;
        this.commandUsage = commandUsage;
        this.permission = permission;
        this.flags = flags;
    }

    private final String command;
    private final String commandUsage;
    private final String permission;
    private final List<Flags> flags;

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