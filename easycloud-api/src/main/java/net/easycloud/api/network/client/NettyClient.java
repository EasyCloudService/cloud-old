package net.easycloud.api.network.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import net.easycloud.api.network.packet.Packet;
import net.easycloud.api.network.packet.PacketHandler;
import net.easycloud.api.network.NettyProvider;

@Getter
public abstract class NettyClient implements NettyProvider {
    private final Channel channel;
    private final PacketHandler packetHandler;
    private final EventLoopGroup eventLoopGroup;

    public NettyClient(String host, int port) {
        this.eventLoopGroup = newEventLoopGroup();
        this.packetHandler = new PacketHandler();

        try {
            this.channel = new Bootstrap()
                    .channel(getSocketChannel())
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

    private EventLoopGroup newEventLoopGroup() {
        //return (Epoll.isAvailable() ? new EpollEventLoopGroup(threads) : new NioEventLoopGroup(threads));
        return (Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup());
    }

    private Class<? extends SocketChannel> getSocketChannel() {
        //return (Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class);
        return (Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class);
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
