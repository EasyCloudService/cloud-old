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
            Path basePath = Path.of("storage/base.jar");

            if(!Files.exists(storage)) {
                Files.createDirectory(storage);
            }

            /*storage.resolve("jars").toFile().mkdirs();
            Map.of(
                    "easycloud-updater.jar", storage.resolve("loader.jar"),
                    "easycloud-base.jar", basePath,
                    "easycloud-wrapper.jar", storage.resolve("wrapper.jar"),
                    "Cloud-API.jar", storage.resolve("jars").resolve("ECS-Plugin.jar")
            ).forEach((fileName, path) -> {
                var stream = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
                if(stream != null) {
                    try {
                        Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
                        //new File(ClassLoader.getSystemClassLoader().getResource(fileName).toURI()).delete();
                    } catch (IOException | URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            });*/

            /*Files.copy(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("easycloud-updater.jar")), storage.resolve("updater.jar"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("easycloud-base.jar")), basePath, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("easycloud-wrapper.jar")), storage.resolve("wrapper.jar"), StandardCopyOption.REPLACE_EXISTING);

            storage.resolve("jars").toFile().mkdirs();
            Files.copy(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("Cloud-API.jar")), storage.resolve("jars").resolve("ECS-Plugin.jar"), StandardCopyOption.REPLACE_EXISTING);
            */

            //new File(ClassLoader.getSystemClassLoader().getResource("Cloud-API.jar").toURI()).delete();

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
