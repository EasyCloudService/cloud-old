package net.easycloud.api.console;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogType {
    EMPTY(""),
    INFO("&9&lINFO"),
    SYSTEM("&b&lSYSTEM"),
    SUCCESS("&a&lSUCCESS"),
    WARNING("&e&lWARNING"),
    NETTY("&c&lNETTY"),
    ERROR("&c&lERROR");

    private final String prefix;
}
