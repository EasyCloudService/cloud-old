package net.easycloud.base.setup;

import lombok.AccessLevel;
import lombok.Getter;
import net.easycloud.api.configuration.DefaultConfiguration;
import net.easycloud.api.configuration.hikari.HikariConfiguration;
import net.easycloud.api.utils.file.FileHelper;

import java.nio.file.Path;
import java.util.List;
import java.util.Random;

@Getter
public final class SetupHandler {
    private boolean onSetup;

    @Getter(AccessLevel.NONE)
    private boolean answered;
    @Getter(AccessLevel.NONE)
    private String input;

    public SetupHandler() {
        this.onSetup = false;
    }

    public void start() {
        // onSetup = true;

       /* ConsoleSetup.subscribe(List.of(
                SetupBuilder.<String>get()
                        .key("database.host")
                        .question("&7What is you database host?")
                        .build(),
                SetupBuilder.<Integer>get()
                        .key("database.port")
                        .question("&7What is you database port? (default: 3306)")
                        .build(),
                SetupBuilder.<String>get()
                        .key("database.name")
                        .question("&7What is you database name?")
                        .build(),
                SetupBuilder.<String>get()
                        .key("database.user")
                        .question("&7What is you database user?")
                        .build(),
                SetupBuilder.<String>get()
                        .key("database.password")
                        .question("&7What is you database password?")
                        .build()
        ), values -> {
            FileHelper.writeAsList(Path.of(System.getProperty("user.dir")), new HikariConfiguration(
                    values.get("database.host"),
                    values.get("database.name"),
                    values.get("database.user"),
                    values.get("database.password"),
                    Integer.parseInt(values.get("database.port"))
            ));*/

        FileHelper.writeAsList(Path.of(System.getProperty("user.dir")), new HikariConfiguration());
        var configs = Path.of(System.getProperty("user.dir")).resolve("storage").resolve("data");
        configs.toFile().mkdirs();

        FileHelper.writeIfNotExists(configs, new DefaultConfiguration("easyCloudService-" + new Random().nextInt(100000000, 999999999)));

        Path.of(System.getProperty("user.dir")).resolve("storage").resolve("h2").toFile().mkdirs();
        //onSetup = false;
        // });
    }
}
