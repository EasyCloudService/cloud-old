package net.easycloud.spigot.listener;

import net.easycloud.spigot.SpigotPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

@SuppressWarnings("unused")
public final class PlayerLoginListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        var player = event.getPlayer();
        var attachment = player.addAttachment(SpigotPlugin.getInstance());
        SpigotPlugin.getInstance().getPermissions().put(player.getUniqueId(), attachment);

        SpigotPlugin.getInstance().updatePlayer(player);
    }
}
