package net.purecloud.api;

import net.purecloud.api.event.EventHandler;
import net.purecloud.api.group.GroupProvider;
import net.purecloud.api.network.NettyProvider;
import net.purecloud.api.network.server.NettyServer;
import net.purecloud.api.service.ServiceProvider;
import net.purecloud.api.velocity.VelocityProvider;
import org.jetbrains.annotations.NotNull;

public interface Driver {
    @NotNull EventHandler getEventHandler();
    @NotNull GroupProvider getGroupProvider();
    @NotNull NettyProvider getNettyProvider();
    @NotNull ServiceProvider getServiceProvider();
    @NotNull VelocityProvider getVelocityProvider();

    void onShutdown();
}
