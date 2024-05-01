package net.easycloud.spigot.inventory;

import dev.dbassett.skullcreator.SkullCreator;
import net.kyori.adventure.text.Component;
import net.easycloud.api.CloudDriver;
import net.easycloud.api.service.IService;
import net.easycloud.spigot.SpigotHeads;
import net.easycloud.spigot.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public final class ServicesInventory {

    public ServicesInventory(Player player) {
        var inventory = Bukkit.createInventory(null, 6 * 9, Component.text("§bCloud §8» §cServices"));

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("§7 ").toStack());
        }
        List.of(9, 17, 18, 26, 27, 35, 36, 44, 45, 53).forEach(it -> inventory.setItem(it, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("§7 ").toStack()));


        for (IService service : CloudDriver.getInstance().getServiceProvider().getServices()) {
            //Material.getMaterial(group.getMaterial())
            inventory.addItem(new ItemBuilder(SkullCreator.itemFromBase64(SpigotHeads.valueOf(String.valueOf(service.getGroup().getName().charAt(0)).toUpperCase()).getUrl())).name("§8» §e" + service.getId()).lore(List.of(
                    "§7",
                    "§8» §7Linksklick §8» §aBetreten",
                    "§8» §7Rechtsklick §8» §cBeenden",
                    "§7"//,
                    //"§8» §7Shift §8+ §7Linksklick §8» §7Edit group icon"
            )).toStack());
        }

        for (int i = 0; i < 6 * 9; i++) {
            if(inventory.getItem(i) == null) {
                inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("§7 ").toStack());
            }
        }
        inventory.setItem(38, new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2U0MWM2MDU3MmM1MzNlOTNjYTQyMTIyODkyOWU1NGQ2Yzg1NjUyOTQ1OTI0OWMyNWMzMmJhMzNhMWIxNTE3In19fQ==")).name("§8» §cGruppe starten").toStack());
        inventory.setItem(42, new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2M5YTEzODYzOGZlZGI1MzRkNzk5Mjg4NzZiYWJhMjYxYzdhNjRiYTc5YzQyNGRjYmFmY2M5YmFjNzAxMGI4In19fQ==")).name("§8» §cGruppen verwalten").toStack());

        player.openInventory(inventory);
    }
}
