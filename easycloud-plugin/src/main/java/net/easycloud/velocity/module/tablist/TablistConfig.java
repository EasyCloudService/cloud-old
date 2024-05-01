package net.easycloud.velocity.module.tablist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.http.aeon.annotations.Options;

@Getter
@AllArgsConstructor
@Options(name = "easycloud-tablist")
public final class TablistConfig {
    private final boolean enable;

    private final String header;
    private final String footer;
}
