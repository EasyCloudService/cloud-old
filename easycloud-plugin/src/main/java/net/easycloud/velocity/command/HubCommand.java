package net.easycloud.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.easycloud.api.CloudDriver;
import net.easycloud.api.group.misc.GroupType;
import net.kyori.adventure.text.Component;
import net.easycloud.velocity.VelocityPlugin;

public final class HubCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if(source instanceof Player player) {
            // LOBBY GROUP
            VelocityPlugin.getInstance().getServer().getAllServers().stream().filter(it -> {
                return CloudDriver.getInstance().getGroupProvider().getOrThrow(it.getServerInfo().getName().split("-")[0].replace("-", "")).getType().equals(GroupType.LOBBY);
            }).findFirst().ifPresentOrElse(server -> {
                player.createConnectionRequest(server).fireAndForget();
            }, () -> {
                player.sendMessage(Component.text("§bEasyCloud §8» §cNo lobby server found!"));
            });
        }
    }
}
