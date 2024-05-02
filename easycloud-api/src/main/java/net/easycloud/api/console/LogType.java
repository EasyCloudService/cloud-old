package net.easycloud.api.console;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogType {
    EMPTY(""),
    INFO("&9&lINFO"),
    WRAPPER("&b&lWRAPPER"),
    SUCCESS("&a&lSUCCESS"),
    WARNING("&e&lWARNING"),
    ERROR("&c&lERROR");

    private final String prefix;
}
