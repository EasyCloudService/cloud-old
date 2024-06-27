package net.easycloud.base.command.defaults;

import net.easycloud.base.Base;
import net.easycloud.base.command.CloudCommand;
import net.easycloud.base.command.Command;

@Command(name = "clear", aliases = {"clearscreen"})
public final class ClearCommand extends CloudCommand {

    @Override
    protected void execute(String[] args) {
        Base.getInstance().printScreen();
    }
}
