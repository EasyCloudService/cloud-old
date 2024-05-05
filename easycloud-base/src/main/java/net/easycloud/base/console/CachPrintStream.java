package net.easycloud.base.console;

import net.easycloud.api.console.LogType;
import net.easycloud.api.console.StaticConsoleInput;
import net.easycloud.base.Base;
import net.easycloud.base.service.Service;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class CachPrintStream extends PrintStream {
    private final SimpleConsole console;

    public CachPrintStream(OutputStream out, SimpleConsole console) {
        super(out, true, StandardCharsets.UTF_8);
        this.console = console;
    }

    @Override
    public void println(String x) {
        if (Base.getInstance().getServiceProvider().getServices().stream().noneMatch(it -> ((Service) it).isConsole())) {
            console.getCache().add(new StaticConsoleInput(System.currentTimeMillis(), x, LogType.INFO));
        }
        super.println(x);
    }
}