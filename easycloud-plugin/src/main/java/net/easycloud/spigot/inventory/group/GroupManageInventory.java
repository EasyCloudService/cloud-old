package net.easycloud.spigot.inventory.group;

import de.flxwdev.ascan.inventory.SingleInventory;
import de.flxwdev.ascan.inventory.item.ClickableItem;
import de.flxwdev.ascan.inventory.item.ItemBuilder;
import net.bytemc.evelon.repository.Filter;
import net.easycloud.api.CloudDriver;
import net.easycloud.api.group.Group;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public final class GroupManageInventory extends SingleInventory {

    public GroupManageInventory(Player player, Group group) {
        super(player, "§cEasyCloud §8- §c"+ group.getName(), 3, false);

        player.playSound(player.getLocation(), Sound.BLOCK_BARREL_OPEN, 1, 1);

        setPlaceHolder(1);
        setPlaceHolder(2);
        setPlaceHolder(3);

        setClickableItem(2, 1, new ClickableItem(ItemBuilder.of(Material.LIME_TERRACOTTA).withName("§8» §aStart service"), () -> {
            player.closeInventory();
            CloudDriver.getInstance().getServiceProvider().start(group, 1);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        }));

        setClickableItem(2, 3, new ClickableItem(ItemBuilder.of(Material.CHEST).withName("§8» §eMemory§8: §b" + group.getMaxMemory()).withLore(List.of(
                "§7Left click §8- §c- 512 Memory",
                "§7Right click §8- §a+ 512 Memory"
        )), () -> {
            if(group.getMaxMemory() <= 512) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }
            group.setMaxMemory(group.getMaxMemory() - 512);
            player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
            CloudDriver.getInstance().getGroupProvider().getRepository().query().filter(Filter.match("name", group.getName())).database().update(group);
            new GroupManageInventory(player, group);
        }, () -> {
            group.setMaxMemory(group.getMaxMemory() + 512);
            player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
            CloudDriver.getInstance().getGroupProvider().getRepository().query().filter(Filter.match("name", group.getName())).database().update(group);
            new GroupManageInventory(player, group);
        }));

        setClickableItem(2, 5, new ClickableItem(ItemBuilder.of(Material.STRUCTURE_VOID).withName("§8» §aAlways online§8: §b" + group.getMinOnline()).withLore(List.of(
                "§7Left click §8- §c- 1 count",
                "§7Right click §8- §a+ 1 count"
        )), () -> {
            if(group.getMinOnline() <= 0) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }
            group.setMinOnline(group.getMinOnline() - 1);
            player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
            CloudDriver.getInstance().getGroupProvider().getRepository().query().filter(Filter.match("name", group.getName())).database().update(group);
            new GroupManageInventory(player, group);
        }, () -> {
            group.setMinOnline(group.getMinOnline() + 1);
            player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
            CloudDriver.getInstance().getGroupProvider().getRepository().query().filter(Filter.match("name", group.getName())).database().update(group);
            new GroupManageInventory(player, group);
        }));

        setClickableItem(2, 7, new ClickableItem(ItemBuilder.of(Material.BARRIER).withName("§8» §aMaximal online§8: §b" + group.getMaxOnline()).withLore(List.of(
                "§7Left click §8- §c- 1 count",
                "§7Right click §8- §a+ 1 count"
        )), () -> {
            if(group.getMaxOnline() <= 0) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }
            group.setMaxOnline(group.getMaxOnline() - 1);
            player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
            CloudDriver.getInstance().getGroupProvider().getRepository().query().filter(Filter.match("name", group.getName())).database().update(group);
            new GroupManageInventory(player, group);
        }, () -> {
            group.setMaxOnline(group.getMaxOnline() + 1);
            player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
            CloudDriver.getInstance().getGroupProvider().getRepository().query().filter(Filter.match("name", group.getName())).database().update(group);
            new GroupManageInventory(player, group);
        }));

        setBackPage(GroupListInventory.class);
    }
}
