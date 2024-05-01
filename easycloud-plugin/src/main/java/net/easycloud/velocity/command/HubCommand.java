package net.easycloud.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.easycloud.velocity.VelocityPlugin;

public final class HubCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if(source instanceof Player player) {
            VelocityPlugin.getInstance().getServer().getAllServers().stream().filter(it -> it.getServerInfo().getName().startsWith(VelocityPlugin.getInstance().getModuleHandler().getConfig().getLobbyGroupName())).findFirst().ifPresentOrElse(server -> {
                player.createConnectionRequest(server).fireAndForget();
            }, () -> {
                player.sendMessage(Component.text("§bEasyCloud §8» §cNo lobby server found!"));
            });
        }
    }
}
