package net.easycloud.wrapper;

import dev.httpmarco.osgan.networking.client.NettyClientBuilder;
import lombok.Getter;
import net.easycloud.api.CloudDriver;
import net.easycloud.api.network.packet.HandshakeAuthenticationPacket;
import net.easycloud.api.network.packet.ServiceConnectPacket;
import net.easycloud.wrapper.event.SimpleEventHandler;
import net.easycloud.wrapper.event.defaults.ServerConnectEvent;
import net.easycloud.wrapper.group.SimpleGroupHandler;
import net.easycloud.wrapper.user.UserHandler;
import net.easycloud.wrapper.service.ServiceHandler;

@SuppressWarnings("ALL")
@Getter
public final class Wrapper extends CloudDriver {
    private static Wrapper instance;

    private final String name;

    public Wrapper(String name, String secret) {
        instance = this;

        this.name = name;
        this.nettyClient = new NettyClientBuilder().withPort(8897).onActive(transmit -> {
            this.nettyClient.sendPacket(new HandshakeAuthenticationPacket(secret));
        }).build();

        this.nettyClient.listen(ServiceConnectPacket.class, (transmit, packet) -> {
            this.eventHandler.call(new ServerConnectEvent(packet.getGroup()));
        });

        this.eventHandler = new SimpleEventHandler();
        this.serviceProvider = new ServiceHandler();
        this.groupProvider = new SimpleGroupHandler();
        this.userProvider = new UserHandler();

        Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));
    }

    @Override
    public void onShutdown() {
        WrapperBootstrap.getThread().interrupt();
    }

    public static Wrapper getInstance() {
        return (Wrapper) instance;
    }
}
