package com.zestarr.pluginportal.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.google.gson.JsonObject;
import com.zestarr.pluginportal.PluginPortal;
import com.zestarr.pluginportal.types.LocalPlugin;
import com.zestarr.pluginportal.types.OnlinePlugin;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
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
                System.out.println("File Name Issue: " + String.valueOf(con));
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


    // Thanks https://github.com/Lenni0451/SpigotPluginManager for code :)

   // private static final JsonParser jsonParser = new JsonParser();

    /**
     * Open a connection to an url
     *
     * @param url The url to open
     * @return The opened connection or null if the response code is not 200
     * @throws IOException when the connection could not be opened
     */
    public static HttpURLConnection openConnection(final String url) throws IOException {
        HttpsURLConnection.setFollowRedirects(true);
        HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
        connection.setDoInput(true);
        connection.setRequestProperty("user-agent", "github.com/Zestarr/PluginPortalApp");

        if (connection.getResponseCode() != 200) {
            connection.disconnect();
            return null;
        }

        return connection;
    }

    /**
     * Read the bytes from a connection and write them to an output stream
     *
     * @param connection The connection to read from
     * @param os         The output stream to write to
     * @throws IOException when the connection could not be read or the output stream could not be written to
     */
    public static void readWriteBytes(final HttpURLConnection connection, final OutputStream os) throws IOException {
        InputStream is = connection.getInputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) os.write(buffer, 0, length);
        is.close();
    }

    /**
     * Read the bytes from a connection and return them
     *
     * @param connection The connection to read from
     * @return The bytes read from the connection
     * @throws IOException when the connection could not be read
     */
    public static byte[] readBytes(final HttpURLConnection connection) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        readWriteBytes(connection, baos);
        return baos.toByteArray();
    }

    /**
     * Read a string from a connection and return it
     *
     * @param connection The connection to read from
     * @return The string read from the connection
     * @throws IOException when the connection could not be read
     */
    public static String readString(final HttpURLConnection connection) throws IOException {
        StringBuilder responseBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) responseBuilder.append(line);
        br.close();
        return responseBuilder.toString();
    }

    /**
     * Get the information about a plugin on spigotmc<br>
     * Please check spiget documentation for more information
     *
     * @param pluginId The id of the plugin
     * @return JsonObject containing all the information
     * @throws IOException when the plugin was not found
     */
    public static String getSpigotMcPluginInfo(final int pluginId) throws IOException {
        HttpURLConnection connection = openConnection("https://api.spiget.org/v2/resources/" + pluginId);
        if (connection == null) return null;

        String response = readString(connection);
        //return jsonParser.parse(response).getAsJsonObject();
        return response;
    }

    /**
     * Download a plugin from the spiget api
     *
     * @param pluginId The plugin id
     * @param file     where to save the plugin
     * @return boolean if the plugin was found
     * @throws IOException when the plugin could not be saved
     */
    public static boolean downloadSpigotMcPlugin(final int pluginId, final File file) throws IOException {
        HttpURLConnection connection = openConnection("https://api.spiget.org/v2/resources/" + pluginId + "/download");
        if (connection == null) return false;

        FileOutputStream fos = new FileOutputStream(file);
        readWriteBytes(connection, fos);
        fos.close();
        return true;
    }

    /**
     * Download a plugin from a direct link
     *
     * @param url  The URL of the plugin
     * @param file where to save the plugin
     * @throws IOException when the url is invalid/the plugin could not be found
     */
    public static void downloadPlugin(final String url, final File file) throws IOException {
        HttpURLConnection connection = openConnection(url);
        if (connection == null || connection.getContentLength() <= 0) throw new IOException();

        FileOutputStream fos = new FileOutputStream(file);
        readWriteBytes(connection, fos);
        fos.close();
    }

    /**
     * Download a file from a direct link
     *
     * @param url THe URL of the file
     * @return The bytes of the file
     * @throws IOException When the URL is invalid
     */
    public static byte[] download(final String url) throws IOException {
        HttpURLConnection connection = openConnection(url);
        if (connection == null || connection.getContentLength() <= 0) throw new IOException();

        return readBytes(connection);
    }

    public static boolean downloadPlugin(OnlinePlugin plugin) {
        String url = plugin.getDownloadLink();

        // Download from SpigotMC
        if (url.contains("https://www.spigotmc.org/resources/")) {
            if (url.endsWith("/")) url = url.substring(0, url.length() - 1);
            String urlPart = url.substring(url.lastIndexOf("/") + 1);
            if (urlPart.contains(".")) { urlPart = urlPart.substring(urlPart.lastIndexOf(".") + 1); }
            try {
                download(url);
                return true;
            } catch (IOException exception) {
                exception.printStackTrace();
                return false;
            }
            // Download from GitHub
        } else if (url.contains("https://github.com") && url.endsWith(".jar")) {
            try {
                download(url);
                return true;
            } catch (IOException exception) {
                exception.printStackTrace();
                return false;
            }
        } else {
            try {
                download(url);
                return true;
            } catch (IOException exception) {
                exception.printStackTrace();
                return false;
            }
        }
    }

}
