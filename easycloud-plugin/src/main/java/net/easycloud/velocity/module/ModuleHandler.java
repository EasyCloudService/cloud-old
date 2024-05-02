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
        FileHelper.writeIfNotExists(path, new MotdConfig(true, "§8▶▷ §bEasyCloud §8» §7simplicity meets §b§operformance", "§8➥ §7download on §8(§beasycloudservice.de§8) §8(§blatest§8)"));
        FileHelper.writeIfNotExists(path, new TablistConfig(true,
                "\n§8▶▷ §bEasyCloud §8» §7simplicity meets §b§operformance \n  §8◁ §bOnline §8» §7%online%§8/§7%max% §8| §bServer §8» §7%server% §8▷ \n",
                "\n §8▶▷ §bDownload §8» §7easycloudservice.de \n §8▶▷ §bGithub §8» §7github.com/easycloudservice \n"));

        this.config = FileHelper.read(path, ModuleConfig.class);
        this.motdConfig = FileHelper.read(path, MotdConfig.class);
        this.tablistConfig = FileHelper.read(path, TablistConfig.class);
    }
}
