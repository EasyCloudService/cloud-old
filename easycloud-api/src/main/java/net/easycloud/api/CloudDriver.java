package net.easycloud.api;

import dev.httpmarco.osgan.networking.client.NettyClient;
import net.easycloud.api.event.EventHandler;
import net.easycloud.api.group.GroupProvider;
import net.easycloud.api.service.ServiceProvider;
import net.easycloud.api.user.UserProvider;
import net.easycloud.api.velocity.VelocityProvider;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ALL")
public abstract class CloudDriver implements Driver {
    private static CloudDriver instance;

    protected EventHandler eventHandler;
    protected GroupProvider groupProvider;
    protected NettyClient nettyClient;
    protected ServiceProvider serviceProvider;
    protected VelocityProvider velocityProvider;
    protected UserProvider userProvider;

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
    public NettyClient getNettyClient() {
        return nettyClient;
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
    public UserProvider getUserProvider() {
        return userProvider;
    }
}
