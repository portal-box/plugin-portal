package com.zestarr.pluginportal.utils

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object ConfigUtils {
    val pluginFolder: File
        get() = File("plugins")
    val pluginListFile: File
        get() = File("Plugins.yml")
    val pluginListFileConfig: YamlConfiguration
        get() = YamlConfiguration.loadConfiguration(pluginListFile)
}