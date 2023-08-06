package net.purecloud.base;

import de.flxwdns.oraculusdb.sql.DatabaseCredentials;
import de.flxwdns.oraculusdb.sql.query.ConnectionQueryHelper;
import lombok.Getter;
import net.http.aeon.Aeon;
import net.purecloud.api.CloudDriver;
import net.purecloud.api.conf.DefaultConfiguration;
import net.purecloud.api.console.LogType;
import net.purecloud.api.console.Logger;
import net.purecloud.api.velocity.VelocityProvider;
import net.purecloud.base.command.CommandHandler;
import net.purecloud.base.console.runner.ConsoleRunner;
import net.purecloud.base.group.SimpleGroupHandler;
import net.purecloud.base.logger.SimpleLogger;
import net.purecloud.base.server.BaseServer;
import net.purecloud.base.service.Service;
import net.purecloud.base.service.SimpleServiceHandler;
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
        this.configuration = Aeon.insert(new DefaultConfiguration(new DatabaseCredentials("127.0.0.1", 3306, "cloud", "root", "test123")), Path.of(System.getProperty("user.dir")));
        ConnectionQueryHelper.init(configuration.database());

        this.nettyProvider = new BaseServer();
        this.commandHandler = new CommandHandler();
        this.groupProvider = new SimpleGroupHandler();
        this.serviceProvider = new SimpleServiceHandler();
        this.velocityProvider = new VelocityProvider();

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
