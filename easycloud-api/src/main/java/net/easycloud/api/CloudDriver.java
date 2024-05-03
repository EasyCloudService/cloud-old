package net.easycloud.api;

import net.easycloud.api.event.EventHandler;
import net.easycloud.api.group.GroupProvider;
import net.easycloud.api.network.NettyProvider;
import net.easycloud.api.service.ServiceProvider;
import net.easycloud.api.permission.PermissionProvider;
import net.easycloud.api.velocity.VelocityProvider;
import org.jetbrains.annotations.NotNull;

public abstract class CloudDriver implements Driver {
    private static CloudDriver instance;

    protected EventHandler eventHandler;
    protected GroupProvider groupProvider;
    protected NettyProvider nettyProvider;
    protected ServiceProvider serviceProvider;
    protected VelocityProvider velocityProvider;
    protected PermissionProvider permissionProvider;

    protected CloudDriver() {
        instance = this;
    }

    public static CloudDriver getInstance() {
        return instance;
    }

    @NotNull
    @Override
    public EventHandler getEventHandler() {
        return eventHandler;
    }

    @NotNull
    @Override
    public GroupProvider getGroupProvider() {
        return groupProvider;
    }

    @NotNull
    @Override
    public NettyProvider getNettyProvider() {
        return nettyProvider;
    }

    @NotNull
    @Override
    public ServiceProvider getServiceProvider() {


        return serviceProvider;
    }

    @NotNull
    @Override
    public VelocityProvider getVelocityProvider() {
        return velocityProvider;
    }

    @NotNull
    @Override
    public PermissionProvider getPermissionProvider() {
        return permissionProvider;
    }
}
