package net.easycloud.api.network.code;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.easycloud.api.network.NetworkBuf;
import net.easycloud.api.network.packet.PacketHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public final class PacketDecoder extends ByteToMessageDecoder {
    private final PacketHandler packetHandler;

    public PacketDecoder(PacketHandler packetHandler) {
        this.packetHandler = packetHandler;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        var index = byteBuf.readInt();
        var clazz = this.packetHandler.getPacketClass(index);
        if (clazz != null) {
            try {
                var packet = clazz.getDeclaredConstructor().newInstance();
                packet.handle(new NetworkBuf(byteBuf));
                list.add(packet);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

}
