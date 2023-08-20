package net.purecloud.wrapper.classloader;

import net.purecloud.api.group.misc.GroupType;
import net.purecloud.wrapper.service.Service;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public final class ApplicationExternalObjectLoader {

    public static Thread init(Service service, Instrumentation instrumentation) {
        try {
            var applicationFile = Path.of("Server.jar").toAbsolutePath();
            var classLoader = ClassLoader.getSystemClassLoader();
            boolean preLoadClasses;
            var main = "";

            try (final var jarFile = new JarFile(applicationFile.toFile())) {
                main = jarFile.getManifest().getMainAttributes().getValue("Main-Class");
                preLoadClasses = jarFile.getEntry("META-INF/versions.list") != null;
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }

            if (preLoadClasses) {
                classLoader = new ApplicationExternalClassLoader(applicationFile.toUri().toURL());
                try (final var jarInputStream = new JarInputStream(Files.newInputStream(applicationFile))) {
                    JarEntry jarEntry;
                    while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
                        if (jarEntry.getName().endsWith(".class")) {
                            Class.forName(jarEntry.getName().replace('/', '.').replace(".class", ""), false, classLoader);
                        }
                    }
                }
            }

            instrumentation.appendToSystemClassLoaderSearch(new JarFile(applicationFile.toFile()));
            var mainClass = Class.forName(main, true, classLoader);

            return getThread(service, mainClass, classLoader);
        } catch (ClassNotFoundException | IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @NotNull
    private static Thread getThread(Service service, Class<?> mainClass, ClassLoader classLoader) {
        var thread = new Thread(() -> {
            try {
                if(service.getGroup().getType().equals(GroupType.PROXY)) {
                    mainClass.getMethod("main", String[].class).invoke(null, (Object) new String[]{"--port=" + service.getPort()});
                } else {
                    mainClass.getMethod("main", String[].class).invoke(null, (Object) new String[]{"--port=" + service.getPort(), "nogui"});
                }
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }, "Service-Thread");
        thread.setContextClassLoader(classLoader);
        thread.start();
        return thread;
    }
}
