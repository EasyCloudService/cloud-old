package net.purecloud.api.network.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import net.purecloud.api.network.packet.Packet;
import net.purecloud.api.network.packet.defaults.HandshakeAuthenticationPacket;

@AllArgsConstructor
public class NettyServerChannel extends SimpleChannelInboundHandler<Packet> {
    private final NettyServer nettyServer;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
        //System.out.println("[DEBUG] NettyServer recived: " + packet);

        if(packet instanceof HandshakeAuthenticationPacket authPacket) {
            if(authPacket.getKey().equals("23645gcji687456zhhj4c5u67z34tx5t6z3hu4x5")) {
                nettyServer.getChannels().add(channelHandlerContext.channel());
            } else {
                System.err.println("NettyClient try to connect with wrong key!");
                return;
            }
        }
        nettyServer.getPacketHandler().call(channelHandlerContext, packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (!cause.getMessage().equals("Connection reset")) cause.printStackTrace();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        nettyServer.getChannels().remove(ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        nettyServer.getChannels().remove(ctx.channel());
    }
}
