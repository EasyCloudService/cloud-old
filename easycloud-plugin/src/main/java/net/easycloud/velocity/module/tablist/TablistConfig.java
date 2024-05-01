package net.easycloud.velocity.module.tablist;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class TablistConfig {
    private final boolean enable;

    private final String header;
    private final String footer;
}
