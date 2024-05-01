package net.easycloud.spigot.inventory.group;

import de.flxwdev.ascan.inventory.MultiInventory;
import de.flxwdev.ascan.inventory.item.ClickableItem;
import de.flxwdev.ascan.inventory.item.ItemBuilder;
import dev.dbassett.skullcreator.SkullCreator;
import net.easycloud.api.CloudDriver;
import net.easycloud.api.group.Group;
import net.easycloud.api.service.IService;
import net.easycloud.spigot.inventory.ControlPanelInventory;
import net.easycloud.spigot.inventory.ServiceManageInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class GroupListInventory extends MultiInventory<Group> {

    public GroupListInventory(Player player) {
        super(player, "§cEasyCloud", 5, false, CloudDriver.getInstance().getGroupProvider().getRepository().query().database().findAll());

        setPlaceHolder(2, 1);
        setPlaceHolder(2, 8);
        setPlaceHolder(3, 1);
        setPlaceHolder(3, 8);
        setPlaceHolder(4, 1);
        setPlaceHolder(4, 8);

        setPlaceHolder(1);
        setPlaceHolder(5);

        setBackPage(ControlPanelInventory.class);
    }

    @Override
    public ClickableItem constructItem(Group group) {
        return new ClickableItem(ItemBuilder.of(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjZlZTAzYmE4NTBlYmJhMjE3MjFjYjMzN2Y3ZWRlYWI5YjBmYTYxNWE4NjJjNjg3MGNjOWM0ZDA1ZDRkMzJmYSJ9fX0=")).withName("§8» §e" + group.getName()), () -> {
            new GroupManageInventory(getPlayer(), group);
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
