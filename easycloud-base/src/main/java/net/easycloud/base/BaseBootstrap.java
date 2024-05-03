package net.easycloud.base;

import org.fusesource.jansi.AnsiConsole;

import java.util.Arrays;

public class BaseBootstrap {

    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        new Base(args[0], Arrays.asList(args).contains("--ignore-update"));
    }
}
