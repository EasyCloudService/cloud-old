package net.easycloud.velocity.module.tablist;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.easycloud.velocity.VelocityPlugin;

import java.util.concurrent.TimeUnit;

public final class TablistModule {
    private final TablistConfig config;

    public TablistModule() {
        this.config = VelocityPlugin.getInstance().getModuleHandler().getConfig().getTablist();

        VelocityPlugin.getInstance().getServer().getScheduler().buildTask(VelocityPlugin.getInstance(), () -> {
            VelocityPlugin.getInstance().getServer().getAllPlayers().forEach(player -> {
                player.getTabList().setHeaderAndFooter(Component.text(replace(player, config.getHeader())), Component.text(replace(player, config.getFooter())));
            });
        }).repeat(2, TimeUnit.SECONDS).schedule();
    }

    private String replace(Player player, String text) {
        var server = player.getCurrentServer().orElse(null);
        if(server == null) {
            return "§cError";
        }
        return text
                .replace("%online%", String.valueOf(VelocityPlugin.getInstance().getServer().getPlayerCount()))
                .replace("%max%", String.valueOf(VelocityPlugin.getInstance().getServer().getConfiguration().getShowMaxPlayers()))
                .replace("%server%", server.getServerInfo().getName())
                .replace("\\n", "\n");
    }
}
