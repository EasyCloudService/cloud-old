package net.purecloud.wrapper.client;

import net.purecloud.api.network.client.NettyClient;
import net.purecloud.api.network.packet.defaults.ServiceConnectPacket;
import net.purecloud.wrapper.Wrapper;
import net.purecloud.wrapper.event.defaults.ServerConnectEvent;

public final class WrapperClient extends NettyClient {

    public WrapperClient() {
        super("127.0.0.1", 8897);

        getPacketHandler().subscribe(ServiceConnectPacket.class, (channel, packet) -> {
            Wrapper.getInstance().getEventHandler().call(new ServerConnectEvent(packet.getGroup()));
        });
    }
}
