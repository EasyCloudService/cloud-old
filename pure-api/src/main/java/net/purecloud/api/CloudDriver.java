package net.purecloud.api;

import net.purecloud.api.event.EventHandler;
import net.purecloud.api.group.GroupProvider;
import net.purecloud.api.network.NettyProvider;
import net.purecloud.api.network.server.NettyServer;
import net.purecloud.api.service.ServiceProvider;
import net.purecloud.api.velocity.VelocityProvider;
import org.jetbrains.annotations.NotNull;

public abstract class CloudDriver implements Driver {
    private static CloudDriver instance;

    protected EventHandler eventHandler;
    protected GroupProvider groupProvider;
    protected NettyProvider nettyProvider;
    protected ServiceProvider serviceProvider;
    protected VelocityProvider velocityProvider;

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
}
