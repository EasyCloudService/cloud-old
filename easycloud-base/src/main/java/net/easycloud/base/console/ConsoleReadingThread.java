package net.easycloud.base.console;

import net.easycloud.api.console.LogType;
import net.easycloud.api.console.Logger;
import net.easycloud.base.Base;
import net.easycloud.base.logger.SimpleLogger;
import net.easycloud.base.service.Service;
import net.easycloud.base.setup.ConsoleSetup;
import org.jline.reader.LineReader;

@SuppressWarnings("unused")
public class ConsoleReadingThread extends Thread {
    private final String consolePrompt;

    @SuppressWarnings("FieldCanBeLocal")
    private final SimpleConsole console;
    private final LineReader lineReader;

    public ConsoleReadingThread(Logger logger, SimpleConsole console) {
        super("Console-Thread");

        this.console = console;
        this.lineReader = console.getLineReader();
        this.consolePrompt = logger.format("&7▶▷ &fEasy&9Cloud &7» &r", LogType.EMPTY);
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            var line = this.lineReader.readLine(this.consolePrompt);
            if (line != null && !line.isEmpty()) {
                if(Base.instance().running()) {
                    //TODO
                } if (ConsoleSetup.SETUP_ENABLED) {
                    ConsoleSetup.pushLine(line);
                } else if (Base.instance().serviceProvider().getServices().stream().anyMatch(it -> ((Service) it).isConsole())) {
                    Base.instance().serviceProvider().getServices().stream().filter(it -> ((Service) it).isConsole()).forEach(service -> {
                        if (line.equalsIgnoreCase("leave")) {
                            ((Service) service).setConsole(false);
                            ((SimpleLogger) Base.instance().logger()).getConsole().clearConsole();
                            ((SimpleLogger) Base.instance().logger()).getConsole().getCache().forEach(it -> Base.instance().logger().log(it.getInput(), it.getLogType()));
                            ((SimpleLogger) Base.instance().logger()).getConsole().setInService(false);

                            return;
                        }
                        ((Service) service).execute(line);
                    });
                } else {
                    Base.instance().commandHandler().runCommand(line.split(" ")[0], line.replace(line.split(" ")[0], "").split(" "));
                }
            }
        }
    }

}