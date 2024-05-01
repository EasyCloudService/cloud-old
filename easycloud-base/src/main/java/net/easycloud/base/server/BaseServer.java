package net.easycloud.base.server;

import net.easycloud.api.network.packet.defaults.HandshakeAuthenticationPacket;
import net.easycloud.api.network.packet.defaults.ServiceConnectPacket;
import net.easycloud.api.network.server.NettyServer;
import net.easycloud.base.Base;

public final class BaseServer extends NettyServer {

    public BaseServer() {
        super("127.0.0.1", 8897);

        getPacketHandler().subscribe(HandshakeAuthenticationPacket.class, (channel, packet) -> {
            Base.getInstance().getServiceProvider().getServices().forEach(service -> {
                channel.writeAndFlush(new ServiceConnectPacket(service.getGroup(), service.getId(), service.getPort()));
            });
        });
    }
}
