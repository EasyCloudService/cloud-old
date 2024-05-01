package net.easycloud.spigot.inventory;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.flxwdev.ascan.inventory.SingleInventory;
import de.flxwdev.ascan.inventory.item.ClickableItem;
import de.flxwdev.ascan.inventory.item.ItemBuilder;
import dev.dbassett.skullcreator.SkullCreator;
import net.easycloud.api.CloudDriver;
import net.easycloud.api.service.IService;
import net.easycloud.spigot.SpigotPlugin;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public final class ServiceManageInventory extends SingleInventory {

    public ServiceManageInventory(Player player, IService service) {
        super(player, "§cEasyCloud §8- §c"+ service.getId(), 3, false);

        setPlaceHolder(1);
        setPlaceHolder(2);
        setPlaceHolder(3);

        setClickableItem(2, 2, new ClickableItem(ItemBuilder.of(Material.LIME_TERRACOTTA).withName("§8» §aStart another service"), () -> {
            player.closeInventory();
            CloudDriver.getInstance().getServiceProvider().start(service.getGroup(), 1);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        }));
        setItem(2, 4, ItemBuilder.of(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjZlZTAzYmE4NTBlYmJhMjE3MjFjYjMzN2Y3ZWRlYWI5YjBmYTYxNWE4NjJjNjg3MGNjOWM0ZDA1ZDRkMzJmYSJ9fX0=")).withName("§8» §e" + service.getId()));
        setClickableItem(2, 6, new ClickableItem(ItemBuilder.of(Material.RED_TERRACOTTA).withName("§8» §cStop service"), () -> {
            CloudDriver.getInstance().getServiceProvider().stop(service.getId());

            player.closeInventory();
            player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DEATH, 1f, 2f);
        }));

        setClickableItem(3, 8, new ClickableItem(ItemBuilder.of(Material.LIGHT_BLUE_DYE).withName("§8» §bJoin service"), () -> {
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(service.getId());
            player.sendPluginMessage(SpigotPlugin.getInstance(), "BungeeCord", out.toByteArray());
            player.sendMessage("§bCloud §8» §7Try to connect you to §c" + service.getId() + "§7...");
        }));

        setBackPage(ControlPanelInventory.class);
    }
}
