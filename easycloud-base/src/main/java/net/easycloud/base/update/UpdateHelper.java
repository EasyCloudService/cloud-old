package net.easycloud.base.update;

import lombok.SneakyThrows;
import net.easycloud.api.console.LogType;
import net.easycloud.api.console.Logger;
import net.easycloud.api.github.GithubDownloader;
import net.easycloud.base.CloudPath;

public final class UpdateHelper {

    @SneakyThrows
    public static void update(boolean isDisabled, String jarName, Logger logger) {
        logger.log("""
                %LINE_BREAK%
                &7[&cGithub&7] &7Searching for an newer release...""".replace("%LINE_BREAK%", "\n".repeat(100)));
        CloudPath.STORAGE.resolve("tmp").toFile().mkdirs();

        if (GithubDownloader.isUpdateReady(CloudPath.STORAGE.resolve("tmp"))) {
            logger.log("[&cGithub&7] &7Following version was found: &7[&f" + GithubDownloader.getLatest() + "&7]", LogType.EMPTY);
            if (isDisabled) {
                logger.log("[&cGithub&7] &7But updates are disabled. Cause &7[&f--ignore-update&7]", LogType.EMPTY);
                Thread.sleep(500);
                return;
            }

            int[] sProgress = {0};
            var status = GithubDownloader.updateIfNeeded(CloudPath.STORAGE.resolve("github"), progress -> {
                if (sProgress[0] + 10 > progress) {
                    return;
                }
                sProgress[0] = progress;
                logger.log("\r&9STATUS: &f[&7" + "=".repeat(progress / 5) + " ".repeat(20 - (progress / 5)) + "&f] &7" + progress + "%", LogType.EMPTY);
            });
            if (!status) {
                logger.log("&cNo update was found...", LogType.ERROR);
            } else {
                logger.log("&cTry to shutdown...");
                logger.log("&cYou have to restart in 10seconds.");

                Thread.sleep(1000);

                new ProcessBuilder("java", "-jar", "loader.jar", jarName).directory(CloudPath.STORAGE.toFile()).start();
                System.exit(0);
            }
        }
        Thread.sleep(1000);
    }
}
