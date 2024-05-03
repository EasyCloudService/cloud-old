package net.easycloud.api.misc;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public final class Reflections {

    public static void createPath(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    public static void copy(Path fromPath, Path toPath) {
        try {
            Files.walkFileTree(fromPath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path targetDir = toPath.resolve(fromPath.relativize(dir));
                    Files.createDirectories(targetDir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.copy(file, toPath.resolve(fromPath.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    System.err.println("Fehler beim Kopieren der Datei: " + file + " - " + exc.getMessage());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
