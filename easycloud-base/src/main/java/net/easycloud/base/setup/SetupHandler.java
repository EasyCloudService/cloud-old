package net.easycloud.base.setup;

import lombok.AccessLevel;
import lombok.Getter;
import net.bytemc.evelon.DatabaseProtocol;
import net.bytemc.evelon.cradinates.DatabaseCradinates;
import net.easycloud.api.conf.DefaultConfiguration;
import net.easycloud.api.conf.FileHelper;
import net.easycloud.api.group.Group;
import net.easycloud.api.group.misc.GroupType;
import net.easycloud.api.group.misc.GroupVersion;
import net.easycloud.base.Base;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

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
                        .build(),
                SetupBuilder.<Boolean>get()
                        .key("service.create.proxy")
                        .question("&7Dou you want to create a proxy?")
                        .possibleResults(List.of(true, false))
                        .build(),
                SetupBuilder.<Boolean>get()
                        .key("service.create.lobby")
                        .question("&7Dou you want to create a lobby? (1.20.4)")
                        .possibleResults(List.of(true, false))
                        .build()
        ), values -> {
            FileHelper.writeIfNotExists(Path.of(System.getProperty("user.dir")), new DefaultConfiguration(new DatabaseCradinates(
                    DatabaseProtocol.valueOf(values.get("database.type")),
                    values.get("database.host"),
                    values.get("database.password"),
                    values.get("database.user"),
                    values.get("database.name"),
                    Integer.valueOf(values.get("database.port"))
            )));

            if(Boolean.parseBoolean(values.get("service.create.proxy"))) {
                Base.getInstance().getGroupProvider().create(new Group("Proxy", 512, 1, 1, 50, "ANVIL", GroupType.PROXY, GroupVersion.VELOCITY_LATEST));
            }
            if(Boolean.parseBoolean(values.get("service.create.proxy"))) {
                Base.getInstance().getGroupProvider().create(new Group("Lobby", 1024, 1, 1, 50, "ANVIL", GroupType.SERVER, GroupVersion.PAPER_1_20_4));
            }
            onSetup = false;
        });
    }
}
