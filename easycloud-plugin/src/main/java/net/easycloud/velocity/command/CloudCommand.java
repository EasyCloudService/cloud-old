package net.easycloud.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.easycloud.api.CloudDriver;

public final class CloudCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if(source instanceof Player player) {
            if(CloudDriver.instance().userProvider().getUser(player.getUniqueId()) != null) {
                if(CloudDriver.instance().userProvider().getUser(player.getUniqueId()).getPermissions().stream().noneMatch(it -> it.equals("*") || it.equals("cloud.control") || it.equals("cloud.*"))) {
                    source.sendMessage(Component.text("§bEasyCloud §8» §cYou don't have permission for that§8!"));
                    return;
                }
            }
        }

        if(args.length == 0) {
            source.sendMessage(Component.text("§bEasyCloud §8» §7Thats a Cloud you want to see it§8? §b☁"));
        }

        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase("start")) {
                if(args.length == 2) {
                    source.sendMessage(Component.text("§bEasyCloud §8» §7Service will be §astarted§8..."));
                    CloudDriver.instance().serviceProvider().start(CloudDriver.instance().groupProvider().getOrThrow(args[1]), 1);
                    return;
                }
                source.sendMessage(Component.text("§bEasyCloud §8» §7/cs start §8[§7group§8]"));
            }
            if(args[0].equalsIgnoreCase("stop")) {
                if(args.length == 2) {
                    source.sendMessage(Component.text("§bEasyCloud §8» §7Service will be §cstopped§8..."));
                    CloudDriver.instance().serviceProvider().stop(args[1]);
                    return;
                }
                source.sendMessage(Component.text("§bEasyCloud §8» §7/cs stop §8[§7service§8]"));
            }

            source.sendMessage(Component.text("§bEasyCloud §8» §7All commands§8:"));
            source.sendMessage(Component.text("§bEasyCloud §8» §7/cs start §8[§7group§8]"));
            source.sendMessage(Component.text("§bEasyCloud §8» §7/cs stop §8[§7service§8]"));
        }
    }
}
