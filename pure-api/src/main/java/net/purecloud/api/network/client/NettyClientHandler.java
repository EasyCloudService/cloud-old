package net.purecloud.api.network.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import net.purecloud.api.network.packet.Packet;
import net.purecloud.api.network.packet.defaults.HandshakeAuthenticationPacket;

@AllArgsConstructor
public final class NettyClientHandler extends SimpleChannelInboundHandler<Packet> {
    private final NettyClient nettyClient;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
        this.nettyClient.getPacketHandler().call(channelHandlerContext, packet);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(new HandshakeAuthenticationPacket("23645gcji687456zhhj4c5u67z34tx5t6z3hu4x5"));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
    }

}
