package net.easycloud.base.command.defaults;

import net.easycloud.api.console.LogType;
import net.easycloud.api.service.IService;
import net.easycloud.base.command.CloudCommand;
import net.easycloud.base.command.Command;
import net.easycloud.base.Base;

@Command(name = "service", aliases = {"ser", "services"})
public final class ServiceCommand extends CloudCommand {

    @Override
    protected void execute(String[] args) {
        var logger = Base.getInstance().getLogger();

        if (args.length >= 2) {
            if (args[1].equalsIgnoreCase("shutdown") || args[1].equalsIgnoreCase("stop")) {
                if (Base.getInstance().groupProvider().getRepository().query().find().isEmpty()) {
                    logger.log("There is currently no group!");
                    return;
                }

                Base.getInstance().serviceProvider().getServices().stream().filter(it -> it.getId().equalsIgnoreCase(args[2])).findFirst().ifPresentOrElse(service -> {
                    Base.getInstance().serviceProvider().stop(service.getId());
                }, () -> {
                    logger.log("Please provide a valid service!", LogType.ERROR);
                });
                return;
            }
            if (args.length == 4) {
                if (args[1].equalsIgnoreCase("start")) {
                    Base.getInstance().groupProvider().getRepository().query().match("name", args[2])
                            .find().stream().findFirst().ifPresentOrElse(group -> {
                                try {
                                    Base.getInstance().serviceProvider().start(group, Integer.parseInt(args[3]));
                                } catch (Exception exception) {
                                    logger.log("Please provide valid values!", LogType.ERROR);
                                }
                            }, () -> {
                                logger.log("Please provide a valid group!", LogType.ERROR);
                            });
                    return;
                }
            }
            if (args[1].equalsIgnoreCase("list")) {
                for (IService service : Base.getInstance().serviceProvider().getServices()) {
                    logger.log("&7[&rOnline&7] &7[&f" + service.getGroup().getType() + "&7] &9" + service.getId());
                }
                return;
            }
        }
        logger.log("");
        logger.log("service start " + argument("group") + " " + argument("amount"));
        logger.log("service shutdown|stop " + argument("name"));
        logger.log("service list");
        logger.log("");
    }

    private String argument(String value) {
        return "§7[§f" + value + "§7]";
    }
}
