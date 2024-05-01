package net.easycloud.velocity.module;

import lombok.Getter;
import net.http.aeon.Aeon;
import net.easycloud.velocity.module.motd.MotdConfig;
import net.easycloud.velocity.module.tablist.TablistConfig;

import java.nio.file.Path;

@Getter
public final class ModuleHandler {
    private final ModuleConfig config;

    public ModuleHandler() {
        this.config = Aeon.insert(new ModuleConfig(
                "Lobby",
                new MotdConfig(true, "§9EasyCloud service", "§7"), new TablistConfig(true,
                "§7                            §1\n§bEasyCloud §8§l| §a%online%§8/§c%max%\n§7Current server is §9%server%\n",
                "\n§7Powered by §9EasyCloud\n§7Hosted on §9Venocix\n§7                            §1"
        ), true), Path.of(System.getProperty("user.dir")).getParent().getParent().getParent());
    }
}
