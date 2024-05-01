package net.easycloud.velocity.module;

import lombok.Getter;
import net.http.aeon.Aeon;
import net.easycloud.velocity.module.motd.MotdConfig;
import net.easycloud.velocity.module.tablist.TablistConfig;

import java.nio.file.Path;

@Getter
public final class ModuleHandler {
    private final ModuleConfig config;
    private final MotdConfig motdConfig;
    private final TablistConfig tablistConfig;

    public ModuleHandler() {
        Path.of(System.getProperty("user.dir")).getParent().getParent().getParent().resolve("modules").toFile().mkdirs();

        this.config = Aeon.insert(new ModuleConfig(true, "Lobby"), Path.of(System.getProperty("user.dir")).getParent().getParent().getParent().resolve("modules"));
        this.motdConfig = Aeon.insert(new MotdConfig(true, "§9EasyCloud service", "§7"), Path.of(System.getProperty("user.dir")).getParent().getParent().getParent().resolve("modules"));
        this.tablistConfig = Aeon.insert(new TablistConfig(true,
                "§7                            §1\n§bEasyCloud §8§l| §a%online%§8/§c%max%\n§7Current server is §9%server%\n",
                "\n§7Powered by §9EasyCloud\n§7Hosted on §9Venocix\n§7                            §1"), Path.of(System.getProperty("user.dir")).getParent().getParent().getParent().resolve("modules"));
    }
}
