package net.easycloud.base;

import lombok.Getter;
import net.bytemc.evelon.DatabaseProtocol;
import net.bytemc.evelon.Evelon;
import net.bytemc.evelon.cradinates.DatabaseCradinates;
import net.easycloud.base.command.CommandHandler;
import net.easycloud.base.console.runner.ConsoleRunner;
import net.easycloud.base.logger.SimpleLogger;
import net.easycloud.base.permission.PermissionHandler;
import net.easycloud.base.server.BaseServer;
import net.easycloud.base.service.Service;
import net.easycloud.base.service.SimpleServiceHandler;
import net.http.aeon.Aeon;
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

    private final CommandHandler commandHandler;

    public Base() {
        instance = this;

        this.running = true;
        this.logger = new SimpleLogger();
        this.configuration = Aeon.insert(new DefaultConfiguration(new DatabaseCradinates(DatabaseProtocol.MARIADB, "127.0.0.1", "test123", "root", "cloud", 3306)), Path.of(System.getProperty("user.dir")));
        Evelon.setCradinates(configuration.database());

        this.nettyProvider = new BaseServer();
        this.commandHandler = new CommandHandler();
        this.groupProvider = new SimpleGroupHandler();
        this.serviceProvider = new SimpleServiceHandler();
        this.velocityProvider = new VelocityProvider();
        this.permissionProvider = new PermissionHandler();

        new ConsoleRunner();
        Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));

        logger.log("§7Cloud was §asuccessfully §7started.", LogType.SUCCESS);
    }

    public static Base getInstance() {
        return (Base) instance;
    }

    @Override
    public void onShutdown() {
        running = false;
        ((SimpleLogger) logger).getConsole().shutdownReading();

        this.nettyProvider.close();
        this.serviceProvider.getServices().forEach(it -> ((Service) it).stop());
        try {
            FileUtils.forceDelete(new File("tmp"));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        System.exit(0);
    }
}