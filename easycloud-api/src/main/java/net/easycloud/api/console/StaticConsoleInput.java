package net.easycloud.api.console;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("ClassCanBeRecord")
@Getter
@AllArgsConstructor
public final class StaticConsoleInput {
    private final long timeMillis;
    private final String input;
    private final LogType logType;
}
