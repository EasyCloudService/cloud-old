package net.easycloud.velocity.module.motd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.easycloud.api.conf.FileName;

@Getter
@AllArgsConstructor
@FileName(name = "easycloud-motd")
@SuppressWarnings("ClassCanBeRecord")
public final class MotdConfig {
    private final boolean enable;

    private final String line1;
    private final String line2;
}
