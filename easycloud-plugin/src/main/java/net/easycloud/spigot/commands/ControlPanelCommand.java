package net.easycloud.spigot.commands;

import net.kyori.adventure.text.Component;
import net.easycloud.spigot.inventory.ControlPanelInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class ControlPanelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("cloud.control")) {
                new ControlPanelInventory(player);
                return false;
            }
            player.sendMessage(Component.text("§bEasyCloud §8» §cYou don't have permission for that§8!"));
            return false;
        }
        sender.sendMessage(Component.text("§bEasyCloud §8» §cYou must be a Player§8!"));
        return false;
    }
}
