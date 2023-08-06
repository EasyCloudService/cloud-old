package net.purecloud.base.logger;

import lombok.Getter;

import net.purecloud.api.console.LogType;
import net.purecloud.api.console.Logger;
import net.purecloud.api.console.LoggerAnsiFactory;
import net.purecloud.base.Base;
import net.purecloud.base.console.SimpleConsole;
import net.purecloud.base.service.Service;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Getter
public final class SimpleLogger implements Logger {
    private final SimpleDateFormat dateFormat;
    private final SimpleConsole console;

    public SimpleLogger() {
        try {
            this.dateFormat = new SimpleDateFormat("HH:mm:ss");
            console = new SimpleConsole(this);
            console.start();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        System.setOut(new PrintStream(new LoggerOutputStream(this, LogType.INFO), true));
        System.setErr(new PrintStream(new LoggerOutputStream(this, LogType.ERROR), true));
    }

    @Override
    public void log(String text) {
        log(text, LogType.INFO);
    }

    @Override
    public void log(String text, LogType logType) {
        var terminal = console.getTerminal();
        var coloredMessage = format(text, logType);
        terminal.puts(InfoCmp.Capability.carriage_return);
        terminal.writer().println(coloredMessage);
        terminal.flush();
        console.redraw();
        //console.getCache().add(text);
    }

    @Override
    public void log(String[] text, LogType logType) {
        StringBuilder builder = new StringBuilder();
        for (String s : text) {
            if(!builder.isEmpty()) {
                builder.append(" ");
            }
            builder.append(s);
        }
        log(builder.toString(), logType);
    }

    @Override
    public String format(String text, LogType logType) {
        var message = "§r" + text + "§r";
        if (logType != LogType.EMPTY) {
            message = "§7" + logType.getPrefix() + " §f[§7" + dateFormat.format(Calendar.getInstance().getTime()) + "§f]" + " §r" + message + "§r";
        }
        return LoggerAnsiFactory.toColorCode(message);
    }
}
