package net.easycloud.velocity.module;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.easycloud.api.conf.FileName;

@Getter
@AllArgsConstructor
@FileName(name = "easycloud-global")
public final class ModuleConfig {
    private final Boolean maintenance;
    private final String lobbyGroupName;
}
