package net.easycloud.base.console;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.easycloud.api.console.Logger;
import net.easycloud.api.console.StaticConsoleInput;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
public final class SimpleConsole {
    private final Logger logger;
    private final Terminal terminal;
    private final LineReader lineReader;
    private final List<StaticConsoleInput> cache;

    @Setter
    private boolean inService;
    private Thread consoleReadingThread;
    private final List<ConsoleInput> inputs;

    public SimpleConsole(Logger logger) throws IOException {
        this.logger = logger;

        this.terminal = TerminalBuilder.builder()
                .system(true)
                .streams(System.in, System.out)
                .encoding(StandardCharsets.UTF_8)
                .dumb(true)
                .build();
        this.lineReader = LineReaderBuilder.builder()
                .terminal(this.terminal)
                .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                .option(LineReader.Option.AUTO_REMOVE_SLASH, false)
                .option(LineReader.Option.INSERT_TAB, false)
                //.completer(null)
                .build();

        this.inService = true;
        this.cache = new ArrayList<>();
        this.inputs = new ArrayList<>();

        clearConsole();

        System.setOut(new CachPrintStream(this.terminal.output(), this));
    }

    @SuppressWarnings("ALL")
    public void start() {
        this.consoleReadingThread = new ConsoleReadingThread(logger, this);
        this.consoleReadingThread.setUncaughtExceptionHandler((t, exception) -> {
            if(exception instanceof UserInterruptException) {
                System.exit(0);
            }
            t.interrupt();
            exception.printStackTrace();
            throw new RuntimeException(exception);
        });
        this.consoleReadingThread.start();
    }

    public void clearConsole() {
        this.terminal.puts(InfoCmp.Capability.clear_screen);
        this.terminal.flush();
        this.redraw();
    }

    public void redraw() {
        if (this.lineReader.isReading()) {
            this.lineReader.callWidget(LineReader.REDRAW_LINE);
            this.lineReader.callWidget(LineReader.REDISPLAY);
        }
    }

    @SneakyThrows
    public void shutdownReading() {
        this.consoleReadingThread.interrupt();
        this.terminal.close();
    }

    public void addInput(Consumer<String> input, List<String> tabCompletions) {
        this.inputs.add(new ConsoleInput(input, tabCompletions));
    }
}
