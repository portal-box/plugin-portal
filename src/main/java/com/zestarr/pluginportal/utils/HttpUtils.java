package com.zestarr.pluginportal.utils;

import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.types.LocalPlugin;
import com.zestarr.pluginportal.types.OnlinePlugin;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    public static void download(OnlinePlugin plugin, File folder) {

        LocalPlugin localPlugin = new LocalPlugin(plugin);

        try {
            URL obj = new URL(plugin.getDownloadLink());
            String fileName = "";
            HttpURLConnection con = (HttpURLConnection)obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "github.com/Zestarr/PluginPortalApp");
            con.setConnectTimeout(5000);
            int responseCode = con.getResponseCode();
            String contentDisposition = con.getHeaderField("Content-Disposition");
            if (contentDisposition != null) {
                String[] parts = contentDisposition.split(";");
                for (String part : parts) {
                    if (!part.trim().startsWith("filename=")) continue;
                    fileName = part.substring(part.indexOf('=') + 1);
                    if (!fileName.startsWith("\"") || !fileName.endsWith("\"")) break;
                    fileName = fileName.substring(1, fileName.length() - 1);
                    break;
                }
            }
            if (fileName.isEmpty()) {
                fileName = plugin.getDisplayName() + ".jar";
                System.out.println("File Name Issue: " + contentDisposition);
            }
            if (responseCode == 200) {
                BufferedInputStream in = new BufferedInputStream(con.getInputStream());
                FileOutputStream fos = new FileOutputStream(folder.getPath() + File.separator + fileName);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                in.close();
                localPlugin.setFile(new File(folder.getPath() + File.separator + fileName));
                PluginPortal.getDataManager().savePluginToFile(plugin, true);
            } else {
                System.out.println("[PPM] DOWNLOAD ERROR: " + responseCode + " | " + plugin.getDownloadLink() + " | " + plugin.getDisplayName() + " | " + plugin.getDisplayName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void copyWebsite(String websiteURL, String filePath) {
        try {
            String line;
            URL website = new URL(websiteURL);
            BufferedReader in = new BufferedReader(new InputStreamReader(website.openStream()));
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            while ((line = in.readLine()) != null) {
                out.write(line);
                out.newLine();
            }
            in.close();
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
