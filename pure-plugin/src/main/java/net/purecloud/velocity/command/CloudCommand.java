package net.purecloud.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.purecloud.api.CloudDriver;

public final class CloudCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if(args.length == 0) {
            source.sendMessage(Component.text("§bPureCloud §8» §7Thats a Cloud you want to see it§8? §b☁"));
        }

        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase("start")) {
                if(args.length == 2) {
                    source.sendMessage(Component.text("§bPureCloud §8» §7Service will be §astarted§8..."));
                    CloudDriver.getInstance().getServiceProvider().start(CloudDriver.getInstance().getGroupProvider().getOrThrow(args[1]), 1);
                    return;
                }
                source.sendMessage(Component.text("§bPureCloud §8» §7/cs start §8[§7group§8]"));
            }
            if(args[0].equalsIgnoreCase("stop")) {
                if(args.length == 2) {
                    source.sendMessage(Component.text("§bPureCloud §8» §7Service will be §cstopped§8..."));
                    CloudDriver.getInstance().getServiceProvider().stop(args[1]);
                    return;
                }
                source.sendMessage(Component.text("§bPureCloud §8» §7/cs stop §8[§7service§8]"));
            }

            source.sendMessage(Component.text("§bPureCloud §8» §7All commands§8:"));
            source.sendMessage(Component.text("§bPureCloud §8» §7/cs start §8[§7group§8]"));
            source.sendMessage(Component.text("§bPureCloud §8» §7/cs stop §8[§7service§8]"));
        }
    }
}
