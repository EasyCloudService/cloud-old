package net.purecloud.api.network.packet;

import io.netty.channel.ChannelHandlerContext;

public interface PacketListener<T> {
    void handle(final ChannelHandlerContext channel, T t);

}