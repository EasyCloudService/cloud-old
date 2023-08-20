package net.purecloud.wrapper.service;

import lombok.Getter;
import net.purecloud.api.group.Group;
import net.purecloud.api.network.packet.Packet;
import net.purecloud.api.network.packet.PacketListener;
import net.purecloud.api.network.packet.defaults.ServiceConnectPacket;
import net.purecloud.api.network.packet.defaults.ServiceDisconnectPacket;
import net.purecloud.api.network.packet.defaults.ServiceRequestStartPacket;
import net.purecloud.api.network.packet.defaults.ServiceRequestStopPacket;
import net.purecloud.api.service.IService;
import net.purecloud.api.service.ServiceProvider;
import net.purecloud.wrapper.Wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
public final class ServiceHandler implements ServiceProvider {
    private final List<IService> services;
    private final List<Consumer<IService>> consumers;

    public ServiceHandler() {
        this.services = new ArrayList<>();
        this.consumers = new ArrayList<>();

        var packetHandler = Wrapper.getInstance().getNettyProvider().getPacketHandler();
        packetHandler.subscribe(ServiceConnectPacket.class, (channel, packet) -> {
            services.add(new Service(packet.getGroup(), packet.getName(), packet.getPort()));
        });

        packetHandler.subscribe(ServiceDisconnectPacket.class, (channel, packet) -> {
            services.removeIf(it -> it.getId().equals(packet.getName()));
        });
    }

    @Override
    public void start(Group group, int count) {
        Wrapper.getInstance().getNettyProvider().sendPacket(new ServiceRequestStartPacket(group.getName(), count));
    }

    @Override
    public void stop(String id) {
        Wrapper.getInstance().getNettyProvider().sendPacket(new ServiceRequestStopPacket(id));
    }
}
