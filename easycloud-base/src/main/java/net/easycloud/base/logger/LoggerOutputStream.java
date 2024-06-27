package net.easycloud.base.logger;

import net.easycloud.api.console.LogType;
import net.easycloud.api.console.Logger;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public final class LoggerOutputStream extends ByteArrayOutputStream {
    private final Logger logger;
    private final LogType logType;

    public LoggerOutputStream(final Logger logger, final LogType logType) {
        this.logger = logger;
        this.logType = logType;
    }

    @Override
    public void flush() {
        final var input = this.toString(StandardCharsets.UTF_8);
        this.reset();
        if (input != null && !input.isEmpty()) {
            if(!input.startsWith("Received packet") && !input.startsWith("Connection established with") && !input.startsWith("Connection closed with")) {
                this.logger.log(input.split("\n"), this.logType);
            }
        }
    }
}