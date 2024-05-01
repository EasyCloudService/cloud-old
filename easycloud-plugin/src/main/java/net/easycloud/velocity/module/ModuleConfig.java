package net.easycloud.velocity.module;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.http.aeon.annotations.Options;
import net.easycloud.velocity.module.motd.MotdConfig;
import net.easycloud.velocity.module.tablist.TablistConfig;

@Getter
@AllArgsConstructor
@Options(name = "modules")
public final class ModuleConfig {
    private final String lobbyGroupName;
    private final MotdConfig motd;
    private final TablistConfig tablist;
    private final Boolean maintenance;
}
