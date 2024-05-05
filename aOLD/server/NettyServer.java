package net.easycloud.api.network.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import net.easycloud.api.network.NettyProvider;
import net.easycloud.api.network.packet.Packet;
import net.easycloud.api.network.packet.PacketHandler;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class NettyServer implements NettyProvider {
    private final ChannelFuture channel;
    private final List<Channel> channels;

    private final EventLoopGroup bossEventLoopGroup;
    private final EventLoopGroup workerEventLoopGroup;
    private final PacketHandler packetHandler;

    public NettyServer(String host, int port) {
        this.channels = new ArrayList<>();

        this.bossEventLoopGroup = new NioEventLoopGroup();
        this.workerEventLoopGroup = new NioEventLoopGroup();
        this.packetHandler = new PacketHandler();

        try {
            this.channel = new ServerBootstrap()
                    .channel(NioServerSocketChannel.class)
                    .group(bossEventLoopGroup, workerEventLoopGroup)
                    .childHandler(new NettyServerInitializer(this))
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.AUTO_READ, true)
                    .bind(host, port)
                    .addListener(ChannelFutureListener.CLOSE_ON_FAILURE)
                    .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
                    .sync()
                    .channel()
                    .closeFuture();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private EventLoopGroup newEventLoopGroup(int threads) {
        //return (Epoll.isAvailable() ? new EpollEventLoopGroup(threads) : new NioEventLoopGroup(threads));
        return new NioEventLoopGroup(threads);
    }

    private Class<? extends SocketChannel> getSocketChannel() {
        //return (Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class);
        return NioSocketChannel.class;
    }

    @Override
    public void close() {
        channels.forEach(ChannelOutboundInvoker::close);
        this.channel.channel().close();

        this.bossEventLoopGroup.shutdownGracefully();
        this.workerEventLoopGroup.shutdownGracefully();
    }

    @Override
    public void sendPacket(Packet packet) {
        channels.forEach(it -> {
            it.writeAndFlush(packet);
        });
    }
}
