package net.easycloud.api.conf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.*;

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
        var file = path.resolve(getName(object.getClass()) + ".json");
        if(file.toFile().exists()) {
            return;
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
}
