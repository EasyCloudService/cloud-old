package net.purecloud.api.console;

public interface Logger {
    void log(String text);
    void log(String text, LogType logType);
    void log(String[] text, final LogType logType);

    String format(String text, LogType logType);
}
