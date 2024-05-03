package net.easycloud.base.logger;

import lombok.Getter;

import net.easycloud.api.console.LogType;
import net.easycloud.api.console.Logger;
import net.easycloud.api.console.LoggerAnsiFactory;
import net.easycloud.base.console.SimpleConsole;
import org.jline.builtins.Completers;
import org.jline.console.impl.Builtins;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.reader.impl.completer.SystemCompleter;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

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



       /* Builtins builtins = new Builtins(console., configPath, widgetCreator);
        SystemCompleter systemCompleter = builtins.compileCompletor(); // builtins commands completers
        systemCompleter.add("test", new StringsCompleter("cmd1"));                  // option descriptions
        systemCompleter.compile();                                     // prepare it for LineReader
        LineReader reader = LineReaderBuilder.builder()
                .terminal(console.getTerminal()).parser(console.getLineReader().getParser())
                .completer(systemCompleter)
                .build();
        builtins.setLineReader(reader);// to complite set builtins lineReader
*/



        //var completer = new ArgumentCompleter(
          //      new StringsCompleter("bar", "baz"),
            //    new StringsCompleter("foo"),
              //  new StringsCompleter("ree"));
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
        var message = "&r" + text + "&r";
        if (logType != LogType.EMPTY) {
            message = "&7[" + dateFormat.format(Calendar.getInstance().getTime()) + "] " + logType.getPrefix() + ": &r" + message + "&r";
        }
        return LoggerAnsiFactory.toColorCode(message);
    }
}
