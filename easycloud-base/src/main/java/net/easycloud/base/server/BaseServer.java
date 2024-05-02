package net.easycloud.base.server;

import net.easycloud.api.console.LogType;
import net.easycloud.api.network.packet.defaults.HandshakeAuthenticationPacket;
import net.easycloud.api.network.packet.defaults.ServiceConnectPacket;
import net.easycloud.api.network.server.NettyServer;
import net.easycloud.base.Base;

public final class BaseServer extends NettyServer {

    public BaseServer() {
        super("127.0.0.1", 8897);

        Base.getInstance().getLogger().log("Netty-Server will be started...");

        getPacketHandler().subscribe(HandshakeAuthenticationPacket.class, (channel, packet) -> {
            Base.getInstance().getServiceProvider().getServices().forEach(service -> {
                channel.writeAndFlush(new ServiceConnectPacket(service.getGroup(), service.getId(), service.getPort()));
            });
        });

        Base.getInstance().getLogger().log("Netty-Server was startet on following port: 8897");
    }
}
