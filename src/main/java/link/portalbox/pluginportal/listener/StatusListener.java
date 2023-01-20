package link.portalbox.pluginportal.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import link.portalbox.pluginportal.PluginPortal;
import link.portalbox.pluginportal.utils.ChatUtil;
import link.portalbox.pluginportal.utils.JsonUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class StatusListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().isOp() || event.getPlayer().hasPermission("pp.primarycommand")) {
            if (!PluginPortal.getMainInstance().isIsPluginLatestVersion()) return;
            try {
                Player player = event.getPlayer();
                TextComponent component = new TextComponent(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &7Plugin Portal needs to be updated. Please download the latest version from: "));
                component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, new ObjectMapper().readValue(JsonUtil.getDataJson(), JsonNode.class).get("downloadURL").asText()));
                player.spigot().sendMessage(component);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

}
