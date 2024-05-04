package net.easycloud.api.network.packet;

import io.netty.channel.ChannelHandlerContext;
import net.easycloud.api.network.packet.defaults.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PacketHandler {
    private final List<Class<? extends Packet>> packets;

    @SuppressWarnings("rawtypes")
    private final Map<Class<? extends Packet>, List<PacketListener>> subscribe;

    public PacketHandler() {
        this.packets = List.of(HandshakeAuthenticationPacket.class, PermissionUpdatePacket.class, PlayerConnectPacket.class, PlayerDisconnectPacket.class, ServiceConnectPacket.class, ServiceDisconnectPacket.class, ServiceRequestStartPacket.class, ServiceRequestStopPacket.class);
        this.subscribe = new HashMap<>();
    }

    public <T extends Packet> void subscribe(Class<T> clazz, PacketListener<T> listener) {
        var listeners = subscribe.getOrDefault(clazz, new ArrayList<>());
        listeners.add(listener);
        subscribe.put(clazz, listeners);
    }

    public int getPacketId(Class<? extends Packet> clazz) {
        return packets.indexOf(clazz);
    }

    public Class<? extends Packet> getPacketClass(int id) {
        return packets.get(id);
    }

    @SuppressWarnings("unchecked")
    public <T extends Packet> void call(ChannelHandlerContext channel, T packet) {
       if(subscribe.containsKey(packet.getClass())) {
            subscribe.get(packet.getClass()).forEach(listener -> {
                listener.handle(channel, packet);
            });
        }
    }
}
