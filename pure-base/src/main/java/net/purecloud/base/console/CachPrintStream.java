package net.purecloud.base.console;

import net.purecloud.base.Base;
import net.purecloud.base.service.Service;

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
            console.getCache().add(x);
        }
        super.println(x);
    }
}