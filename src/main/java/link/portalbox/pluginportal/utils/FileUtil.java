package link.portalbox.pluginportal.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import link.portalbox.pluginportal.PluginPortal;
import link.portalbox.pluginportal.type.LocalPlugin;

import java.io.*;
import java.lang.reflect.Type;
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
        Gson gson = new Gson();
        String json = gson.toJson(plugin.getLocalPluginManager().getPlugins());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(plugin.getLocalPluginManager().getDataFile()))) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData(PluginPortal plugin, File dataFile) {
        Gson gson = new Gson();
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            Type type = new TypeToken<Map<String, LocalPlugin>>(){}.getType();
            Map<String, LocalPlugin> map = gson.fromJson(reader, type);
            if (map != null) {
                plugin.getLocalPluginManager().getPlugins().putAll(map);
            }
        } catch (IOException e) {
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