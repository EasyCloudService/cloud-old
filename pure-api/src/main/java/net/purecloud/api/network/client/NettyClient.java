package net.purecloud.api.network.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import net.purecloud.api.network.NettyProvider;
import net.purecloud.api.network.packet.Packet;
import net.purecloud.api.network.packet.PacketHandler;

@Getter
public abstract class NettyClient implements NettyProvider {
    private final Channel channel;
    private final PacketHandler packetHandler;
    private final EventLoopGroup eventLoopGroup;

    public NettyClient(String host, int port) {
        this.eventLoopGroup = new NioEventLoopGroup();
        this.packetHandler = new PacketHandler();

        try {
            this.channel = new Bootstrap()
                    .channel(NioSocketChannel.class)
                    .group(this.eventLoopGroup)
                    .handler(new NettyClientInitializer(this))
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.AUTO_READ, true)
                    .connect(host, port)
                    .sync()
                    .channel();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void sendPacket(Packet packet) {
        channel.writeAndFlush(packet);
    }

    @Override
    public void close() {
        channel.close();
    }
}
