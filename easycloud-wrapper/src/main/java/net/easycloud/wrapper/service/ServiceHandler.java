package net.easycloud.wrapper.service;

import lombok.Getter;
import net.easycloud.api.group.Group;
import net.easycloud.api.network.packet.defaults.ServiceConnectPacket;
import net.easycloud.api.network.packet.defaults.ServiceDisconnectPacket;
import net.easycloud.api.network.packet.defaults.ServiceRequestStartPacket;
import net.easycloud.api.network.packet.defaults.ServiceRequestStopPacket;
import net.easycloud.api.service.IService;
import net.easycloud.api.service.ServiceProvider;
import net.easycloud.wrapper.Wrapper;

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

        Wrapper.getInstance().getNettyClient().listen(ServiceConnectPacket.class, (channel, packet) -> {
            services.add(new Service(packet.getGroup(), packet.getName(), packet.getPort()));
        });

        Wrapper.getInstance().getNettyClient().listen(ServiceDisconnectPacket.class, (channel, packet) -> {
            services.removeIf(it -> it.getId().equals(packet.getName()));
        });
    }

    @Override
    public void start(Group group, int count) {
        Wrapper.getInstance().getNettyClient().sendPacket(new ServiceRequestStartPacket(group.getName(), count));
    }

    @Override
    public void stop(String id) {
        Wrapper.getInstance().getNettyClient().sendPacket(new ServiceRequestStopPacket(id));
    }

    @Override
    public IService getCurrentService() {
        return services.stream().filter(it -> it.getId().equals(Wrapper.getInstance().getName())).findFirst().orElseThrow();
    }
}
