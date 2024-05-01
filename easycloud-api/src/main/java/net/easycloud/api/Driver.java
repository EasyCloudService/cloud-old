package net.easycloud.api;

import net.easycloud.api.event.EventHandler;
import net.easycloud.api.group.GroupProvider;
import net.easycloud.api.network.NettyProvider;
import net.easycloud.api.permission.PermissionProvider;
import net.easycloud.api.service.ServiceProvider;
import net.easycloud.api.velocity.VelocityProvider;
import org.jetbrains.annotations.NotNull;

public interface Driver {
    @NotNull
    EventHandler getEventHandler();
    @NotNull
    GroupProvider getGroupProvider();
    @NotNull
    NettyProvider getNettyProvider();
    @NotNull
    ServiceProvider getServiceProvider();
    @NotNull
    VelocityProvider getVelocityProvider();
    @NotNull
    PermissionProvider getPermissionProvider();

    void onShutdown();
}
