package net.easycloud.api.network.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.easycloud.api.network.code.PacketDecoder;
import net.easycloud.api.network.code.PacketEncoder;
import net.easycloud.api.network.code.PacketLengthDeserializer;
import net.easycloud.api.network.code.PacketLengthSerializer;

@Getter
@AllArgsConstructor
public final class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    private final NettyServer nettyServer;

    @Override
    protected void initChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new PacketDecoder(nettyServer.getPacketHandler()));
        pipeline.addLast(new PacketEncoder(nettyServer.getPacketHandler()));
        pipeline.addLast(new PacketLengthDeserializer());
        pipeline.addLast(new PacketLengthSerializer());
        pipeline.addLast("handler", new NettyServerChannel(nettyServer));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        throw new RuntimeException(cause);
    }
}
