package net.easycloud.loader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public final class LoaderBoostrap {

    public static void main(String[] args) {
        try {
            Path storage = Path.of("storage");
            if(!Files.exists(storage)) {
                Files.createDirectory(storage);
            }

            final var classLoader = new URLClassLoader(new URL[]{Path.of("storage/base.jar").toUri().toURL()}, ClassLoader.getSystemClassLoader()) {
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
