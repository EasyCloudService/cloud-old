package net.easycloud.spigot.inventory;

import de.flxwdev.ascan.inventory.MultiInventory;
import de.flxwdev.ascan.inventory.item.ClickableItem;
import de.flxwdev.ascan.inventory.item.ItemBuilder;
import dev.dbassett.skullcreator.SkullCreator;
import net.easycloud.api.CloudDriver;
import net.easycloud.api.service.IService;
import net.easycloud.spigot.inventory.group.GroupListInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class ControlPanelInventory extends MultiInventory<IService> {

    public ControlPanelInventory(Player player) {
        super(player, "§cEasyCloud", 5, false, CloudDriver.getInstance().getServiceProvider().getServices());

        setPlaceHolder(2, 0);
        setPlaceHolder(2, 8);
        setPlaceHolder(3, 0);
        setPlaceHolder(3, 8);
        setPlaceHolder(4, 0);
        setPlaceHolder(4, 8);

        setPlaceHolder(1);
        setPlaceHolder(5);

        setClickableItem(5,4, new ClickableItem(ItemBuilder.of(Material.HOPPER).withName("§8» §7Group"), () -> {
            new GroupListInventory(player);
        }));

        open(player);
    }

    @Override
    public ClickableItem constructItem(IService service) {
        return new ClickableItem(ItemBuilder.of(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjZlZTAzYmE4NTBlYmJhMjE3MjFjYjMzN2Y3ZWRlYWI5YjBmYTYxNWE4NjJjNjg3MGNjOWM0ZDA1ZDRkMzJmYSJ9fX0=")).withName("§8» §e" + service.getId()), () -> {
            new ServiceManageInventory(getPlayer(), service);
        });
    }

    /*public ControlPanelInventory(Player player) {
        super(play);
        var inventory = Bukkit.createInventory(null, 3 * 9, Component.text("§bCloud §8» §cControlPanel"));

        for (int i = 0; i < 3 * 9; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY).name("§7").toStack());
        }

        inventory.setItem(11, new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2U0MWM2MDU3MmM1MzNlOTNjYTQyMTIyODkyOWU1NGQ2Yzg1NjUyOTQ1OTI0OWMyNWMzMmJhMzNhMWIxNTE3In19fQ==")).name("§8» §cServer verwalten").toStack());
        inventory.setItem(13, new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBjMWI1ODRmMTM5ODdiNDY2MTM5Mjg1YjJmM2YyOGRmNjc4NzEyM2QwYjMyMjgzZDg3OTRlMzM3NGUyMyJ9fX0=")).name("§8» §cBerechtigungen verwalten").toStack());
        inventory.setItem(15, new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJlOTgzZWM0NzgwMjRlYzZmZDA0NmZjZGZhNDg0MjY3NjkzOTU1MWI0NzM1MDQ0N2M3N2MxM2FmMThlNmYifX19")).name("§8» §cCloud verwalten").toStack());

        player.openInventory(inventory);
    }*/
}
