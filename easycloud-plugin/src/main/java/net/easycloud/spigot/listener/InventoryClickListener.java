package net.easycloud.spigot.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.easycloud.api.CloudDriver;
import net.easycloud.spigot.SpigotPlugin;
import net.easycloud.spigot.inventory.ControlPanelInventory;
import net.easycloud.spigot.inventory.ServiceStartInventory;
import net.easycloud.spigot.inventory.ServicesInventory;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

@SuppressWarnings("unused")
public final class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getOriginalTitle().equals("§bCloud §8» §cControlPanel")) {
            if (event.getWhoClicked() instanceof Player player && event.getCurrentItem() != null) {
                var name = event.getCurrentItem().getItemMeta().getDisplayName();
                if (name.equals("§8» §cServer verwalten")) {
                    new ServicesInventory(player);
                    return;
                }
            }
            event.setCancelled(true);
        } else if (event.getView().getOriginalTitle().equals("§bCloud §8» §cServices")) {
            if (event.getWhoClicked() instanceof Player player && event.getCurrentItem() != null) {
                var name = event.getCurrentItem().getItemMeta().getDisplayName();
                if (name.equals("§8» §cGruppe starten")) {
                    new ServiceStartInventory(player);
                    return;
                }

                CloudDriver.getInstance().getServiceProvider().getServices().stream().filter(it -> it.getId().equalsIgnoreCase(name.replace("§8» §e", ""))).findFirst().ifPresent(service -> {
                    if (event.isLeftClick() && !event.isShiftClick()) {
                        player.sendMessage("§bCloud §8» §7Try to connect you to §c" + service.getId() + "§7...");

                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Connect");
                        out.writeUTF(service.getId());
                        player.sendPluginMessage(SpigotPlugin.getInstance(), "BungeeCord", out.toByteArray());
                    } else if (event.isRightClick() && !event.isShiftClick()) {
                        CloudDriver.getInstance().getServiceProvider().stop(service.getId());

                        player.closeInventory();
                        player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DEATH, 1f, 2f);
                    } else if (event.isLeftClick() && event.isShiftClick()) {
                        /*var group = service.getGroup();
                        if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                            player.sendMessage(Component.text("§bCloud §8» §cYour mainhand is empty§8!"));
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 2f);
                            return;
                        }

                        group.setMaterial(player.getInventory().getItemInMainHand().getType().name().toUpperCase());
                        CloudDriver.getInstance().getGroupProvider().getRepository().edit(group);

                        new ControlPanelInventory(player);
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 2f);*/
                    }
                });
            }
            event.setCancelled(true);
        } else if (event.getView().getOriginalTitle().equals("§bCloud §8» §cService start")) {
            if (event.getWhoClicked() instanceof Player player && event.getCurrentItem() != null) {
                var name = event.getCurrentItem().getItemMeta().getDisplayName();
                if (name.equals("§8» §cZurück")) {
                    new ServicesInventory(player);
                    return;
                }
                CloudDriver.getInstance().getGroupProvider().getRepository().query().database().findAll().stream().filter(it -> it.getName().equalsIgnoreCase(name.replace("§8» §c", ""))).findFirst().ifPresent(group -> {
                    if (event.isLeftClick() && !event.isShiftClick()) {
                        CloudDriver.getInstance().getServiceProvider().start(group, 1);
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 2f);

                        new ControlPanelInventory(player);
                    }

                });
            }
            event.setCancelled(true);
        }
    }
}
