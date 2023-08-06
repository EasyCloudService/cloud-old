package net.purecloud.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.purecloud.api.CloudDriver;
import net.purecloud.api.group.misc.GroupType;
import net.purecloud.api.network.packet.defaults.ServiceConnectPacket;
import net.purecloud.api.network.packet.defaults.ServiceDisconnectPacket;
import net.purecloud.api.service.IService;
import net.purecloud.velocity.command.CloudCommand;
import net.purecloud.velocity.listener.ServerConnectListener;
import net.purecloud.wrapper.Wrapper;
import net.purecloud.wrapper.service.ServiceHandler;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Plugin(id = "cloud", name = "Cloud", authors = "FlxwDNS", version = "1.0.0")
@SuppressWarnings("unused")
public final class VelocityPlugin {
    @Getter
    private static VelocityPlugin instance;

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public VelocityPlugin(ProxyServer server, Logger logger) {
        instance = this;

        // Unregister default
        server.getAllServers().forEach(it -> {
            server.unregisterServer(it.getServerInfo());
        });

        this.server = server;
        this.logger = logger;

        if(!CloudDriver.getInstance().getServiceProvider().getServices().stream().filter(it -> !it.getGroup().getType().equals(GroupType.PROXY)).toList().isEmpty()) {
            System.out.println("All connected services:");
        }
        for (IService service : CloudDriver.getInstance().getServiceProvider().getServices()) {
            if(!service.getGroup().getType().equals(GroupType.PROXY)) {
                System.out.println(" - " + service.getId());
                server.registerServer(new ServerInfo(service.getId(), new InetSocketAddress(service.getPort())));
            }
        }
        System.out.println();

        //CloudDriver.getInstance().getEventHandler().register(new ServerConnectListener());

        CloudDriver.getInstance().getNettyProvider().getPacketHandler().subscribe(ServiceConnectPacket.class, (channel, packet) -> {
            if(server.getServer(packet.getName()).isEmpty() && !packet.getGroup().getType().equals(GroupType.PROXY)) {
                System.out.println("Service " + packet.getName() + " will be started...");
                server.getAllPlayers().forEach(it -> it.sendMessage(Component.text("§bPureCloud §8» §e" + packet.getName() + " §7was §astarted§8!")));
                server.registerServer(new ServerInfo(packet.getName(), new InetSocketAddress(packet.getPort())));
            }
        });

        CloudDriver.getInstance().getNettyProvider().getPacketHandler().subscribe(ServiceDisconnectPacket.class, (channel, packet) -> {
            if(server.getServer(packet.getName()).isPresent() && !packet.getGroup().getType().equals(GroupType.PROXY)) {
                System.out.println("Service " + packet.getName() + " will be stopped...");
                server.getAllPlayers().forEach(it -> it.sendMessage(Component.text("§bPureCloud §8» §e" + packet.getName() + " §7was §cstopped§8!")));
                server.unregisterServer(server.getServer(packet.getName()).orElseThrow().getServerInfo());
            }
        });
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        CommandManager commandManager = server.getCommandManager();

        CommandMeta commandMeta = commandManager.metaBuilder("cs").aliases("cloudsystem", "pc", "purecloud").plugin(this).build();
        commandManager.register(commandMeta, new CloudCommand());
    }

    @Subscribe
    public void onInitialize(PlayerChooseInitialServerEvent event) {
        server.getServer("Lobby-1").ifPresentOrElse(event::setInitialServer, () -> {
            event.getPlayer().disconnect(Component.text("No fallback server!"));
        });
    }
}
