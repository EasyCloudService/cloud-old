package net.easycloud.api.network.code;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.easycloud.api.network.NetworkBuf;
import net.easycloud.api.network.packet.Packet;
import net.easycloud.api.network.packet.PacketHandler;

public final class PacketEncoder extends MessageToByteEncoder<Packet> {
    private final PacketHandler packetHandler;

    public PacketEncoder(PacketHandler packetHandler) {
        this.packetHandler = packetHandler;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) {
        byteBuf.writeInt(packetHandler.getPacketId(packet.getClass()));
        packet.write(new NetworkBuf(byteBuf));
    }
}