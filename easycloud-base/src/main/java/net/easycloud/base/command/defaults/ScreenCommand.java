package net.easycloud.base.command.defaults;

import net.easycloud.api.console.LogType;
import net.easycloud.base.command.CloudCommand;
import net.easycloud.base.command.Command;
import net.easycloud.base.logger.SimpleLogger;
import net.easycloud.base.service.Service;
import net.easycloud.base.Base;

@Command(name = "screen", aliases = {"scr", "console"})
public final class ScreenCommand extends CloudCommand {

    @Override
    protected void execute(String[] args) {
        var logger = Base.getInstance().getLogger();

        if (args.length == 2) {
            Base.getInstance().getServiceProvider().getServices().stream().filter(service -> service.getId().equalsIgnoreCase(args[1])).findFirst().ifPresentOrElse(service -> {
                var baseService = (Service) service;
                ((SimpleLogger) Base.getInstance().getLogger()).getConsole().clearConsole();
                ((SimpleLogger) Base.getInstance().getLogger()).getConsole().setInService(true);
                baseService.getConsoleCache().forEach(line -> Base.getInstance().getLogger().log("§7[§r" + service.getId() + "§7] §r" + line));
                Base.getInstance().getLogger().log("");
                Base.getInstance().getLogger().log("§7[§r" + service.getId() + "§7] §rWrite §eleave §rto leave the screen!");
                baseService.setConsole(true);

            }, () -> {
                logger.log("Please provide a valid service!", LogType.ERROR);
            });
            return;
        }

        logger.log("");
        logger.log("screen §7[§fname§7]");
        logger.log("");
    }
}
