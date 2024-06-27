package net.easycloud.wrapper.event.defaults;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.easycloud.api.event.Event;
import net.easycloud.api.group.Group;

@SuppressWarnings("ALL")
@Getter
@AllArgsConstructor
public final class ServerConnectEvent implements Event {
    private final Group group;
}
