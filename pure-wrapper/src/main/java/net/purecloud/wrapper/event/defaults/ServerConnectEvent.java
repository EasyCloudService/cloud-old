package net.purecloud.wrapper.event.defaults;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.purecloud.api.event.Event;
import net.purecloud.api.group.Group;

@Getter
@AllArgsConstructor
public final class ServerConnectEvent implements Event {
    private final Group group;

}
