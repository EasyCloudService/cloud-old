package net.purecloud.velocity.listener;

import net.purecloud.api.event.Event;
import net.purecloud.api.event.Subscribe;
import net.purecloud.wrapper.event.defaults.ServerConnectEvent;

public final class ServerConnectListener implements Event {

    @Subscribe
    public void onServerConnect(ServerConnectEvent event) {
        System.out.println(event.getGroup().getName());
    }
}
