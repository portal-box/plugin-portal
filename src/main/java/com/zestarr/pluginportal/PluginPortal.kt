package com.zestarr.pluginportal

import co.aikar.commands.*
import com.zestarr.pluginportal.commands.InstallCommand
import com.zestarr.pluginportal.managers.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException

class PluginPortal : JavaPlugin() {
    override fun onEnable() {
        println(dataFolder.absolutePath)
        pluginManager = PluginManager()
        pluginManager!!.setupFolder(File("plugins"))
        try {
            pluginManager!!.loadPlugins()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Register Commands and Listeners
        manager = PaperCommandManager(this)
        commands.add(InstallCommand())
        registerCommands(manager)
        for (plugin in pluginManager!!.getPlugins().values) {
            tabComplete.add(plugin.displayName)
        }
    }

    private fun registerCommands(manager: PaperCommandManager?) {

        // Enable the help api.
        manager!!.enableUnstableAPI("help")

        // Register tab-autocomplete options.
        manager.commandCompletions.registerAsyncCompletion(
            "install"
        ) { c: BukkitCommandCompletionContext? -> tabComplete }
        for (command in commands) {
            manager.registerCommand(command)
        }

        // Generic exception handler
        manager.defaultExceptionHandler =
            ExceptionHandler { command: BaseCommand, registeredCommand: RegisteredCommand<*>?, sender: CommandIssuer?, args: List<String?>?, t: Throwable? ->
                logger.warning("Error occured while executing command: " + command.name)
                false
            }
    }

    companion object {
        var pluginManager: PluginManager? = null
        var manager: PaperCommandManager? = null
        var commands = ArrayList<BaseCommand>()
        var tabComplete = ArrayList<String>()
    }
}