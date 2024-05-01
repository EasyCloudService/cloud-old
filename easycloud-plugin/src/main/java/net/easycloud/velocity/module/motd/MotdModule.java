package net.easycloud.velocity.module.motd;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.Component;
import net.easycloud.velocity.VelocityPlugin;

public final class MotdModule {
    private final MotdConfig config;

    public MotdModule() {
        this.config = VelocityPlugin.getInstance().getModuleHandler().getMotdConfig();
    }

    @Subscribe
    public void onProxyPing(ProxyPingEvent event) {
        var builder = event.getPing().asBuilder();
        if(VelocityPlugin.getInstance().getModuleHandler().getConfig().getMaintenance()) {
            builder.version(new ServerPing.Version(1, "Maintenance"));
        }
        builder.description(Component.text(config.getLine1() + "\n" + config.getLine2())).build();
        event.setPing(builder.build());
    }
}
