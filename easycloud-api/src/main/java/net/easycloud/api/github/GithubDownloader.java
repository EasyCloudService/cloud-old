package net.easycloud.api.github;

import lombok.SneakyThrows;
import net.easycloud.api.utils.file.FileHelper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.function.Consumer;

public class GithubDownloader {

    public static boolean updateIfNeeded(Path path, Consumer<Integer> progress) {
        String latest = getLatest();
        String version = latest.split(";")[0];
        String download = latest.split(";")[1];

        if (!isUpdateReady(path)) {
            return false;
        }
        try {
            downloadRelease(download, path.resolve("release.zip"), progress);
            FileHelper.write(path, new GithubConfig(version));
            return true;
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }

    public static boolean isUpdateReady(Path path) {
        FileHelper.writeIfNotExists(path, new GithubConfig("1"));
        return !FileHelper.read(path, GithubConfig.class).getVersion().equals(getLatest().split(";")[0]);
    }

    @SneakyThrows
    public static String getLatest() {
        String urlString = "https://api.github.com/repos/EasyCloudService/cloud/releases/latest";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        String name = "";
        String download = "";

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            String jsonResponse = convertStreamToString(inputStream);
            inputStream.close();

            int startIndex = jsonResponse.indexOf("\"browser_download_url\":") + 24;
            int endIndex = jsonResponse.indexOf("\"", startIndex);
            download = jsonResponse.substring(startIndex, endIndex);

            startIndex = jsonResponse.indexOf("\"name\":") + 8;
            endIndex = jsonResponse.indexOf("\"", startIndex);
            name = jsonResponse.substring(startIndex, endIndex);
        } else {
            System.out.println("Ratelimit exceed. (" + responseCode + ")");
            return "null;0";
        }

        return name + ";" + download;
    }

    private static void downloadRelease(String url, Path path, Consumer<Integer> progress) throws IOException {
        URL website = new URL(url);
        try (InputStream in = website.openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            long fileSize = website.openConnection().getContentLengthLong();
            long totalBytesRead = 0;

            while (totalBytesRead < fileSize) {
                int bytesRead = rbc.read(buffer);
                if (bytesRead == -1) break;

                totalBytesRead += bytesRead;

                buffer.flip();
                fileChannel.write(buffer);
                buffer.clear();

                progress.accept((int) ((double) totalBytesRead / fileSize * 100));
            }
        }
    }

    private static String convertStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
}
