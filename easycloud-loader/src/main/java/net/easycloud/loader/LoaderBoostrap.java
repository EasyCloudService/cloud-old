package net.easycloud.loader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
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


            if(!(args.length >= 1 && args[0].equals("--ignore-update"))) {
                if(storage.resolve("easycloud-temp.jar").toFile().exists()) {
                    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                        public void run() {
                            try {
                                Files.copy(storage.resolve("easycloud-temp.jar"), Path.of(new File(LoaderBoostrap.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath()), StandardCopyOption.REPLACE_EXISTING);
                                //storage.resolve("easycloud-temp.jar").toFile().delete();
                            } catch (IOException | URISyntaxException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, "Shutdown-thread"));
                    System.exit(0);
                }
            }

            if(!Files.exists(storage)) {
                Files.createDirectory(storage);
            }

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

            classLoader.loadClass("net.easycloud.base.BaseBootstrap").getMethod("main", String[].class).invoke(null, (Object) args);
        } catch (IOException | ClassNotFoundException | InvocationTargetException |
                 IllegalAccessException | NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }
}
