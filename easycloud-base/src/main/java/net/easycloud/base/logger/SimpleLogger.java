package net.easycloud.base.logger;

import lombok.Getter;

import net.easycloud.api.console.LogType;
import net.easycloud.api.console.Logger;
import net.easycloud.api.console.LoggerAnsiFactory;
import net.easycloud.api.console.StaticConsoleInput;
import net.easycloud.base.console.SimpleConsole;
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
        if(!console.isInService()) {
            console.getCache().add(new StaticConsoleInput(System.currentTimeMillis(), text, logType));
        }
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
        var message = "&r" + text + "&r";
        if (logType != LogType.EMPTY) {
            message = "&7[" + dateFormat.format(Calendar.getInstance().getTime()) + "] " + logType.getPrefix() + ": &r" + message + "&r";
        }
        return LoggerAnsiFactory.toColorCode(message);
    }
}
