package net.easycloud.base;

import lombok.Getter;
import net.bytemc.evelon.DatabaseProtocol;
import net.bytemc.evelon.Evelon;
import net.bytemc.evelon.cradinates.DatabaseCradinates;
import net.easycloud.api.conf.FileHelper;
import net.easycloud.base.command.CommandHandler;
import net.easycloud.base.console.runner.ConsoleRunner;
import net.easycloud.base.logger.SimpleLogger;
import net.easycloud.base.permission.PermissionHandler;
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
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
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
        if(!Path.of(System.getProperty("user.dir")).resolve("config.ae").toFile().exists()) {
            setupHandler.start();
            while (true) {
                try {
                    Thread.sleep(1000);
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

        logger.log("""
                
                
                &r  ______                 &b    _____ _                _
                &r |  ____|                &b  / ____| |               | |
                &r | |__   __ _ ___ _   _  &b | |    | | ___  _   _  __| |
                &r |  __| / _` / __| | | | &b | |    | |/ _ \\| | | |/ _` |
                &r | |___| (_| \\__ \\ |_| |  &b| |____| | (_) | |_| | (_| |
                &r |______\\__,_|___/\\__, |  &b \\_____|_|\\___/ \\__,_|\\__,_|
                &r ------------------__/ |------------------------------
                &r                  |___/
                
                 &r| EasyCloud - Powered by &b@AscanAPI &7and &b@Vynl
                """, LogType.EMPTY);

        this.groupProvider = new SimpleGroupHandler();
        this.nettyProvider = new BaseServer();
        this.commandHandler = new CommandHandler();
        this.serviceProvider = new SimpleServiceHandler();
        this.velocityProvider = new VelocityProvider();
        this.permissionProvider = new PermissionHandler();

        new ConsoleRunner();
        Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));

        new Thread(() -> {
            try {
                Thread.sleep(500);
                logger.log("§7Cloud was §asuccessfully §7started.", LogType.SUCCESS);
                logger.log("", LogType.EMPTY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
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
            FileUtils.forceDelete(new File("tmp"));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        System.exit(0);
    }
}
