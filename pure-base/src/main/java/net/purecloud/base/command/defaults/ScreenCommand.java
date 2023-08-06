package net.purecloud.base.command.defaults;

import net.purecloud.api.console.LogType;
import net.purecloud.base.Base;
import net.purecloud.base.command.CloudCommand;
import net.purecloud.base.command.Command;
import net.purecloud.base.logger.SimpleLogger;
import net.purecloud.base.service.Service;

@Command(name = "screen", description = "", aliases = {"scr", "console"})
public final class ScreenCommand extends CloudCommand {

    @Override
    protected void execute(String[] args) {
        var logger = Base.getInstance().getLogger();

        if (args.length == 2) {
            Base.getInstance().getServiceProvider().getServices().stream().filter(service -> service.getId().equalsIgnoreCase(args[1])).findFirst().ifPresentOrElse(service -> {
                var baseService = (Service) service;
                ((SimpleLogger) Base.getInstance().getLogger()).getConsole().clearConsole();
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
