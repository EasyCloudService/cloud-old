package net.purecloud.spigot.listener;

import de.flxwdns.oraculusdb.repository.Repository;
import net.kyori.adventure.text.Component;
import net.purecloud.api.CloudDriver;
import net.purecloud.api.group.Group;
import net.purecloud.spigot.inventory.ServiceInventory;
import net.purecloud.spigot.inventory.ServiceStartInventory;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

@SuppressWarnings("unused")
public final class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getOriginalTitle().equals("§bCloud §8» §eServices")) {
            if (event.getWhoClicked() instanceof Player player && event.getCurrentItem() != null) {
                var name = event.getCurrentItem().getItemMeta().getDisplayName();
                if (name.equals("§8» §aStart template")) {
                    new ServiceStartInventory(player);
                    return;
                }

                CloudDriver.getInstance().getServiceProvider().getServices().stream().filter(it -> it.getId().equalsIgnoreCase(name.replace("§8» §e", ""))).findFirst().ifPresent(service -> {
                    if (event.isLeftClick() && !event.isShiftClick()) {
                        player.sendMessage("send: " + service.getId());
                    } else if (event.isRightClick() && !event.isShiftClick()) {
                        CloudDriver.getInstance().getServiceProvider().stop(service.getId());

                        player.closeInventory();
                        player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DEATH, 1f, 2f);
                    } else if (event.isLeftClick() && event.isShiftClick()) {
                        var group = service.getGroup();
                        if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                            player.sendMessage(Component.text("§bCloud §8» §cYour mainhand is empty§8!"));
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 2f);
                            return;
                        }

                        group.setMaterial(player.getInventory().getItemInMainHand().getType().name().toUpperCase());
                        CloudDriver.getInstance().getGroupProvider().getRepository().edit(group);

                        new ServiceInventory(player);
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 2f);
                    }
                });
            }
            event.setCancelled(true);
        } else if (event.getView().getOriginalTitle().equals("§bCloud §8» §eService start")) {
            if (event.getWhoClicked() instanceof Player player && event.getCurrentItem() != null) {
                var name = event.getCurrentItem().getItemMeta().getDisplayName();
                if (name.equals("§8» §eBack")) {
                    new ServiceInventory(player);
                    return;
                }
                CloudDriver.getInstance().getGroupProvider().getRepository().findAll().stream().filter(it -> it.getName().equalsIgnoreCase(name.replace("§8» §e", ""))).findFirst().ifPresent(group -> {
                    if (event.isLeftClick() && !event.isShiftClick()) {
                        CloudDriver.getInstance().getServiceProvider().start(group, 1);
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 2f);

                        new ServiceInventory(player);
                    }

                });
            }
            event.setCancelled(true);
        }
    }
}
