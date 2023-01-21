package link.portalbox.pluginportal.commands.commandutil;

public enum Flags {

    FORCE(new String[]{"-f", "--force"});

    private final String[] flagsString;

    Flags(String[] flagsString) {
        this.flagsString = flagsString;
    }

    public String[] getFlagsString() {
        return flagsString;
    }
}