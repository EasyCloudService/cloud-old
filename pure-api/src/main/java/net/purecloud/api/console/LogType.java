package net.purecloud.api.console;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogType {
    EMPTY(""),
    INFO("§bINFO"),
    SUCCESS("§aSUCCESS"),
    WARNING("§eWARNING"),
    ERROR("§cERROR");

    private final String prefix;
}
