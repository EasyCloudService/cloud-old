package net.easycloud.api;

import dev.httpmarco.osgan.networking.client.NettyClient;
import net.easycloud.api.event.EventHandler;
import net.easycloud.api.group.GroupProvider;
import net.easycloud.api.user.UserProvider;
import net.easycloud.api.service.ServiceProvider;
import net.easycloud.api.velocity.VelocityProvider;
import org.jetbrains.annotations.NotNull;

public interface Driver {
    @NotNull
    EventHandler getEventHandler();
    @NotNull
    GroupProvider getGroupProvider();
    @NotNull
    NettyClient getNettyClient();
    @NotNull
    ServiceProvider getServiceProvider();
    @NotNull
    VelocityProvider getVelocityProvider();
    @NotNull
    UserProvider getUserProvider();

    void onShutdown();
}
