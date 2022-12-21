package com.zestarr.pluginportal.managers

import com.zestarr.pluginportal.types.Plugin
import com.zestarr.pluginportal.utils.ConfigUtils
import com.zestarr.pluginportal.utils.HttpUtils
import org.bukkit.configuration.file.FileConfiguration
import java.io.File
import java.io.IOException

class PluginManager {
    private val plugins = HashMap<String, Plugin>()
    var downloadFolder: File? = null
        private set

    @Throws(IOException::class)
    fun loadPlugins() {
        val config: FileConfiguration = ConfigUtils.pluginListFileConfig
        for (str in ConfigUtils.pluginListFileConfig.getConfigurationSection("Plugins.")!!.getKeys(false)) {
            val plugin = Plugin()
            plugin.fileName = config.getString("Plugins.$str.fileName")
            plugin.displayName = str
            plugin.description = config.getString("Plugins.$str.description")
            plugin.downloadLink = config.getString("Plugins.$str.downloadLink")
            plugin.version = config.getString("Plugins.$str.version")
            plugin.sha256 = config.getString("Plugins.$str.sha256")
            plugins[str] = plugin
        }
    }

    @JvmOverloads
    fun downloadPlugins(downloadFolder: File? = this.downloadFolder) {
        setupFolder(downloadFolder)
        for (plugin in plugins.values) {
            HttpUtils.download(plugin, downloadFolder)
        }
    }

    fun setupFolder(folder: File?): File? {
        folder!!.mkdirs()
        downloadFolder = folder
        HttpUtils.copyWebsite(
            "https://raw.githubusercontent.com/Zestarr/PluginPortal/master/PluginsList.yml",
            "Plugins.yml"
        )
        return folder
    }

    fun setupFolder(path: String?): File? {
        return setupFolder(File(path))
    }

    fun getPlugins(): Map<String, Plugin> {
        return plugins
    }
}