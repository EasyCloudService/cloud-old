package net.easycloud.updater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class UpdaterBootstrap {

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1000);


        var jarPath = Path.of("../");
        var github = Path.of("github");

        jarPath.resolve("storage").resolve("jars").toFile().delete();
        for (File file : jarPath.resolve("storage").toFile().listFiles()) {
            var blackList = List.of(
                    "loader.jar", "github", "release.zip", "github.json"
            );
            System.out.println("TRY: " + file.getName());
            if(blackList.stream().noneMatch(it -> file.getName().equalsIgnoreCase(it))) {
                System.out.println("DELETE: " + file.getName());
                file.delete();
            }
        };
        List.of("start.bat", "start.sh", "EasyCloudService.jar").forEach(it -> jarPath.resolve(it).toFile().delete());

        unzip(github.resolve("release.zip"), jarPath);
        github.resolve("release.zip").toFile().delete();

        /*Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                var jar = Path.of("easycloud-temp.jar");
                Files.copy(jar, Path.of("../" + args[0]), StandardCopyOption.REPLACE_EXISTING);
                jar.toFile().delete();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, "Shutdown-thread"));
        System.exit(0);*/
    }

    private static void unzip(Path path, Path dest) {
        if (!dest.toFile().exists()) dest.toFile().mkdirs();

        byte[] buffer = new byte[1024];
        try (FileInputStream fis = new FileInputStream(path.toString());
             ZipInputStream zis = new ZipInputStream(fis)) {

            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(dest + File.separator + fileName);
                System.out.println("[DEBUG] Extract to: " + newFile.getAbsolutePath());

                if (ze.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    new File(newFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
