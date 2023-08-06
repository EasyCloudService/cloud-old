package net.purecloud.wrapper;

import lombok.Getter;
import net.purecloud.api.CloudDriver;
import net.purecloud.api.network.client.NettyClient;
import net.purecloud.api.network.packet.defaults.HandshakeAuthenticationPacket;
import net.purecloud.wrapper.client.WrapperClient;
import net.purecloud.wrapper.event.SimpleEventHandler;
import net.purecloud.wrapper.group.SimpleGroupHandler;
import net.purecloud.wrapper.service.ServiceHandler;

@Getter
public final class Wrapper extends CloudDriver {
    private static Wrapper instance;

    private final String name;

    public Wrapper(String name) {
        instance = this;

        this.name = name;
        this.nettyProvider = new WrapperClient();
        this.eventHandler = new SimpleEventHandler();
        this.serviceProvider = new ServiceHandler();
        this.groupProvider = new SimpleGroupHandler();

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
