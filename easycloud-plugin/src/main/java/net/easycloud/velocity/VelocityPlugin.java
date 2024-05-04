package net.easycloud.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.easycloud.api.CloudDriver;
import net.easycloud.api.group.misc.GroupType;
import net.easycloud.api.network.packet.defaults.ServiceConnectPacket;
import net.easycloud.api.network.packet.defaults.ServiceDisconnectPacket;
import net.easycloud.api.permission.PermissionUser;
import net.easycloud.api.service.IService;
import net.easycloud.velocity.command.CloudCommand;
import net.easycloud.velocity.command.HubCommand;
import net.easycloud.velocity.command.PermissionCommand;
import net.easycloud.velocity.module.ModuleHandler;
import net.easycloud.velocity.module.tablist.TablistModule;

import java.net.InetSocketAddress;
import java.util.logging.Logger;

@Getter
@Plugin(id = "cloud", name = "Cloud", authors = "FlxwDNS, 1Chickxn, Swerion", version = "1.0.0")
@SuppressWarnings("unused")
public final class VelocityPlugin {
    @Getter
    private static VelocityPlugin instance;

    private final ModuleHandler moduleHandler;
    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public VelocityPlugin(ProxyServer server, Logger logger) {
        instance = this;

        this.moduleHandler = new ModuleHandler();

        // Unregister default
        server.getAllServers().forEach(it -> {
            server.unregisterServer(it.getServerInfo());
        });

        this.server = server;
        this.logger = logger;

        server.getConsoleCommandSource().sendMessage(Component.text("""
                                
                                
                §r███████╗ █████╗ ███████╗██╗   ██╗ ██████╗██╗      ██████╗ ██╗   ██╗██████╗§r
                §r██╔════╝██╔══██╗██╔════╝╚██╗ ██╔╝██╔════╝██║     ██╔═══██╗██║   ██║██╔══██╗§r
                §r█████╗  ███████║███████╗ ╚████╔╝ ██║     ██║     ██║   ██║██║   ██║██║  ██║§r
                §r██╔══╝  ██╔══██║╚════██║  ╚██╔╝  ██║     ██║     ██║   ██║██║   ██║██║  ██║§r
                §r███████╗██║  ██║███████║   ██║   ╚██████╗███████╗╚██████╔╝╚██████╔╝██████╔╝§r
                §r╚══════╝╚═╝  ╚═╝╚══════╝   ╚═╝    ╚═════╝╚══════╝ ╚═════╝  ╚═════╝ ╚═════╝§r
                      
                §b@TheEasyCloud
                         
                """));

        server.getConsoleCommandSource().sendMessage(Component.text("§bPlugin §7was §bsuccessfully §7connected to the §bBase§7!"));

        System.out.println();
        if(!CloudDriver.getInstance().getServiceProvider().getServices().stream().filter(it -> !it.getGroup().getType().equals(GroupType.PROXY)).toList().isEmpty()) {
            System.out.println("All connected services:");
        }
        for (IService service : CloudDriver.getInstance().getServiceProvider().getServices()) {
            if(!service.getGroup().getType().equals(GroupType.PROXY)) {
                System.out.println(" §8- §7" + service.getId() + "§7(§e" + service.getGroup().getName() + "§7)");
                server.registerServer(new ServerInfo(service.getId(), new InetSocketAddress(service.getPort())));
            }
        }
        System.out.println();

        CloudDriver.getInstance().getNettyProvider().getPacketHandler().subscribe(ServiceConnectPacket.class, (channel, packet) -> {
            if(server.getServer(packet.getName()).isEmpty() && !packet.getGroup().getType().equals(GroupType.PROXY)) {
                System.out.println("Service " + packet.getName() + " will be started...");
                server.getAllPlayers().forEach(it -> it.sendMessage(Component.text("§bEasyCloud §8» §e" + packet.getName() + " §7was §astarted§8!")));
                server.registerServer(new ServerInfo(packet.getName(), new InetSocketAddress(packet.getPort())));
            }
        });

        CloudDriver.getInstance().getNettyProvider().getPacketHandler().subscribe(ServiceDisconnectPacket.class, (channel, packet) -> {
            if(server.getServer(packet.getName()).isPresent() && !packet.getGroup().getType().equals(GroupType.PROXY)) {
                System.out.println("Service " + packet.getName() + " will be stopped...");
                server.getAllPlayers().forEach(it -> it.sendMessage(Component.text("§bEasyCloud §8» §e" + packet.getName() + " §7was §cstopped§8!")));
                server.unregisterServer(server.getServer(packet.getName()).orElseThrow().getServerInfo());
            }
        });
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        // Module
        server.getEventManager().register(this, new TablistModule());

        CommandManager commandManager = server.getCommandManager();

        commandManager.register(commandManager.metaBuilder("cs").aliases("cloudsystem", "pc", "EasyCloud").plugin(this).build(), new CloudCommand());
        commandManager.register(commandManager.metaBuilder("hub").aliases("lobby", "lobbyserver", "hubschrauber").plugin(this).build(), new HubCommand());
        commandManager.register(commandManager.metaBuilder("perm").aliases("permission").plugin(this).build(), new PermissionCommand());
    }

    @Subscribe
    public void onKickedFromServer(KickedFromServerEvent event) {
        server.getAllServers().stream().filter(it -> it.getServerInfo().getName().startsWith(moduleHandler.getConfig().getLobbyGroupName())).findFirst().ifPresentOrElse(it -> {
            event.setResult(KickedFromServerEvent.RedirectPlayer.create(it));
        }, () -> {
            event.getPlayer().disconnect(Component.text("§cNo fallback server!"));
        });
    }

    @Subscribe
    public void onLogin(LoginEvent event) {
        if(CloudDriver.getInstance().getPermissionProvider().getUser(event.getPlayer().getUniqueId()) == null) {
            CloudDriver.getInstance().getPermissionProvider().getRepository().query().create(new PermissionUser(event.getPlayer().getUniqueId(), ""));
        }
        var permissions = CloudDriver.getInstance().getPermissionProvider().getUser(event.getPlayer().getUniqueId()).getPermissions();
        if(!permissions.contains("*") && !permissions.contains("cloud.maintenance")) {
            if(this.moduleHandler.getConfig().getMaintenance()) {
                event.setResult(LoginEvent.ComponentResult.denied(Component.text("§cMaintenance is enabled§8!")));
            }
        }
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        CloudDriver.getInstance().getServiceProvider().stop(CloudDriver.getInstance().getServiceProvider().getCurrentService().getId());
    }

    @Subscribe
    public void onPlayerChooseInitialServer(PlayerChooseInitialServerEvent event) {
        server.getAllServers().stream().filter(it -> it.getServerInfo().getName().startsWith(moduleHandler.getConfig().getLobbyGroupName())).findFirst().ifPresentOrElse(event::setInitialServer, () -> {
            event.getPlayer().disconnect(Component.text("§cNo fallback server!"));
        });
    }
}
