package net.purecloud.spigot;

import net.purecloud.spigot.commands.ServicesCommand;
import net.purecloud.spigot.listener.AsyncPlayerPreLoginListener;
import net.purecloud.spigot.listener.InventoryClickListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("all")
public final class SpigotPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("""
                                
                                
                §r$$$$$$$\\                                 $$$$$$\\  $$\\                           $$\\§r
                §r$$  __$$\\                               $$  __$$\\ $$ |                          $$ |§r
                §r$$ |  $$ |$$\\   $$\\  $$$$$$\\   $$$$$$\\  $$ /  \\__|$$ | $$$$$$\\  $$\\   $$\\  $$$$$$$ |§r
                §r$$$$$$$  |$$ |  $$ |$$  __$$\\ $$  __$$\\ $$ |      $$ |$$  __$$\\ $$ |  $$ |$$  __$$ |§r
                §r$$  ____/ $$ |  $$ |$$ |  \\__|$$$$$$$$ |$$ |      $$ |$$ /  $$ |$$ |  $$ |$$ /  $$ |§r
                §r$$ |      $$ |  $$ |$$ |      $$   ____|$$ |  $$\\ $$ |$$ |  $$ |$$ |  $$ |$$ |  $$ |§r
                §r$$ |      \\$$$$$$  |$$ |      \\$$$$$$$\\ \\$$$$$$  |$$ |\\$$$$$$  |\\$$$$$$  |\\$$$$$$$ |§r
                §r\\__|       \\______/ \\__|       \\_______| \\______/ \\__| \\______/  \\______/  \\_______|§r
                      
                §bPureCloud §7coded by §cFlxwDNS§r
                         
                """);


        Bukkit.getConsoleSender().sendMessage("§bPlugin §7was §bsuccessfully §7connected to the §bWrapper!");

        getCommand("services").setExecutor(new ServicesCommand());
        getServer().getPluginManager().registerEvents(new AsyncPlayerPreLoginListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
    }
}
