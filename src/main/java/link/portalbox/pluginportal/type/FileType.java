package link.portalbox.pluginportal.type;

public enum FileType {

    JAR(".jar", true),
    EXTERNAL("External", false),
    ZIP(".zip", false),
    SKRIPT(".sk", false);

    private final String extension;
    private final boolean supported;

    FileType(String extension, boolean supported) {
        this.extension = extension;
        this.supported = supported;
    }

    public String getExtension() {
        return extension;
    }

    public boolean isSupported() {
        return supported;
    }
}