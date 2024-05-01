package net.easycloud.velocity.module;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.http.aeon.annotations.Options;

@Getter
@AllArgsConstructor
@Options(name = "easycloud-global")
public final class ModuleConfig {
    private final Boolean maintenance;
    private final String lobbyGroupName;
}
