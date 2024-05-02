package net.easycloud.base;

import lombok.Getter;
import net.bytemc.evelon.DatabaseProtocol;
import net.bytemc.evelon.Evelon;
import net.bytemc.evelon.cradinates.DatabaseCradinates;
import net.easycloud.api.conf.FileHelper;
import net.easycloud.api.github.GithubDownloader;
import net.easycloud.base.command.CommandHandler;
import net.easycloud.base.console.runner.ConsoleRunner;
import net.easycloud.base.logger.SimpleLogger;
import net.easycloud.base.permission.PermissionHandler;
import net.easycloud.base.rest.RestAPI;
import net.easycloud.base.server.BaseServer;
import net.easycloud.base.service.Service;
import net.easycloud.base.service.SimpleServiceHandler;
import net.easycloud.base.setup.SetupHandler;
import net.easycloud.api.CloudDriver;
import net.easycloud.api.conf.DefaultConfiguration;
import net.easycloud.api.console.LogType;
import net.easycloud.api.console.Logger;
import net.easycloud.api.velocity.VelocityProvider;
import net.easycloud.base.group.SimpleGroupHandler;

import java.nio.file.Path;

@Getter
public final class Base extends CloudDriver {
    private static Base instance;

    private boolean running;
    private final Logger logger;
    private final DefaultConfiguration configuration;

    private final SetupHandler setupHandler;
    private final CommandHandler commandHandler;

    public Base() {
        instance = this;

        this.setupHandler = new SetupHandler();

        this.running = true;
        this.logger = new SimpleLogger();
        try {
            logger.log("Searching for an update...");
            var result = GithubDownloader.updateIfNeeded(CloudPath.STORAGE);
            if(result) {
                logger.log("An update was found. restarting...");
            } else {
                logger.log("No update was found...");
            }
            Thread.sleep(2000);
            if(result) System.exit(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(!Path.of(System.getProperty("user.dir")).resolve("config.json").toFile().exists()) {
            setupHandler.start();
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(!this.setupHandler.isOnSetup()) {
                    break;
                }
            }
        }

        this.configuration = FileHelper.read(Path.of(System.getProperty("user.dir")), DefaultConfiguration.class);
        Evelon.setCradinates(configuration.database());

        ((SimpleLogger) this.logger).getConsole().clearConsole();
        logger.log("""
                \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n
                  &7____ ____ ____ _   _ &9____ _    ____ _  _ ___ 
                  &7|___ |__| [__   \\_/  &9|    |    |  | |  | |  \\ 
                  &7|___ |  | ___]   |   &9|___ |___ |__| |__| |__/
                  &7[&f%RELEASE%&7] Powered by &b@FlxwDNS&7, &b@1Chickxn &7and &b@Swerion
                  
                """.replace("%RELEASE%", GithubDownloader.getLatest().split(";")[0]), LogType.EMPTY);

        this.groupProvider = new SimpleGroupHandler();
        this.nettyProvider = new BaseServer();
        this.commandHandler = new CommandHandler();
        this.serviceProvider = new SimpleServiceHandler();
        this.velocityProvider = new VelocityProvider();
        this.permissionProvider = new PermissionHandler();
        new RestAPI();
        logger.log("§7RestAPI is listening on following port: 4567");
        logger.log("§7Cloud was connected to all services.");

        new ConsoleRunner();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            onShutdown();
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public static Base getInstance() {
        return (Base) instance;
    }

    @Override
    public void onShutdown() {
        running = false;
        ((SimpleLogger) logger).getConsole().shutdownReading();

        new Thread(() -> this.nettyProvider.close()).start();
        this.serviceProvider.getServices().forEach(it -> ((Service) it).stop());
        try {
            FileHelper.removeDirectory(Path.of("tmp"));
            Thread.sleep(1000);
            System.exit(0);
        } catch (InterruptedException exception) {
            //throw new RuntimeException(exception);
        }
    }
}
