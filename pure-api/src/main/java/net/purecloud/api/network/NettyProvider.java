package net.purecloud.api.network;

import net.purecloud.api.network.packet.Packet;
import net.purecloud.api.network.packet.PacketHandler;

public interface NettyProvider {
    PacketHandler getPacketHandler();

    void sendPacket(Packet packet);
    void close();
}
