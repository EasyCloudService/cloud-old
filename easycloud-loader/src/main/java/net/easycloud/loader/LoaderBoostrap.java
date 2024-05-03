package net.easycloud.loader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public final class LoaderBoostrap {

    public static void main(String[] args) {
        try {
            Path storage = Path.of(".cache");
            Path basePath = Path.of(".cache/base.jar");

            if(!Files.exists(storage)) {
                Files.createDirectory(storage);
            }

            Files.copy(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("easycloud-updater.jar")), storage.resolve("updater.jar"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("easycloud-base.jar")), basePath, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("easycloud-wrapper.jar")), storage.resolve("wrapper.jar"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("Cloud-API.jar")), storage.resolve("plugin.jar"), StandardCopyOption.REPLACE_EXISTING);

            final var classLoader = new URLClassLoader(new URL[]{basePath.toUri().toURL()}, ClassLoader.getSystemClassLoader()) {
                @Override
                public void addURL(URL url) {
                    super.addURL(url);
                }
            };
            Thread.currentThread().setContextClassLoader(classLoader);

            var arrayList = new ArrayList<>();
            arrayList.add(new java.io.File(LoaderBoostrap.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName());
            arrayList.addAll(Arrays.asList(args));

            classLoader.loadClass("net.easycloud.base.BaseBootstrap").getMethod("main", String[].class).invoke(null, (Object) arrayList.toArray(new String[0]));
        } catch (IOException | ClassNotFoundException | InvocationTargetException | IllegalAccessException |
                 NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }
}
