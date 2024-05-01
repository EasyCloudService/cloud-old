package net.easycloud.spigot.inventory;

import de.flxwdev.ascan.inventory.MultiInventory;
import de.flxwdev.ascan.inventory.item.ClickableItem;
import de.flxwdev.ascan.inventory.item.ItemBuilder;
import dev.dbassett.skullcreator.SkullCreator;
import net.easycloud.api.CloudDriver;
import net.easycloud.api.service.IService;
import net.easycloud.spigot.inventory.group.GroupListInventory;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public final class ControlPanelInventory extends MultiInventory<IService> {

    public ControlPanelInventory(Player player) {
        super(player, "§cEasyCloud", 5, false, CloudDriver.getInstance().getServiceProvider().getServices());

        player.playSound(player.getLocation(), Sound.BLOCK_BARREL_OPEN, 1, 1);

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
            getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
            new ServiceManageInventory(getPlayer(), service);
        });
    }
}
