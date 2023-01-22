package link.portalbox.pluginportal.utils;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import link.portalbox.pluginportal.PluginPortal;
import link.portalbox.pluginportal.type.LocalPlugin;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

public class FileUtil {

    public static String getSHA256(File file) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            // this should never happen
            e.printStackTrace();
            return null;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] dataBytes = new byte[1024];

            int nread;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        byte[] digest = md.digest();

        // convert the byte to hex format
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static void saveData(PluginPortal plugin) {
        try {
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Map<String, LocalPlugin>> jsonAdapater = moshi.adapter(Types.newParameterizedType(Map.class, String.class, LocalPlugin.class));
            String json = jsonAdapater.toJson(plugin.getLocalPluginManager().getPlugins());
            BufferedWriter writer = new BufferedWriter(new FileWriter(plugin.getLocalPluginManager().getDataFile()));
            writer.write(json);
            writer.flush();
            writer.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void loadData(PluginPortal plugin, File dataFile) {
        try {
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Map<String, LocalPlugin>> jsonAdapater = moshi.adapter(Types.newParameterizedType(Map.class, String.class, LocalPlugin.class));
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            Map<String, LocalPlugin> map = jsonAdapater.fromJson(reader.readLine());
            if (map != null) {
                plugin.getLocalPluginManager().getPlugins().putAll(map);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        scanDeletedPlugins(plugin);
    }

    public static void scanDeletedPlugins(PluginPortal plugin) {
        ArrayList<LocalPlugin> deletedPlugins = new ArrayList<>();
        for (LocalPlugin localPlugin : plugin.getLocalPluginManager().getPlugins().values()) {
            boolean exists = false;
            for (File file : plugin.getDataFolder().getParentFile().listFiles()) {
                if (file.getPath().endsWith(".jar")) {
                    if (FileUtil.getSHA256(file).equals(localPlugin.getSha256())) {
                        localPlugin.setFileName(file.getName());
                        exists = true;
                    }
                }
            }

            if (!exists) {
                deletedPlugins.add(localPlugin);
                System.out.println("Deleted plugin: " + localPlugin.getPreviewingPlugin().getSpigotName());
            }

        }

        for (LocalPlugin localPlugin : deletedPlugins) {
            plugin.getLocalPluginManager().getPlugins().remove(localPlugin.getPreviewingPlugin().getSpigotName());
        }

        saveData(plugin);
    }


}