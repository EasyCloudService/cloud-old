package net.easycloud.api.console;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogType {
    EMPTY(""),
    INFO("&b&lINFO"),
    SUCCESS("&a&lSUCCESS"),
    WARNING("&e&lWARNING"),
    ERROR("&c&lERROR");

    private final String prefix;
}
