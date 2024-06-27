package net.easycloud.base.command;

import lombok.Getter;
import net.easycloud.api.console.LogType;
import net.easycloud.base.Base;
import net.easycloud.base.command.defaults.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public final class CommandHandler {
    private final List<CloudCommand> commands;

    public CommandHandler() {
        commands = new ArrayList<>();
        commands.add(new ClearCommand());
        commands.add(new GroupCommand());
        commands.add(new HelpCommand());
        commands.add(new ScreenCommand());
        commands.add(new ServiceCommand());
        commands.add(new StopCommand());

        Base.getInstance().getLogger().log("Commands was loaded successfully.");
    }

    public void addCommand(CloudCommand cloudCommand) {
        commands.add(cloudCommand);
    }

    public void runCommand(String command, String[] args) {
        commands.stream().filter(it -> {
            if (it.getName().equalsIgnoreCase(command)) {
                return true;
            } else {
                return Arrays.stream(it.getAliases()).anyMatch(it2 -> Arrays.stream(it2.split(", ")).anyMatch(it3 -> it3.equalsIgnoreCase(command)));
            }
        }).findFirst().ifPresentOrElse(it -> it.execute(args), () -> {
            Base.getInstance().getLogger().log("Command: " + command + " does not exists!", LogType.ERROR);
        });
    }
}
