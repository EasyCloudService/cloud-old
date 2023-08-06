package net.purecloud.base.command.defaults;

import net.purecloud.api.console.LogType;
import net.purecloud.api.group.misc.GroupType;
import net.purecloud.api.group.misc.GroupVersion;
import net.purecloud.base.Base;
import net.purecloud.base.command.CloudCommand;
import net.purecloud.base.command.Command;

@Command(name = "service", description = "", aliases = {"ser", "services"})
public final class ServiceCommand extends CloudCommand {

    @Override
    protected void execute(String[] args) {
        var logger = Base.getInstance().getLogger();

        if (args.length >= 2) {
            if (args[1].equalsIgnoreCase("list")) {
                if (Base.getInstance().getGroupProvider().getRepository().findAll().isEmpty()) {
                    logger.log("There is currently no group!");
                    return;
                }

                Base.getInstance().getGroupProvider().getRepository().findAll().forEach(group -> {
                    logger.log(group.getName() + " " + argument(group.getMaxMemory() + "mb") + " §7| " + "§f0 Online");
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
            if (args.length == 4) {
                if (args[1].equalsIgnoreCase("start")) {
                    Base.getInstance().getGroupProvider().getRepository().filter()
                            .value("name", args[2])
                            .complete()
                            .findFirst().ifPresentOrElse(group -> {
                                try {
                                    Base.getInstance().getServiceProvider().start(group, Integer.parseInt(args[3]));
                                } catch (Exception exception) {
                                    logger.log("Please provide valid values!", LogType.ERROR);
                                }
                            }, () -> {
                                logger.log("Please provide a valid group!", LogType.ERROR);
                            });
                    return;
                }
            }
        }
        logger.log("");
        logger.log("service start " + argument("group") + " " + argument("amount"));
        logger.log("service shutdown " + argument("name"));
        logger.log("");
    }

    private String argument(String value) {
        return "§7[§f" + value + "§7]";
    }
}
