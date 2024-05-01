package net.easycloud.base;

import org.fusesource.jansi.AnsiConsole;

public class BaseBootstrap {

    public static void main(String[] args) {
        AnsiConsole.systemInstall();

        new Base();
    }
}
