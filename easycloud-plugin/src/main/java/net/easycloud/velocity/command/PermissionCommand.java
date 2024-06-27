package net.easycloud.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.easycloud.UUIDFetcher;
import net.easycloud.api.CloudDriver;

public final class PermissionCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (source instanceof Player player) {
            if (CloudDriver.getInstance().getUserProvider().getUser(player.getUniqueId()) != null) {
                if (CloudDriver.getInstance().getUserProvider().getUser(player.getUniqueId()).getPermissions().stream().noneMatch(it -> it.equals("*") || it.equals("cloud.control") || it.equals("cloud.*"))) {
                    source.sendMessage(Component.text("§bEasyCloud §8» §cYou don't have permission for that§8!"));
                    return;
                }
            }
        }

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("user")) {
                if (args.length == 5) {
                    var user = CloudDriver.getInstance().getUserProvider().getUser(UUIDFetcher.getUUID(args[1]));
                    if (user == null) {
                        source.sendMessage(Component.text("§bEasyCloud §8» §cUser does not exists§8!"));
                        return;
                    }
                    if (args[3].equalsIgnoreCase("add")) {
                        user.addPermission(args[4]);
                        source.sendMessage(Component.text("§bEasyCloud §8» §7User has now the §e" + args[4].toLowerCase() + " §7Permission§8!"));
                        return;
                    }
                    if (args[3].equalsIgnoreCase("remove")) {
                        user.removePermission(args[4]);
                        source.sendMessage(Component.text("§bEasyCloud §8» §7User has now §close §7the §e" + args[4].toLowerCase() + " §7Permission§8!"));
                        return;
                    }
                }
                if (args.length == 2) {
                    var user = CloudDriver.getInstance().getUserProvider().getUser(UUIDFetcher.getUUID(args[1]));
                    source.sendMessage(Component.text());
                    source.sendMessage(Component.text("§bEasyCloud §8» §e" + args[1]));
                    for (String permission : user.getPermissions()) {
                        source.sendMessage(Component.text("§bEasyCloud §8» §8- §7" + permission));
                    }
                    source.sendMessage(Component.text());
                    return;
                }
                source.sendMessage(Component.text("§bEasyCloud §8» §7/perms user §8[§7name§8] §7perm add §8[§7perm§8]"));
                source.sendMessage(Component.text("§bEasyCloud §8» §7/perms user §8[§7name§8] §7perm remove §8[§7perm§8]"));
                return;
            }
            if (args[0].equalsIgnoreCase("group")) {
                if (args.length == 2) {
                    source.sendMessage(Component.text("§bEasyCloud §8» §7Service will be §cstopped§8..."));
                    CloudDriver.getInstance().getServiceProvider().stop(args[1]);
                    return;
                }
                source.sendMessage(Component.text("§bEasyCloud §8» §7/cs stop §8[§7service§8]"));
                return;
            }
        }

        source.sendMessage(Component.text("§bEasyCloud §8» §7All commands§8:"));
        source.sendMessage(Component.text("§bEasyCloud §8» §7/perms user §8[§7name§8] §7perm add §8[§7perm§8]"));
        source.sendMessage(Component.text("§bEasyCloud §8» §7/perms user §8[§7name§8] §7perm remove §8[§7perm§8]"));
    }
}
