package net.easycloud.base.console;

import net.easycloud.api.console.LogType;
import net.easycloud.api.console.Logger;
import net.easycloud.base.Base;
import net.easycloud.base.logger.SimpleLogger;
import net.easycloud.base.service.Service;
import net.easycloud.base.setup.ConsoleSetup;
import org.jline.reader.LineReader;

public class ConsoleReadingThread extends Thread {
    private final String consolePrompt;

    private final SimpleConsole console;
    private final LineReader lineReader;

    public ConsoleReadingThread(Logger logger, SimpleConsole console) {
        super("Console-Thread");

        this.console = console;
        this.lineReader = console.getLineReader();
        //this.consolePrompt = logger.format("&feasy&7@&bCloud &7» &r", LogType.EMPTY);
        this.consolePrompt = logger.format("&7▶▷ &fEasy&9Cloud &7» &r", LogType.EMPTY);
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            var line = this.lineReader.readLine(this.consolePrompt);

            if (line != null && !line.isEmpty()) {
                //var input = console.getInputs().poll();
                //if (input != null) {
                //input.input().accept(line);
                //} else {
                if(ConsoleSetup.SETUP_ENABLED) {
                    ConsoleSetup.pushLine(line);
                } else if (Base.getInstance().getServiceProvider().getServices().stream().anyMatch(it -> ((Service) it).isConsole())) {
                    Base.getInstance().getServiceProvider().getServices().stream().filter(it -> ((Service) it).isConsole()).forEach(service -> {
                        if (line.equalsIgnoreCase("leave")) {
                            ((Service) service).setConsole(false);
                            Base.getInstance().printScreen();
                            //((SimpleLogger) Base.getInstance().getLogger()).getConsole().clearConsole();
                            //((SimpleLogger) Base.getInstance().getLogger()).getConsole().getCache().forEach(it -> Base.getInstance().getLogger().log(it));

                            return;
                        }
                        ((Service) service).execute(line);
                    });
                } else {
                    Base.getInstance().getCommandHandler().runCommand(line.split(" ")[0], line.replace(line.split(" ")[0], "").split(" "));
                }
                // }
            }
        }
    }

}