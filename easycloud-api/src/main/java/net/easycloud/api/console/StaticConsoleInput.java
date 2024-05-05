package net.easycloud.api.console;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class StaticConsoleInput {
    private final long timeMillis;
    private final String input;
    private final LogType logType;
}
