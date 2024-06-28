package net.easycloud.wrapper.service;

import lombok.Getter;
import net.easycloud.api.group.Group;
import net.easycloud.api.network.packet.*;
import net.easycloud.api.service.IService;
import net.easycloud.api.service.ServiceProvider;
import net.easycloud.api.service.state.ServiceState;
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

        Wrapper.instance().nettyClient().listen(ServiceStatePacket.class, (channel, packet) -> {
            var service = (Service) this.services.stream().filter(it -> it.getId().equalsIgnoreCase(packet.getName())).findFirst().orElseThrow();
            service.setState(packet.getState());
        });

        Wrapper.instance().nettyClient().listen(ServiceConnectPacket.class, (channel, packet) -> {
            this.services.add(new Service(packet.getGroup(), packet.getName(), packet.getPort(), ServiceState.STARTING));
        });

        Wrapper.instance().nettyClient().listen(ServiceDisconnectPacket.class, (channel, packet) -> {
            this.services.removeIf(it -> it.getId().equals(packet.getName()));
        });
    }

    @Override
    public void start(Group group, int count) {
        Wrapper.instance().nettyClient().sendPacket(new ServiceRequestStartPacket(group.getName(), count));
    }

    @Override
    public void stop(String id) {
        Wrapper.instance().nettyClient().sendPacket(new ServiceRequestStopPacket(id));
    }

    @Override
    public IService getCurrentService() {
        return services.stream().filter(it -> it.getId().equals(Wrapper.instance().name())).findFirst().orElseThrow();
    }
}
