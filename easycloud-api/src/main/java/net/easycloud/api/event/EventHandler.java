package net.easycloud.api.event;

public interface EventHandler {
    <T extends Event> void call(T event);
    <T extends Event> void register(T event);
}
