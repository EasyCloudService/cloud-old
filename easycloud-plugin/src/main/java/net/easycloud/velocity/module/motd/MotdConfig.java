package net.easycloud.velocity.module.motd;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class MotdConfig {
    private final boolean enable;

    private final String line1;
    private final String line2;
}
