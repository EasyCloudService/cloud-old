package net.easycloud.base.command.defaults;

import net.easycloud.base.command.CloudCommand;
import net.easycloud.base.command.Command;
import net.easycloud.base.Base;

@Command(name = "help", aliases = {"commands"})
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
