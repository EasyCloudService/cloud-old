package net.easycloud.base.console.runner;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public final class ConsoleRunner {
    private final byte[] bytes;
    private final StringBuffer stringBuffer;

    public ConsoleRunner() {
        this.bytes = new byte[2048];
        this.stringBuffer = new StringBuffer();
    }

    private void readStream(final InputStream inputStream, final Consumer<String> consumer) {
        int length;
        try {
            while (inputStream.available() > 0
                    && (length = inputStream.read(this.bytes, 0, this.bytes.length)) != -1) {
                this.stringBuffer.append(new String(this.bytes, 0, length, StandardCharsets.UTF_8));
            }
            final var string = this.stringBuffer.toString();
            if (string.contains("\n")) {
                for (final var s : string.split("\n")) consumer.accept(s);
            }
            this.stringBuffer.setLength(0);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
