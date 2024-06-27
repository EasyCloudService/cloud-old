package net.easycloud.base.command;

import lombok.Getter;

@Getter
public abstract class CloudCommand {
    private final String name;
    private final String description;
    private final String[] aliases;

    protected CloudCommand() {
        var annotation = getClass().getAnnotation(Command.class);

        this.name = annotation.name();
        this.description = annotation.description();
        this.aliases = annotation.aliases();
    }

    protected abstract void execute(String[] args);
}
