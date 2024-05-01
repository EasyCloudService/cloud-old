package net.easycloud.base.command.defaults;

import net.bytemc.evelon.repository.Filter;
import net.easycloud.api.console.LogType;
import net.easycloud.api.group.Group;
import net.easycloud.api.group.misc.GroupType;
import net.easycloud.api.group.misc.GroupVersion;
import net.easycloud.base.command.CloudCommand;
import net.easycloud.base.command.Command;
import net.easycloud.base.Base;

@Command(name = "group", description = "", aliases = {"groups"})
public final class GroupCommand extends CloudCommand {

    @Override
    protected void execute(String[] args) {
        var logger = Base.getInstance().getLogger();

        if (args.length >= 2) {
            if (args[1].equalsIgnoreCase("list")) {
                if (Base.getInstance().getGroupProvider().getRepository().query().database().findAll().isEmpty()) {
                    logger.log("There is currently no group!");
                    return;
                }

                Base.getInstance().getGroupProvider().getRepository().query().database().findAll().forEach(group -> {
                    logger.log(group.getName() + " " + argument(group.getMaxMemory() + "mb") + " §7| §f0 Online");
                });
                return;
            }
            if (args[1].equalsIgnoreCase("types")) {
                for (GroupType value : GroupType.values()) {
                    logger.log(value.name());
                }
                return;
            }
            if (args[1].equalsIgnoreCase("versions")) {
                for (GroupVersion version : GroupVersion.values()) {
                    logger.log(version.name() + " " + argument(version.getUrl()));
                }
                return;
            }
            if (args[1].equalsIgnoreCase("create")) {
                if (args.length == 6) {
                    try {
                        Base.getInstance().getGroupProvider().getRepository().query().filter(Filter.match("name", args[2]))
                                .database()
                                .findAll().stream().findFirst()
                                .ifPresentOrElse(group -> {
                                    logger.log("Group " + args[2] + " already exists!", LogType.WARNING);
                                }, () -> {
                                    Base.getInstance().getGroupProvider().create(new Group(args[2], Integer.parseInt(args[3]), 1, 1, 50, "ANVIL", GroupType.valueOf(args[4].toUpperCase()), GroupVersion.valueOf(args[5].toUpperCase())));
                                });

                    } catch (Exception exception) {
                        logger.log("Please provide valid values!", LogType.ERROR);
                    }
                    return;
                }
            }
        }
        logger.log("");
        logger.log("group create " + argument("name") + " " + argument("memory") + " " + argument("type") + " " + argument("version"));
        logger.log("group delete " + argument("name"));
        logger.log("group versions");
        logger.log("group list");
        logger.log("");
    }

    private String argument(String value) {
        return "§7[§f" + value + "§7]";
    }
}
