package net.easycloud;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class UUIDFetcher {
    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/";
    private static final Pattern UUID_PATTERN = Pattern.compile("\"id\"\\s*:\\s*\"(.*?)\"");

    private UUIDFetcher() {
        throw new UnsupportedOperationException();
    }

    public static UUID getUUID(String name) {
        String output = callURL(UUID_URL + name);
        Matcher m = UUID_PATTERN.matcher(output);
        if (m.find()) {
            return UUID.fromString(insertDashes(m.group(1)));
        }
        return null;
    }

    public static String insertDashes(String uuid) {
        StringBuilder sb = new StringBuilder(uuid);
        sb.insert(8, '-');
        sb.insert(13, '-');
        sb.insert(18, '-');
        sb.insert(23, '-');
        return sb.toString();
    }

    private static String callURL(String urlStr) {
        StringBuilder sb = new StringBuilder();
        URLConnection conn;
        BufferedReader br = null;
        InputStreamReader in = null;
        try {
            conn = new URL(urlStr).openConnection();
            if (conn != null) {
                conn.setReadTimeout(60 * 1000);
            }
            if (conn != null && conn.getInputStream() != null) {
                in = new InputStreamReader(conn.getInputStream(), "UTF-8");
                br = new BufferedReader(in);
                String line = br.readLine();
                while (line != null) {
                    sb.append(line).append("\n");
                    line = br.readLine();
                }
            }
        } catch (Throwable ignored) {
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Throwable ignored) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable ignored) {
                }
            }
        }
        return sb.toString();
    }
}