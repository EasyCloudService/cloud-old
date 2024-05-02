package net.easycloud.velocity.module;

import lombok.Getter;
import net.easycloud.api.conf.FileHelper;
import net.easycloud.velocity.module.motd.MotdConfig;
import net.easycloud.velocity.module.tablist.TablistConfig;

import java.nio.file.Path;

@Getter
public final class ModuleHandler {
    private final ModuleConfig config;
    private final MotdConfig motdConfig;
    private final TablistConfig tablistConfig;

    public ModuleHandler() {
        var path = Path.of(System.getProperty("user.dir")).getParent().getParent().getParent().resolve("modules");
        path.toFile().mkdirs();

        // Create if not exists
        FileHelper.writeIfNotExists(path, new ModuleConfig(true, "Lobby"));
        FileHelper.writeIfNotExists(path, new MotdConfig(true, "§9EasyCloud service", "§7"));
        FileHelper.writeIfNotExists(path, new TablistConfig(true,
                "§7                            §1\n§feasy§7@§bCloud §8§l| §a%online%§8/§c%max%\n§7Current server is §9%server%\n",
                "\n§7Powered by §9EasyCloud\n§7Hosted on §9SyncServ\n§7                            §1"));

        this.config = FileHelper.read(path, ModuleConfig.class);
        this.motdConfig = FileHelper.read(path, MotdConfig.class);
        this.tablistConfig = FileHelper.read(path, TablistConfig.class);
    }
}
