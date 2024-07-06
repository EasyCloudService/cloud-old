package net.easycloud.spigot;

import lombok.Getter;
import net.easycloud.api.CloudDriver;
import net.easycloud.api.network.packet.ServiceStatePacket;
import net.easycloud.api.service.state.ServiceState;
import net.easycloud.spigot.listener.AsyncPlayerPreLoginListener;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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
        Bukkit.getConsoleSender().sendMessage("§aSuccessfully §7injected the §b@EasyCloudService");
        Bukkit.getConsoleSender().sendMessage("§bPlugin §7was §asuccessfully §7connected to the §bWrapper§7!");

        getServer().getPluginManager().registerEvents(new AsyncPlayerPreLoginListener(), this);

        var current = CloudDriver.instance().serviceProvider().getCurrentService().getId();
        CloudDriver.instance().nettyClient().sendPacket(new ServiceStatePacket(current, ServiceState.RUNNING));
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(it -> {
            it.kick(Component.text("§cService is offline§8!"));
        });
        CloudDriver.instance().serviceProvider().stop(CloudDriver.instance().serviceProvider().getCurrentService().getId());

        var current = CloudDriver.instance().serviceProvider().getCurrentService().getId();
        CloudDriver.instance().nettyClient().sendPacket(new ServiceStatePacket(current, ServiceState.STOPPED));
    }
}
