package net.purecloud.base.command.defaults;

import net.purecloud.base.Base;
import net.purecloud.base.command.CloudCommand;
import net.purecloud.base.command.Command;

@Command(name = "help", description = "", aliases = {"commands"})
public final class HelpCommand extends CloudCommand {

    @Override
    protected void execute(String[] args) {
        Base.getInstance().getLogger().log("");
        Base.getInstance().getCommandHandler().getCommands().forEach(command -> {
            StringBuilder builder = new StringBuilder();
            builder.append("§7[§f").append(command.getName());
            for (String alias : command.getAliases()) {
                builder.append("§7, §f").append(alias);
            }
            builder.append("§7]");

            Base.getInstance().getLogger().log(builder + " §7- §f" + command.getDescription());
        });
        Base.getInstance().getLogger().log("");
    }
}
