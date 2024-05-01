package net.easycloud.loader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public final class LoaderBoostrap {

    public static void main(String[] args) {
        try {
            Path storage = Path.of("storage");
            Path basePath = Path.of("storage/base.jar");

            if(!Files.exists(storage)) {
                Files.createDirectory(storage);
            }

            Files.copy(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("easycloud-base.jar")), basePath, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("easycloud-wrapper.jar")), Path.of("storage/wrapper.jar"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("Cloud-API.jar")), Path.of("storage/plugin.jar"), StandardCopyOption.REPLACE_EXISTING);

            final var classLoader = new URLClassLoader(new URL[]{basePath.toUri().toURL()}, ClassLoader.getSystemClassLoader()) {
                @Override
                public void addURL(URL url) {
                    super.addURL(url);
                }
            };
            Thread.currentThread().setContextClassLoader(classLoader);

            classLoader.loadClass("net.easycloud.base.BaseBootstrap").getMethod("main", String[].class).invoke(null, (Object) args);
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }
}
