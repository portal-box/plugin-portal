package com.zestarr.pluginportal.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.zestarr.pluginportal.PluginPortal
import com.zestarr.pluginportal.types.Plugin
import com.zestarr.pluginportal.utils.ConfigUtils
import com.zestarr.pluginportal.utils.HttpUtils
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.File

@CommandAlias("ppm")
class InstallCommand : BaseCommand() {
    @Default
    @CommandPermission("ppm.install")
    @Description("Main Install Command for the ppm (PluginPortal Package Manager)")
    fun onCommand(sender: CommandSender?, pluginKey: String): Boolean {
        if (sender is Player) {
            val player = sender
            // Check if plugin is valid
            val plugins: Map<String, Plugin?> = PluginPortal.pluginManager.getPlugins()
            if (plugins[pluginKey] == null) {
                player.sendMessage(ChatColor.RED.toString() + "Plugin not found! Plugin List: ")
                for (plugin in plugins.values) {
                    player.sendMessage(ChatColor.GREEN.toString() + " + " + ChatColor.GRAY + plugin!!.displayName)
                }
                return true
            } else {
                if (File(
                        ConfigUtils.pluginFolder,
                        "$pluginKey.jar"
                    ).exists()
                ) { // Broken, Method is to save all saved plugins "display name" and the file name to a hashmap/file and check from there
                    player.sendMessage(ChatColor.RED.toString() + "Plugin already installed!")
                    return true
                } else {
                    HttpUtils.download(
                        PluginPortal.pluginManager.getPlugins()[pluginKey],
                        ConfigUtils.pluginFolder
                    )
                    player.sendMessage(ChatColor.GREEN.toString() + "Intsalling Plugin!")
                }
            }
        }
        return true
    }
}