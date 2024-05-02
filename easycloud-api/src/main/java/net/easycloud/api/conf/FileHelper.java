package net.easycloud.api.conf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;

public final class FileHelper {
    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static String getName(Class<?> clazz) {
        var name = clazz.getSimpleName();
        if(clazz.isAnnotationPresent(FileName.class)) {
            name = clazz.getAnnotation(FileName.class).name();
        }
        return name;
    }

    public static void writeIfNotExists(Path path, Object object) {
        if(!path.resolve(getName(object.getClass()) + ".json").toFile().exists()) {
            write(path, object);
        }
    }

    public static void write(Path path, Object object) {
        var file = path.resolve(getName(object.getClass()) + ".json");
        if(file.toFile().exists()) {
            file.toFile().delete();
        }

        try (FileWriter writer = new FileWriter(file.toFile().getPath())) {
            GSON.toJson(object, writer);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static <T> T read(Path path, Class<T> clazz) {
        var file = path.resolve(getName(clazz) + ".json");
        try (Reader reader = new FileReader(file.toFile().getPath())) {
            return GSON.fromJson(reader, clazz);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @SneakyThrows
    public static void removeDirectory(Path path) {
        try (Stream<Path> pathStream = Files.walk(path)) {
            pathStream.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        //Files.deleteIfExists(path);
    }
}
