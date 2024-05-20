package net.easycloud.base.service;

import lombok.Getter;
import net.easycloud.api.console.LogType;
import net.easycloud.api.group.Group;
import net.easycloud.api.group.misc.GroupType;
import net.easycloud.api.network.packet.*;
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

        Base.getInstance().getNettyServer().listen(ServiceRequestStartPacket.class, (channel, packet) -> {
            start(Base.getInstance().getGroupProvider().getOrThrow(packet.getGroupName()), packet.getCount());
        });

        Base.getInstance().getNettyServer().listen(ServiceRequestStopPacket.class, (channel, packet) -> {
            stop(packet.getServiceId());
        });

        Base.getInstance().getNettyServer().listen(ServiceStatePacket.class, (channel, packet) -> {
            var service = (Service) this.services.stream().filter(it -> it.getId().equalsIgnoreCase(packet.getName())).findFirst().orElseThrow();
            service.setState(packet.getState());
        });

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for (Group group : Base.getInstance().getGroupProvider().getRepository().query().find()) {
                if (group.getMinOnline() > 0) {
                    start(group, group.getMinOnline());
                }
            }
        }).start();
    }

    @Override
    public void start(Group group, int count) {
        new Thread(() -> {
            String id = group.getName() + "-" + (services.stream().filter(it -> it.getGroup().getName().equals(group.getName())).count() + 1);
            Base.getInstance().getLogger().log("§7Told §bInternalWrapper §7to start §b" + id + "§7...", LogType.WRAPPER);

            if (group.getMaxOnline() < (services.stream().filter(it -> it.getGroup().getName().equals(group.getName())).count() + 1) && group.getMaxOnline() != -1) {
                Base.getInstance().getLogger().log("§7Starting the §e" + group.getName() + " §7service-group §efailed§7. §7(§eMaximum number is reached§7).", LogType.WARNING);
                return;
            }

            servicePrepareHandler.createFiles(group, id);

            var port = (group.getType().equals(GroupType.PROXY) ? (25565 + Base.getInstance().getServiceProvider().getServices().stream().filter(it -> it.getGroup().getType().equals(GroupType.PROXY)).toList().size()) : getPort());
            var service = new Service(group, port, id);
            service.setProcess(ServiceProcessBuilder.buildProcess(service));

            services.add(service);

            Base.getInstance().getNettyServer().sendPacket(new ServiceConnectPacket(group, id, port));
            Base.getInstance().getLogger().log("§7Service §b" + id + " §7will be running on port§f: §7" + port, LogType.WRAPPER);

            if (count > 1) {
                start(group, count - 1);
            }
        }).start();
    }

    @Override
    public void stop(String id) {
        for (IService service : services.stream().filter(it -> it.getId().equalsIgnoreCase(id)).toList()) {
            services.remove(service);
            ((Service) service).stop(true);

            Base.getInstance().getNettyServer().sendPacket(new ServiceDisconnectPacket(service.getGroup(), id, service.getPort()));
        }
        update();
    }

    @Override
    public IService getCurrentService() {
        return null;
    }

    public void update() {
        new Thread(() -> {
            for (Group group : Base.getInstance().getGroupProvider().getRepository().query().find()) {
                var online = services.stream().filter(it -> it.getGroup().getName().equals(group.getName())).count();

                if (group.getMaxOnline() > online || group.getMaxOnline() == -1) {
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
