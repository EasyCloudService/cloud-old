package net.easycloud.base;

import dev.httpmarco.osgan.networking.server.NettyServer;
import dev.httpmarco.osgan.networking.server.NettyServerBuilder;
import lombok.Getter;
import net.bytemc.evelon.Evelon;
import net.easycloud.api.configuration.SecretConfiguration;
import net.easycloud.api.utils.file.FileHelper;
import net.easycloud.api.github.GithubConfig;
import net.easycloud.api.github.GithubDownloader;
import net.easycloud.api.network.packet.HandshakeAuthenticationPacket;
import net.easycloud.api.network.packet.ServiceConnectPacket;
import net.easycloud.api.utils.RandomStringUtil;
import net.easycloud.base.command.CommandHandler;
import net.easycloud.base.console.runner.ConsoleRunner;
import net.easycloud.base.logger.SimpleLogger;
import net.easycloud.base.user.UserHandler;
import net.easycloud.base.rest.RestAPI;
import net.easycloud.base.service.Service;
import net.easycloud.base.service.SimpleServiceHandler;
import net.easycloud.base.setup.SetupHandler;
import net.easycloud.api.CloudDriver;
import net.easycloud.api.configuration.DefaultConfiguration;
import net.easycloud.api.console.LogType;
import net.easycloud.api.console.Logger;
import net.easycloud.api.velocity.VelocityProvider;
import net.easycloud.base.group.SimpleGroupHandler;

import java.io.IOException;
import java.nio.file.Path;

@SuppressWarnings("ALL")
@Getter
public final class Base extends CloudDriver {
    private static Base instance;

    private boolean running;
    private final Logger logger;
    private final DefaultConfiguration configuration;

    @Getter
    private final NettyServer nettyServer;
    private final SetupHandler setupHandler;
    private final CommandHandler commandHandler;

    public Base(String jarName, boolean ignoreUpdate) {
        instance = this;

        this.setupHandler = new SetupHandler();

        this.running = true;
        this.logger = new SimpleLogger();
        try {
            var result = false;
            logger.log("Searching for an update...");
            if (ignoreUpdate) {
                logger.log("Ignore updates.");
            } else {
                result = GithubDownloader.updateIfNeeded(CloudPath.STORAGE);
                if (result) {
                    logger.log("An update was found. restarting...");
                    logger.log("&cPlease wait 5 seconds before starting again!");
                    Thread.sleep(1000);
                    new ProcessBuilder("java", "-jar", "updater.jar", jarName).directory(CloudPath.STORAGE.toFile()).start();
                    System.exit(0);
                } else {
                    logger.log("No update was found...");
                }
            }
            Thread.sleep(1000);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

        if (!Path.of(System.getProperty("user.dir")).resolve("config.json").toFile().exists()) {
            setupHandler.start();
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (!this.setupHandler.isOnSetup()) {
                    break;
                }
            }
        }

        FileHelper.writeIfNotExists(Path.of(System.getProperty("user.dir")), new SecretConfiguration("SECRET_" + RandomStringUtil.generate(100)));
        var secret = FileHelper.read(Path.of(System.getProperty("user.dir")), SecretConfiguration.class);
        this.configuration = FileHelper.read(Path.of(System.getProperty("user.dir")), DefaultConfiguration.class);
        Evelon.setCradinates(configuration.database());

        ((SimpleLogger) Base.getInstance().getLogger()).getConsole().setInService(false);

        printScreen();

        this.groupProvider = new SimpleGroupHandler();

        Base.getInstance().getLogger().log("Netty-Server will be started...");
        this.nettyServer = new NettyServerBuilder().withPort(8897).build();
        Base.getInstance().getLogger().log("Netty-Server was startet on following port: 8897");
        this.nettyServer.listen(HandshakeAuthenticationPacket.class, (transmit, packet) -> {
            if(!packet.getKey().equals(secret.value())) {
                var service = this.serviceProvider.getServices().stream().filter(it -> it.getId().equals(packet.getName())).findFirst().orElse(null);
                transmit.channel().close();

                if(service != null) {
                    ((Service) service).stop(true);
                }
            } else {
                this.serviceProvider.getServices().forEach(service -> {
                    transmit.sendPacket(new ServiceConnectPacket(service.getGroup(), service.getId(), service.getPort()));
                });
            }
        });

        this.serviceProvider = new SimpleServiceHandler();
        this.commandHandler = new CommandHandler();
        this.velocityProvider = new VelocityProvider();
        this.userProvider = new UserHandler();
        new RestAPI();
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

    public String getVersion() {
        var version = "-";
        if(CloudPath.STORAGE.resolve("github.json").toFile().exists()) {
            version = FileHelper.read(CloudPath.STORAGE, GithubConfig.class).getVersion();
        }
        return version;
    }

    public void printScreen() {
        ((SimpleLogger) this.logger).getConsole().clearConsole();
        logger.log("""
                %LINE_BREAK%
                  &7____ ____ ____ _   _ &9____ _    ____ _  _ ___
                  &7|___ |__| [__   \\_/  &9|    |    |  | |  | |  \\
                  &7|___ |  | ___]   |   &9|___ |___ |__| |__| |__/
                  &7[&f%RELEASE%&7] Powered by &b@FlxwDNS&7, &b@1Chickxn &7and &b@Swerion
                
                """.replace("%RELEASE%", getVersion()).replace("%LINE_BREAK%", "\n".repeat(100)), LogType.EMPTY);
    }

    public static Base getInstance() {
        return (Base) instance;
    }

    @Override
    public void onShutdown() {
        running = false;
        printScreen();
        new Thread(() -> this.nettyServer.close()).start();
        this.serviceProvider.getServices().forEach(it -> ((Service) it).stop(false));
        try {
            logger.log("&7Try to delete &9tmp &7directory.");
            FileHelper.removeDirectory(Path.of("tmp"));
            logger.log("&9tmp &7directory was deleted.");
            logger.log("&9Console &7will be shutdown...");
            Thread.sleep(1000);
            logger.log("&7Good bye, see you soon!");
            ((SimpleLogger) logger).getConsole().shutdownReading();
            Runtime.getRuntime().halt(0);
            System.exit(1);
        } catch (InterruptedException exception) {
            //throw new RuntimeException(exception);
        }
    }
}
