package net.easycloud.wrapper;

import lombok.Getter;
import net.easycloud.api.CloudDriver;
import net.easycloud.wrapper.client.WrapperClient;
import net.easycloud.wrapper.event.SimpleEventHandler;
import net.easycloud.wrapper.group.SimpleGroupHandler;
import net.easycloud.wrapper.user.UserHandler;
import net.easycloud.wrapper.service.ServiceHandler;

@SuppressWarnings("ALL")
@Getter
public final class Wrapper extends CloudDriver {
    private static Wrapper instance;

    private final String name;

    public Wrapper(String name) {
        instance = this;

        this.name = name;
        this.nettyProvider = new WrapperClient();
        this.eventHandler = new SimpleEventHandler();
        this.serviceProvider = new ServiceHandler();
        this.groupProvider = new SimpleGroupHandler();
        this.userProvider = new UserHandler();

        Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));
    }

    @Override
    public void onShutdown() {
        WrapperBootstrap.getThread().interrupt();
    }

    public static Wrapper getInstance() {
        return (Wrapper) instance;
    }
}
