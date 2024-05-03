package net.easycloud.base.setup;

import lombok.AccessLevel;
import lombok.Getter;
import net.bytemc.evelon.DatabaseProtocol;
import net.bytemc.evelon.cradinates.DatabaseCradinates;
import net.easycloud.api.conf.DefaultConfiguration;
import net.easycloud.api.conf.FileHelper;

import java.nio.file.Path;
import java.util.Arrays;
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
        onSetup = true;

        ConsoleSetup.subscribe(List.of(
                SetupBuilder.<String>get()
                        .key("database.type")
                        .question("&7What is you database type?")
                        .possibleResults(Arrays.stream(DatabaseProtocol.values()).map(Enum::name).toList())
                        .build(),
                SetupBuilder.<String>get()
                        .key("database.host")
                        .question("&7What is you database host?")
                        .build(),
                SetupBuilder.<Integer>get()
                        .key("database.port")
                        .question("&7What is you database port?")
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
            FileHelper.writeIfNotExists(Path.of(System.getProperty("user.dir")), new DefaultConfiguration(new DatabaseCradinates(
                    DatabaseProtocol.valueOf(values.get("database.type")),
                    values.get("database.host"),
                    values.get("database.password"),
                    values.get("database.user"),
                    values.get("database.name"),
                    Integer.parseInt(values.get("database.port"))
            ), "easyCloudService-" + new Random().nextInt(100000000, 999999999)));
            onSetup = false;
        });
    }
}
