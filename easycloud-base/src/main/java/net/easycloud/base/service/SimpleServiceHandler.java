package net.easycloud.base.service;

import lombok.Getter;
import net.easycloud.api.console.LogType;
import net.easycloud.api.group.Group;
import net.easycloud.api.group.misc.GroupType;
import net.easycloud.api.network.packet.defaults.ServiceConnectPacket;
import net.easycloud.api.network.packet.defaults.ServiceDisconnectPacket;
import net.easycloud.api.network.packet.defaults.ServiceRequestStartPacket;
import net.easycloud.api.network.packet.defaults.ServiceRequestStopPacket;
import net.easycloud.api.service.IService;
import net.easycloud.api.service.ServiceProvider;
import net.easycloud.base.service.process.ServiceProcessBuilder;
import net.easycloud.base.Base;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class SimpleServiceHandler implements ServiceProvider {
    private final List<IService> services;
    private final ServicePrepareHandler servicePrepareHandler;

    public SimpleServiceHandler() {
        this.services = new ArrayList<>();
        this.servicePrepareHandler = new ServicePrepareHandler();

        Base.getInstance().getNettyProvider().getPacketHandler().subscribe(ServiceRequestStartPacket.class, (channel, packet) -> {
            start(Base.getInstance().getGroupProvider().getOrThrow(packet.getGroupName()), packet.getCount());
        });

        Base.getInstance().getNettyProvider().getPacketHandler().subscribe(ServiceRequestStopPacket.class, (channel, packet) -> {
            stop(packet.getServiceId());
        });

        for (Group group : Base.getInstance().getGroupProvider().getRepository().query().database().findAll()) {
            if (group.getMinOnline() > 0) {
                start(group, group.getMinOnline());
            }
        }
    }

    @Override
    public void start(Group group, int count) {
        new Thread(() -> {
            String id = group.getName() + "-" + (services.stream().filter(it -> it.getGroup().getName().equals(group.getName())).count() + 1);
            Base.getInstance().getLogger().log("§7Told §bInternalWrapper §7to start §b" + id + "§7...", LogType.WRAPPER);

            if (group.getMaxOnline() < (services.stream().filter(it -> it.getGroup().getName().equals(group.getName())).count() + 1)) {
                Base.getInstance().getLogger().log("§cCant §7start §e" + group.getName() + " §7maximum number is reached.", LogType.WARNING);
                return;
            }

            servicePrepareHandler.createFiles(group, id);

            var port = (group.getType().equals(GroupType.PROXY) ? 25565 : getPort());
            var service = new Service(group, port, id);
            service.setProcess(ServiceProcessBuilder.buildProcess(service));

            services.add(service);

            Base.getInstance().getNettyProvider().sendPacket(new ServiceConnectPacket(group, id, port));

            if (count > 1) {
                start(group, count - 1);
            }
        }).start();
    }

    @Override
    public void stop(String id) {
        for (IService service : services.stream().filter(it -> it.getId().equalsIgnoreCase(id)).toList()) {
            ((Service) service).stop();
            services.remove(service);

            Base.getInstance().getNettyProvider().sendPacket(new ServiceDisconnectPacket(service.getGroup(), id, service.getPort()));
        }
        update();
    }

    @Override
    public IService getCurrentService() {
        return null;
    }

    public void update() {
        new Thread(() -> {
            for (Group group : Base.getInstance().getGroupProvider().getRepository().query().database().findAll()) {
                var online = services.stream().filter(it -> it.getGroup().getName().equals(group.getName())).count();

                if(group.getMaxOnline() > online) {
                    if (group.getMinOnline() > services.stream().filter(it -> it.getGroup().getName().equals(group.getName())).count()) {
                        start(group, group.getMinOnline());
                    }
                }
            }
        }).start();
    }

    public int getPort() {
        for (int i = 0; i < 1000; i++) {
            int port = 3000 + i;
            if (services.stream().noneMatch(it -> it.getPort() == port)) {
                return port;
            }
        }
        throw new RuntimeException("There was no free port!");
    }
}
