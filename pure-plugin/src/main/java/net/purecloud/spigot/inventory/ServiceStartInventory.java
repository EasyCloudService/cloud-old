package net.purecloud.spigot.inventory;

import net.kyori.adventure.text.Component;
import net.purecloud.api.CloudDriver;
import net.purecloud.api.group.Group;
import net.purecloud.api.service.IService;
import net.purecloud.spigot.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public final class ServiceStartInventory {

    public ServiceStartInventory(Player player) {
        var inventory = Bukkit.createInventory(null, 6 * 9, Component.text("§bCloud §8» §eService start"));

        for (Group group : CloudDriver.getInstance().getGroupProvider().getRepository().findAll()) {
            inventory.addItem(new ItemBuilder(Material.getMaterial(group.getMaterial())).name("§8» §e" + group.getName()).lore(List.of(
                    "§7",
                    "§7Left click §8» §aStart service"
            )).toStack());
        }

        for (int i = 0; i < 9; i++) {
            inventory.setItem(45 + i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("§7 ").toStack());
        }
        inventory.setItem(49, new ItemBuilder(Material.FEATHER).name("§8» §eBack").toStack());

        player.openInventory(inventory);
    }
}
