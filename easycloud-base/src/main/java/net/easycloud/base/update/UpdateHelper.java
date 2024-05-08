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
                &9        __   __       ___  ___  __
                &9 |    ||__) |  \\  /\\   |  |__  |__)
                &9  \\__/ |    |__/ /--\\  |  |___ |  \\
                &7[&9Github&7] &7Searching for an newer release...""".replace("%LINE_BREAK%", "\n".repeat(100)));

        if(GithubDownloader.isUpdateReady(CloudPath.STORAGE)) {
            logger.log("[&9Github&7] &7An newer release was found.", LogType.EMPTY);

            if(isDisabled) {
                logger.log("[&9Github&7] &7But sorry. Updates are &cdisabled&7.", LogType.EMPTY);
                Thread.sleep(1000);
                return;
            }

            int[] sProgress = {0};
            var status = GithubDownloader.updateIfNeeded(CloudPath.STORAGE, progress -> {
                if(sProgress[0] + 10 > progress) {
                    return;
                }
                sProgress[0] = progress;
                logger.log("\r&9STATUS: &f[&7" + "=".repeat(progress / 5) + " ".repeat(50 - (progress / 5)) + "&f] &7" + progress + "%", LogType.EMPTY);
            });
            if(!status) {
                logger.log("&cNo update was found...", LogType.ERROR);
                Thread.sleep(1000);
                return;
            }

            logger.log("&cRESTARTING...");
            logger.log("&cPlease wait 5 seconds before starting again!");

            Thread.sleep(1000);
            new ProcessBuilder("java", "-jar", "updater.jar", jarName).directory(CloudPath.STORAGE.toFile()).start();
            System.exit(0);
        }
        Thread.sleep(1000);
    }
}
