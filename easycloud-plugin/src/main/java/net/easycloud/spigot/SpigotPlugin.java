package net.easycloud.spigot;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.flxwdev.ascan.AscanAPI;
import de.flxwdev.ascan.inventory.misc.InventoryConfig;
import lombok.Getter;
import net.easycloud.api.CloudDriver;
import net.easycloud.api.network.packet.defaults.PermissionUpdatePacket;
import net.easycloud.spigot.commands.ControlPanelCommand;
import net.easycloud.spigot.listener.AsyncPlayerPreLoginListener;
import net.easycloud.spigot.listener.PlayerLoginListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@SuppressWarnings("all")
public final class SpigotPlugin extends JavaPlugin {
    @Getter
    private static SpigotPlugin instance;

    private Map<UUID, PermissionAttachment> permissions;

    @Override
    public void onEnable() {
        instance = this;

        this.permissions = new HashMap<UUID, PermissionAttachment>();

        AscanAPI.init(this, new InventoryConfig());
        Bukkit.getConsoleSender().sendMessage("""
                                
                                
                §r███████╗ █████╗ ███████╗██╗   ██╗ ██████╗██╗      ██████╗ ██╗   ██╗██████╗§r
                §r██╔════╝██╔══██╗██╔════╝╚██╗ ██╔╝██╔════╝██║     ██╔═══██╗██║   ██║██╔══██╗§r
                §r█████╗  ███████║███████╗ ╚████╔╝ ██║     ██║     ██║   ██║██║   ██║██║  ██║§r
                §r██╔══╝  ██╔══██║╚════██║  ╚██╔╝  ██║     ██║     ██║   ██║██║   ██║██║  ██║§r
                §r███████╗██║  ██║███████║   ██║   ╚██████╗███████╗╚██████╔╝╚██████╔╝██████╔╝§r
                §r╚══════╝╚═╝  ╚═╝╚══════╝   ╚═╝    ╚═════╝╚══════╝ ╚═════╝  ╚═════╝ ╚═════╝§r
                      
                §b@TheEasyCloud
                         
                """);


        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getConsoleSender().sendMessage("§bPlugin §7was §bsuccessfully §7connected to the §bWrapper§7!");

        getCommand("control-panel").setExecutor(new ControlPanelCommand());
        getServer().getPluginManager().registerEvents(new AsyncPlayerPreLoginListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(), this);

        CloudDriver.getInstance().getNettyProvider().getPacketHandler().subscribe(PermissionUpdatePacket.class, (channel, packet) -> {
            var player = Bukkit.getPlayer(packet.getUuid());
            if(!player.isOnline()) {
                return;
            }
            updatePlayer(player);
        });
    }

    @Override
    public void onDisable() {
        CloudDriver.getInstance().getServiceProvider().stop(CloudDriver.getInstance().getServiceProvider().getCurrentService().getId());

        Bukkit.getOnlinePlayers().forEach(it -> {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF("Lobby-1");
            it.sendPluginMessage(SpigotPlugin.getInstance(), "BungeeCord", out.toByteArray());
        });
    }

    public void updatePlayer(Player player) {
        getPermissions().get(player.getUniqueId()).getPermissions().forEach((permission, unused) -> {
            getPermissions().get(player.getUniqueId()).unsetPermission(permission);
        });

        if(CloudDriver.getInstance().getPermissionProvider().getUser(player.getUniqueId()).getPermissions().stream().anyMatch(it -> it.equals("*"))) {
            player.setOp(true);
        } else {
            player.setOp(false);
            CloudDriver.getInstance().getPermissionProvider().getUser(player.getUniqueId()).getPermissions().forEach(permission -> getPermissions().get(player.getUniqueId()).setPermission(permission, true));
        }
    }
}
