package net.easycloud.spigot.listener;

import net.easycloud.api.CloudDriver;
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

        if(CloudDriver.getInstance().getPermissionProvider().getUser(player.getUniqueId()).getPermissions().stream().anyMatch(it -> it.equals("*"))) {
            player.setOp(true);
        } else {
            player.setOp(false);
            CloudDriver.getInstance().getPermissionProvider().getUser(player.getUniqueId()).getPermissions().forEach(permission -> SpigotPlugin.getInstance().getPermissions().get(player.getUniqueId()).setPermission(permission, true));
        }
    }
}
