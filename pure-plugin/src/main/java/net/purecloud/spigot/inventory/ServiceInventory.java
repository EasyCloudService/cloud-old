package net.purecloud.spigot.inventory;

import net.kyori.adventure.text.Component;
import net.purecloud.api.CloudDriver;
import net.purecloud.api.service.IService;
import net.purecloud.spigot.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public final class ServiceInventory {

    public ServiceInventory(Player player) {
        var inventory = Bukkit.createInventory(null, 6 * 9, Component.text("§bCloud §8» §eServices"));

        for (IService service : CloudDriver.getInstance().getServiceProvider().getServices()) {
            inventory.addItem(new ItemBuilder(Material.getMaterial(service.getGroup().getMaterial())).name("§8» §e" + service.getId()).lore(List.of(
                    "§7",
                    "§7Left click §8» §aJoin service",
                    "§7Right click §8» §cStop service",
                    "§7",
                    "§7Shift §8+ §7Left click §8» §7Edit group icon"
            )).toStack());
        }

        for (int i = 0; i < 9; i++) {
            inventory.setItem(45 + i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("§7 ").toStack());
        }
        inventory.setItem(45, new ItemBuilder(Material.SUNFLOWER).name("§8» §aStart template").toStack());
        inventory.setItem(49, new ItemBuilder(Material.FEATHER).name("§8» §eReload page").toStack());
        inventory.setItem(53, new ItemBuilder(Material.BARRIER).name("§8» §c-").toStack());

        player.openInventory(inventory);
    }
}
