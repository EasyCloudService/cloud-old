package net.easycloud.base.setup;

import lombok.AccessLevel;
import lombok.Getter;
import net.bytemc.evelon.DatabaseProtocol;
import net.easycloud.api.console.LogType;
import net.easycloud.base.Base;

import java.util.Arrays;
import java.util.Collections;
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
                        .build()
        ), values -> {
            onSetup = false;
        });

/*
        Base.getInstance().getLogger().log("&7Setup was &estarted.", LogType.INFO);

        Base.getInstance().getLogger().log("&7Datbase types: &bMARIADB&7, &bMYSQL&7, &bPOSTGRESQL", LogType.INFO);
        var answer1 = queueQuestion("&7What is you database type?").toUpperCase();
        if(Arrays.stream(DatabaseProtocol.values()).noneMatch(it -> it.name().equals(answer1))) {
            Base.getInstance().getLogger().log("&cInvalid answer.", LogType.ERROR);
            System.exit(0);
        }
        var protocol = DatabaseProtocol.valueOf(answer1);
        var host = queueQuestion("&7What is you database host?");
        int port = 0;
        try {
            port = Integer.parseInt(queueQuestion("&7What is you database port?"));
        } catch (NumberFormatException e) {
            Base.getInstance().getLogger().log("&cInvalid answer.", LogType.ERROR);
            System.exit(0);
        }

        var user = queueQuestion("&7What is you database user?");
        var password = queueQuestion("&7What is you database password?");
        var database = queueQuestion("&7What is you database name?");*/
    }

    public String queueQuestion(String question) {
        Base.getInstance().getLogger().log("&f[&bQUESTION&f] &7" + question, LogType.EMPTY);
        this.answered = false;
        return input;
    }
}
