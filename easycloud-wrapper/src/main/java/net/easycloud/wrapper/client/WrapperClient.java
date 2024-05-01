package net.easycloud.wrapper.client;

import net.easycloud.api.network.client.NettyClient;
import net.easycloud.api.network.packet.defaults.ServiceConnectPacket;
import net.easycloud.wrapper.Wrapper;
import net.easycloud.wrapper.event.defaults.ServerConnectEvent;

public final class WrapperClient extends NettyClient {

    public WrapperClient() {
        super("127.0.0.1", 8897);

        getPacketHandler().subscribe(ServiceConnectPacket.class, (channel, packet) -> {
            Wrapper.getInstance().getEventHandler().call(new ServerConnectEvent(packet.getGroup()));
        });
    }
}
