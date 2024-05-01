package net.easycloud.velocity.module.motd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.http.aeon.annotations.Options;

@Getter
@AllArgsConstructor
@Options(name = "easycloud-motd")
public final class MotdConfig {
    private final boolean enable;

    private final String line1;
    private final String line2;
}
