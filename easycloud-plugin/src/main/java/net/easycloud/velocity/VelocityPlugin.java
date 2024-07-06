package net.easycloud.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import lombok.Getter;
import net.easycloud.api.network.packet.*;
import net.easycloud.api.service.state.ServiceState;
import net.kyori.adventure.text.Component;
import net.easycloud.api.CloudDriver;
import net.easycloud.api.group.misc.GroupType;
import net.easycloud.api.service.IService;
import net.easycloud.velocity.command.CloudCommand;
import net.easycloud.velocity.command.HubCommand;

import java.net.InetSocketAddress;
import java.util.logging.Logger;

@Getter
@Plugin(id = "cloud", name = "Cloud", authors = "FlxwDNS, 1Chickxn, Swerion", version = "1.0.0")
@SuppressWarnings({"unused", "ClassCanBeRecord"})
public final class VelocityPlugin {
    @Getter
    private static VelocityPlugin instance;

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public VelocityPlugin(ProxyServer server, Logger logger) {
        instance = this;

        // Unregister default
        server.getAllServers().forEach(it -> server.unregisterServer(it.getServerInfo()));

        this.server = server;
        this.logger = logger;

        server.getConsoleCommandSource().sendMessage(Component.text("§aSuccessfully §7injected the §b@EasyCloudService"));
        server.getConsoleCommandSource().sendMessage(Component.text("§bPlugin §7was §asuccessfully §7connected to the §bWrapper§7!"));

        if(!CloudDriver.instance().serviceProvider().getServices().stream().filter(it -> !it.getGroup().getType().equals(GroupType.PROXY)).toList().isEmpty()) {
            System.out.println("All registered services:");
        }
        for (IService service : CloudDriver.instance().serviceProvider().getServices()) {
            if(!service.getGroup().getType().equals(GroupType.PROXY)) {
                System.out.println("§f- §7" + service.getId() + " §7(§e" + service.getGroup().getName() + "§7)");
                server.registerServer(new ServerInfo(service.getId(), new InetSocketAddress(service.getPort())));
            }
        }
        System.out.println();

        CloudDriver.instance().nettyClient().listen(ServiceConnectPacket.class, (channel, packet) -> {
            if(server.getServer(packet.getName()).isEmpty() && !packet.getGroup().getType().equals(GroupType.PROXY)) {
                System.out.println("Service " + packet.getName() + " will be started...");
                server.getAllPlayers().forEach(it -> it.sendMessage(Component.text("§bEasyCloud §8» §e" + packet.getName() + " §7was §astarted§8!")));
                server.registerServer(new ServerInfo(packet.getName(), new InetSocketAddress(packet.getPort())));
            }
        });

        CloudDriver.instance().nettyClient().listen(ServiceDisconnectPacket.class, (channel, packet) -> {
            if(server.getServer(packet.getName()).isPresent() && !packet.getGroup().getType().equals(GroupType.PROXY)) {
                System.out.println("Service " + packet.getName() + " will be stopped...");
                server.getAllPlayers().forEach(it -> it.sendMessage(Component.text("§bEasyCloud §8» §e" + packet.getName() + " §7was §cstopped§8!")));
                server.unregisterServer(server.getServer(packet.getName()).orElseThrow().getServerInfo());
            }
        });
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        CommandManager commandManager = server.getCommandManager();

        commandManager.register(commandManager.metaBuilder("cs").aliases("cloudsystem").plugin(this).build(), new CloudCommand());
        commandManager.register(commandManager.metaBuilder("hub").aliases("lobby", "lobbyserver", "hubschrauber").plugin(this).build(), new HubCommand());

        var current = CloudDriver.instance().serviceProvider().getCurrentService().getId();
        CloudDriver.instance().nettyClient().sendPacket(new ServiceStatePacket(current, ServiceState.RUNNING));
    }

    @Subscribe
    public void onKickedFromServer(KickedFromServerEvent event) {
        CloudDriver.instance().userProvider().removeUser(event.getPlayer().getUniqueId());
        CloudDriver.instance().nettyClient().sendPacket(new PlayerDisconnectPacket(event.getPlayer().getUniqueId()));

        server.getAllServers().stream()
                .filter(it -> CloudDriver.instance().groupProvider().getOrThrow(it.getServerInfo().getName().split("-")[0].replace("-", "")).getType().equals(GroupType.LOBBY))
                .findFirst()
                .ifPresentOrElse(it -> event.setResult(KickedFromServerEvent.RedirectPlayer.create(it)),
                        () -> event.getPlayer().disconnect(Component.text("§cNo fallback server!")));
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        CloudDriver.instance().serviceProvider().stop(CloudDriver.instance().serviceProvider().getCurrentService().getId());
    }

    @Subscribe
    public void onPlayerDisconnect(DisconnectEvent event) {
        CloudDriver.instance().userProvider().removeUser(event.getPlayer().getUniqueId());
        CloudDriver.instance().nettyClient().sendPacket(new PlayerDisconnectPacket(event.getPlayer().getUniqueId()));
    }

    @Subscribe
    public void onPlayerChooseInitialServer(PlayerChooseInitialServerEvent event) {
        CloudDriver.instance().userProvider().createUserIfNotExists(event.getPlayer().getUniqueId());
        CloudDriver.instance().nettyClient().sendPacket(new PlayerConnectPacket(event.getPlayer().getUniqueId()));
        server.getAllServers().stream()
                .filter(it -> CloudDriver.instance().groupProvider().getOrThrow(it.getServerInfo().getName().split("-")[0].replace("-", "")).getType().equals(GroupType.LOBBY))
                .findFirst()
                .ifPresentOrElse(event::setInitialServer, () -> event.getPlayer().disconnect(Component.text("§cNo fallback server!")));
    }
}
