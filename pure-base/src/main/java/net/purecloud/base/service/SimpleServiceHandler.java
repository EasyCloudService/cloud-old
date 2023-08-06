package net.purecloud.base.service;

import lombok.Getter;
import net.purecloud.api.console.LogType;
import net.purecloud.api.group.Group;
import net.purecloud.api.group.misc.GroupType;
import net.purecloud.api.network.packet.defaults.ServiceConnectPacket;
import net.purecloud.api.network.packet.defaults.ServiceDisconnectPacket;
import net.purecloud.api.network.packet.defaults.ServiceRequestStartPacket;
import net.purecloud.api.network.packet.defaults.ServiceRequestStopPacket;
import net.purecloud.api.service.IService;
import net.purecloud.api.service.ServiceProvider;
import net.purecloud.base.Base;
import net.purecloud.base.service.process.ServiceProcessBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

        update();
    }

    @Override
    public void start(Group group, int count) {
        new Thread(() -> {
            String id = group.getName() + "-" + (services.stream().filter(it -> it.getGroup().getName().equals(group.getName())).count() + 1);
            Base.getInstance().getLogger().log("§7Told §bInternalWrapper §7to start §e" + id + "§7...");

            if (group.getMaxOnline() < (services.stream().filter(it -> it.getGroup().getName().equals(group.getName())).count() + 1)) {
                Base.getInstance().getLogger().log("§7Told §bInternalWrapper §7to §cstop §e" + id + " §7cause §emaximum limit§7...");
                return;
            }

            servicePrepareHandler.createFiles(group, id);

            var port = (group.getType().equals(GroupType.PROXY) ? 25565 : getPort());
            var service = new Service(group, port, id);
            service.setProcess(ServiceProcessBuilder.buildProcess(service));

            services.add(service);

            Base.getInstance().getNettyProvider().sendPacket(new ServiceConnectPacket(group, id, port));

            if(count > 1) {
                start(group, count - 1);
            }
        }).start();

        if(count == 1) {
            update();
        }
    }

    @Override
    public void stop(String id) {
        for (IService service : services.stream().filter(it -> it.getId().equalsIgnoreCase(id)).toList()) {
            ((Service) service).stop();
            services.remove(service);

            Base.getInstance().getNettyProvider().sendPacket(new ServiceDisconnectPacket(id, service.getGroup(), service.getPort()));
        }
        update();
    }

    public void update() {
        new Thread(() -> {
            try {
                Thread.sleep(500);
                for (Group group : Base.getInstance().getGroupProvider().getRepository().findAll()) {
                    if (group.getMinOnline() > services.stream().filter(it -> it.getGroup().getName().equals(group.getName())).count()) {
                        start(group, (int) (group.getMinOnline() - services.stream().filter(it -> it.getGroup().equals(group)).count()));
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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
