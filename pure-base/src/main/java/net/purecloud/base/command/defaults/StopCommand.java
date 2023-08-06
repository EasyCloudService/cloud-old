package net.purecloud.base.command.defaults;

import net.purecloud.base.Base;
import net.purecloud.base.command.CloudCommand;
import net.purecloud.base.command.Command;

@Command(name = "stop", description = "", aliases = {"shutdown"})
public final class StopCommand extends CloudCommand {

    @Override
    protected void execute(String[] args) {
        Base.getInstance().onShutdown();
    }
}
