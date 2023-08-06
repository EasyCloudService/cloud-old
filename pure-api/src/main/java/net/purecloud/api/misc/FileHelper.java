package net.purecloud.api.misc;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class FileHelper {

    public static void downloadFromUrl(String urlString, String name, Path path) {
        System.out.println("Download §e" + urlString + "§f...");

        try {
            URL url = new URL(urlString);
            InputStream inputStream = url.openStream();
            Path filePath = path.resolve(name);

            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            inputStream.close();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        System.out.println("Download of §e" + name + " §fwas §asuccessfully!");
    }
}
