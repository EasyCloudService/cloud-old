package net.easycloud.api;

import dev.httpmarco.osgan.networking.client.NettyClient;
import lombok.experimental.Accessors;
import net.easycloud.api.event.EventHandler;
import net.easycloud.api.group.GroupProvider;
import net.easycloud.api.service.ServiceProvider;
import net.easycloud.api.user.UserProvider;
import net.easycloud.api.velocity.VelocityProvider;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ALL")
@Accessors(fluent = true)
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

    public static CloudDriver instance() {
        return instance;
    }


    @NotNull
    @Override
    public EventHandler eventProvider() {
        return eventHandler;
    }

    @NotNull
    @Override
    public GroupProvider groupProvider() {
        return groupProvider;
    }

    @NotNull
    @Override
    public NettyClient nettyClient() {
        return nettyClient;
    }

    @NotNull
    @Override
    public ServiceProvider serviceProvider() {
        return serviceProvider;
    }

    @NotNull
    @Override
    public VelocityProvider velocityProvider() {
        return velocityProvider;
    }

    @NotNull
    @Override
    public UserProvider userProvider() {
        return userProvider;
    }
}
