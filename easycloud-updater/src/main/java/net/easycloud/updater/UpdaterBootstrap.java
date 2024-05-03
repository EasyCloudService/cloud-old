package net.easycloud.updater;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class UpdaterBootstrap {

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1000);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                var jar = Path.of("easycloud-temp.jar");
                Files.copy(jar, Path.of("../" + args[0]), StandardCopyOption.REPLACE_EXISTING);
                jar.toFile().delete();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, "Shutdown-thread"));
        System.exit(0);
    }
}
