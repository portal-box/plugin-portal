package com.zestarr.pluginportal.utils;

// Thanks to SkyClient for some of the code! <3
// Dont dm them for help with this code, dm Zestarr#0001 or report a issue on github.

import com.zestarr.pluginportal.types.Plugin;

import java.io.*;
import java.net.URL;

public class HttpUtils {

    //create a simple get request
    public static String get(String url) {
        try {
            URL obj = new URL(url);
            java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "github.com/Zestarr/PluginPortalApp");
            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void download(Plugin plugin, File folder) {

        System.out.println("Downloading " + plugin.getDownloadLink() + " to " + folder.getPath());

        try {
            URL obj = new URL(plugin.getDownloadLink());
            String fileName = "";

            java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "github.com/Zestarr/PluginPortalApp");
            con.setConnectTimeout(5000);
            int responseCode = con.getResponseCode();

            String contentDisposition = con.getHeaderField("Content-Disposition");
            if (contentDisposition != null) {
                String[] parts = contentDisposition.split(";");
                for (String part : parts) {
                    if (part.trim().startsWith("filename=")) {
                        fileName = part.substring(part.indexOf('=') + 1);
                        if (fileName.startsWith("\"") && fileName.endsWith("\"")) {
                            fileName = fileName.substring(1, fileName.length() - 1);
                        }
                        break;
                    }
                }
            }

            if (fileName.isEmpty()) {
                fileName = plugin.getFileName();
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
            } else {
                System.out.println(responseCode + " | " + plugin.getDownloadLink());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String urlEscape(String toEncode) {
        //escape url
        //noinspection deprecation
        if (toEncode == null) return null;

        StringBuilder sb = new StringBuilder();
        for (char character : toEncode.toCharArray())//for every character in the string
            switch (character) {//if the character needs to be escaped, add its escaped value to the StringBuilder
                case '!':
                    sb.append("%21");
                    continue;
                case '#':
                    sb.append("%23");
                    continue;
                case '$':
                    sb.append("%24");
                    continue;
                case '&':
                    sb.append("%26");
                    continue;
                case '\'':
                    sb.append("%27");
                    continue;
                case '(':
                    sb.append("%28");
                    continue;
                case ')':
                    sb.append("%29");
                    continue;
                case '*':
                    sb.append("%2A");
                    continue;
                case '+':
                    sb.append("%2B");
                    continue;
                case ',':
                    sb.append("%2C");
                    continue;
                case '/':
                    sb.append("%2F");
                    continue;
                case ':':
                    sb.append("%3A");
                    continue;
                case ';':
                    sb.append("%3B");
                    continue;
                case '=':
                    sb.append("%3D");
                    continue;
                case '?':
                    sb.append("%3F");
                    continue;
                case '@':
                    sb.append("%40");
                    continue;
                case '[':
                    sb.append("%5B");
                    continue;
                case ']':
                    sb.append("%5D");
                    continue;
                case ' ':
                    sb.append("%20");
                    continue;
                case '"':
                    sb.append("%22");
                    continue;
                case '%':
                    sb.append("%25");
                    continue;
                case '-':
                    sb.append("%2D");
                    continue;
                case '<':
                    sb.append("%3C");
                    continue;
                case '>':
                    sb.append("%3E");
                    continue;
                case '\\':
                    sb.append("%5C");
                    continue;
                case '^':
                    sb.append("%5E");
                    continue;
                case '_':
                    sb.append("%5F");
                    continue;
                case '`':
                    sb.append("%60");
                    continue;
                case '{':
                    sb.append("%7B");
                    continue;
                case '|':
                    sb.append("%7C");
                    continue;
                case '}':
                    sb.append("%7D");
                    continue;
                case '~':
                    sb.append("%7E");
                    continue;
                default:
                    sb.append(character);//if it does not need to be escaped, add the character itself to the StringBuilder
            }
        return sb.toString();//build the string, and return
    }

    public static void copyWebsite(String websiteURL, String filePath) {
        try {
            // Create a URL object for the website
            URL website = new URL(websiteURL);

            // Open a BufferedReader to read the website's content
            BufferedReader in = new BufferedReader(new InputStreamReader(website.openStream()));

            // Create a new file at the specified file path
            File file = new File(filePath);

            // Check if the file already exists
            if (file.exists()) {
                // If the file exists, delete it
                file.delete();
            }

            // Create a new file
            file.createNewFile();

            // Open a BufferedWriter to write to the file
            BufferedWriter out = new BufferedWriter(new FileWriter(file));

            // Read each line of the website's content and write it to the file
            String line;
            while ((line = in.readLine()) != null) {
                out.write(line);
                out.newLine();
            }

            // Close the BufferedReader and BufferedWriter
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}