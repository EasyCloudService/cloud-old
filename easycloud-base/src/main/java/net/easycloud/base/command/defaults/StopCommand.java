package net.easycloud.base.command.defaults;

import net.easycloud.base.command.CloudCommand;
import net.easycloud.base.command.Command;
import net.easycloud.base.Base;

@Command(name = "stop", aliases = {"shutdown"})
public final class StopCommand extends CloudCommand {

    @Override
    protected void execute(String[] args) {
        Base.getInstance().onShutdown();
    }
}
