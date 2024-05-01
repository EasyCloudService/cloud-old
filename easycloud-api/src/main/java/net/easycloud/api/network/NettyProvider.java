package net.easycloud.api.network;

import net.easycloud.api.network.packet.Packet;
import net.easycloud.api.network.packet.PacketHandler;

public interface NettyProvider {
    PacketHandler getPacketHandler();

    void sendPacket(Packet packet);
    void close();
}
