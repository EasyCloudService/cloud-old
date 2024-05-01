package net.easycloud.spigot.inventory;

import dev.dbassett.skullcreator.SkullCreator;
import net.kyori.adventure.text.Component;
import net.easycloud.api.CloudDriver;
import net.easycloud.api.group.Group;
import net.easycloud.spigot.SpigotHeads;
import net.easycloud.spigot.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public final class ServiceStartInventory {

    public ServiceStartInventory(Player player) {
        var inventory = Bukkit.createInventory(null, 6 * 9, Component.text("§bCloud §8» §cService start"));

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("§7 ").toStack());
        }
        List.of(9, 17, 18, 26, 27, 35, 36, 44, 45, 53).forEach(it -> inventory.setItem(it, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("§7 ").toStack()));


        for (Group group : CloudDriver.getInstance().getGroupProvider().getRepository().findAll()) {
            var count = (int) CloudDriver.getInstance().getServiceProvider().getServices().stream().filter(it -> it.getGroup().getName().equals(group.getName())).count();
            inventory.addItem(new ItemBuilder(SkullCreator.itemFromBase64(SpigotHeads.valueOf(String.valueOf(group.getName().charAt(0)).toUpperCase()).getUrl())).name("§8» §c" + group.getName()).amount((count == 0 ? 1 : count)).toStack());
        }

        for (int i = 0; i < 6 * 9; i++) {
            if(inventory.getItem(i) == null) {
                inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("§7 ").toStack());
            }
        }
        inventory.setItem(40, new ItemBuilder(Material.FEATHER).name("§8» §cZurück").toStack());

        player.openInventory(inventory);
    }
}
