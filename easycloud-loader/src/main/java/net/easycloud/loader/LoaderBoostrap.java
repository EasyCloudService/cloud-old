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
            Path storage = Path.of(".cache");
            Path basePath = Path.of(".cache/base.jar");

            if(!Files.exists(storage)) {
                Files.createDirectory(storage);
            }

            Files.copy(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("easycloud-updater.jar")), storage.resolve("updater.jar"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("easycloud-base.jar")), basePath, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("easycloud-wrapper.jar")), storage.resolve("wrapper.jar"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("Cloud-API.jar")), storage.resolve("plugin.jar"), StandardCopyOption.REPLACE_EXISTING);

            if(!(args.length >= 1 && args[0].equals("--ignore-update"))) {
                if(storage.resolve("easycloud-temp.jar").toFile().exists()) {
                    for (int i = 0; i < 999; i++) {
                        System.out.println();
                    }
                    System.out.println("PRE: Found update. Installing... Please restart jar in 5 seconds.");
                    Thread.sleep(2000);

                    var name = new java.io.File(LoaderBoostrap.class.getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .getPath())
                            .getName();
                    new ProcessBuilder("java", "-jar", "updater.jar", name).directory(storage.toFile()).start();
                    System.exit(0);
                }
            }

            final var classLoader = new URLClassLoader(new URL[]{basePath.toUri().toURL()}, ClassLoader.getSystemClassLoader()) {
                @Override
                public void addURL(URL url) {
                    super.addURL(url);
                }
            };
            Thread.currentThread().setContextClassLoader(classLoader);

            classLoader.loadClass("net.easycloud.base.BaseBootstrap").getMethod("main", String[].class).invoke(null, (Object) args);
        } catch (IOException | ClassNotFoundException | InvocationTargetException | IllegalAccessException |
                 NoSuchMethodException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }
}
