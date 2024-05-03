package net.easycloud.velocity.module.tablist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.easycloud.api.conf.FileName;

@Getter
@AllArgsConstructor
@FileName(name = "easycloud-tablist")
@SuppressWarnings("ClassCanBeRecord")
public final class TablistConfig {
    private final boolean enable;

    private final String header;
    private final String footer;
}
