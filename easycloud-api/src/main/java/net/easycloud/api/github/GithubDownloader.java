package net.easycloud.api.github;

import lombok.SneakyThrows;
import net.easycloud.api.conf.FileHelper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class GithubDownloader {

    public static boolean updateIfNeeded(Path path) {
        String latest = getLatest();
        String version = latest.split(";")[0];
        String download = latest.split(";")[1];

        FileHelper.writeIfNotExists(path, new GithubConfig("1"));
        if(FileHelper.read(path, GithubConfig.class).getVersion().equals(version)) {
            return false;
        }
        try {
            downloadRelease(download, path.resolve("easycloud-temp.jar"));
            FileHelper.write(path, new GithubConfig(version));
            return true;
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
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
            System.out.println("HTTP error: " + responseCode);
        }

        return name + ";" + download;
    }

    private static void downloadRelease(String url, Path path) throws IOException {
        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static String convertStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
}
