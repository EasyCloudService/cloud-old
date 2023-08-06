package net.purecloud.api.network.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.purecloud.api.network.code.*;

@Getter
@AllArgsConstructor
public final class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    private final NettyClient nettyClient;

    @Override
    protected void initChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new PacketDecoder(nettyClient.getPacketHandler()));
        pipeline.addLast(new PacketEncoder(nettyClient.getPacketHandler()));
        pipeline.addLast(new PacketLengthDeserializer());
        pipeline.addLast(new PacketLengthDeserializer());
        pipeline.addLast("handler", new NettyClientHandler(nettyClient));
       // pipeline.addLast(nettyClient.getPacketHandler());
    }
}
