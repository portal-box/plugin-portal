package link.portalbox.pluginportal.listener;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import link.portalbox.pluginportal.PluginPortal;
import link.portalbox.pluginportal.utils.ChatUtil;
import link.portalbox.util.JsonUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class StatusListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().isOp() || event.getPlayer().hasPermission("pp.primarycommand")) {
            if (PluginPortal.getMainInstance().isUpdated()) return;
            try {
                Player player = event.getPlayer();
                JsonObject jsonObject = new JsonParser().parse(JsonUtil.getJson("https://raw.githubusercontent.com/portal-box/plugin-portal/master/resources/Data.json")).getAsJsonObject();
                TextComponent component = new TextComponent(ChatUtil.format("&7&l[&b&lPP&7&l] &8&l> &7Plugin Portal needs to be updated. Please download the latest version from: &b&l[CLICK HERE]"));
                component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, jsonObject.get("downloadURL").getAsString()));
                player.spigot().sendMessage(component);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

}
