package com.zestarr.pluginportal.utils

import com.zestarr.pluginportal.types.Plugin
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

// Thanks to SkyClient for some of the code! <3
// Dont dm them for help with this code, dm Zestarr#0001 or report a issue on github.
object HttpUtils {
    //create a simple get request
    operator fun get(url: String?): String? {
        try {
            val obj = URL(url)
            val con = obj.openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.setRequestProperty("User-Agent", "github.com/Zestarr/PluginPortalApp")
            val responseCode = con.responseCode
            if (responseCode == 200) {
                val `in` = BufferedReader(InputStreamReader(con.inputStream))
                var inputLine: String?
                val response = StringBuffer()
                while (`in`.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                return response.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun download(plugin: Plugin, folder: File) {
        println("Downloading " + plugin.downloadLink + " to " + folder.path)
        try {
            val obj = URL(plugin.downloadLink)
            var fileName = ""
            val con = obj.openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.setRequestProperty("User-Agent", "github.com/Zestarr/PluginPortalApp")
            con.connectTimeout = 5000
            val responseCode = con.responseCode
            val contentDisposition = con.getHeaderField("Content-Disposition")
            if (contentDisposition != null) {
                val parts = contentDisposition.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (part in parts) {
                    if (part.trim { it <= ' ' }.startsWith("filename=")) {
                        fileName = part.substring(part.indexOf('=') + 1)
                        if (fileName.startsWith("\"") && fileName.endsWith("\"")) {
                            fileName = fileName.substring(1, fileName.length - 1)
                        }
                        break
                    }
                }
            }
            if (fileName.isEmpty()) {
                fileName = plugin.displayName + ".jar"
            }
            if (responseCode == 200) {
                val `in` = BufferedInputStream(con.inputStream)
                val fos = FileOutputStream(folder.path + File.separator + fileName)
                val buffer = ByteArray(1024)
                var len: Int
                while (`in`.read(buffer).also { len = it } > 0) {
                    fos.write(buffer, 0, len)
                }
                fos.close()
                `in`.close()
            } else {
                println(responseCode.toString() + " | " + plugin.downloadLink)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun urlEscape(toEncode: String?): String? {
        //escape url
        if (toEncode == null) return null
        val sb = StringBuilder()
        for (character in toEncode.toCharArray()) when (character) {
            '!' -> {
                sb.append("%21")
                continue
            }

            '#' -> {
                sb.append("%23")
                continue
            }

            '$' -> {
                sb.append("%24")
                continue
            }

            '&' -> {
                sb.append("%26")
                continue
            }

            '\'' -> {
                sb.append("%27")
                continue
            }

            '(' -> {
                sb.append("%28")
                continue
            }

            ')' -> {
                sb.append("%29")
                continue
            }

            '*' -> {
                sb.append("%2A")
                continue
            }

            '+' -> {
                sb.append("%2B")
                continue
            }

            ',' -> {
                sb.append("%2C")
                continue
            }

            '/' -> {
                sb.append("%2F")
                continue
            }

            ':' -> {
                sb.append("%3A")
                continue
            }

            ';' -> {
                sb.append("%3B")
                continue
            }

            '=' -> {
                sb.append("%3D")
                continue
            }

            '?' -> {
                sb.append("%3F")
                continue
            }

            '@' -> {
                sb.append("%40")
                continue
            }

            '[' -> {
                sb.append("%5B")
                continue
            }

            ']' -> {
                sb.append("%5D")
                continue
            }

            ' ' -> {
                sb.append("%20")
                continue
            }

            '"' -> {
                sb.append("%22")
                continue
            }

            '%' -> {
                sb.append("%25")
                continue
            }

            '-' -> {
                sb.append("%2D")
                continue
            }

            '<' -> {
                sb.append("%3C")
                continue
            }

            '>' -> {
                sb.append("%3E")
                continue
            }

            '\\' -> {
                sb.append("%5C")
                continue
            }

            '^' -> {
                sb.append("%5E")
                continue
            }

            '_' -> {
                sb.append("%5F")
                continue
            }

            '`' -> {
                sb.append("%60")
                continue
            }

            '{' -> {
                sb.append("%7B")
                continue
            }

            '|' -> {
                sb.append("%7C")
                continue
            }

            '}' -> {
                sb.append("%7D")
                continue
            }

            '~' -> {
                sb.append("%7E")
                continue
            }

            else -> sb.append(character) //if it does not need to be escaped, add the character itself to the StringBuilder
        }
        return sb.toString() //build the string, and return
    }

    fun copyWebsite(websiteURL: String?, filePath: String?) {
        try {
            // Create a URL object for the website
            val website = URL(websiteURL)

            // Open a BufferedReader to read the website's content
            val `in` = BufferedReader(InputStreamReader(website.openStream()))

            // Create a new file at the specified file path
            val file = File(filePath)

            // Check if the file already exists
            if (file.exists()) {
                // If the file exists, delete it
                file.delete()
            }

            // Create a new file
            file.createNewFile()

            // Open a BufferedWriter to write to the file
            val out = BufferedWriter(FileWriter(file))

            // Read each line of the website's content and write it to the file
            var line: String?
            while (`in`.readLine().also { line = it } != null) {
                out.write(line)
                out.newLine()
            }

            // Close the BufferedReader and BufferedWriter
            `in`.close()
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}