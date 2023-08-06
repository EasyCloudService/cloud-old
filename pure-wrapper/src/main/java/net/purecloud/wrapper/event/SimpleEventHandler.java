package net.purecloud.wrapper.event;

import net.purecloud.api.event.Event;
import net.purecloud.api.event.EventHandler;
import net.purecloud.api.event.Subscribe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SimpleEventHandler implements EventHandler {
    private final List<Event> events;

    public SimpleEventHandler() {
        this.events = new ArrayList<>();
    }

    @Override
    public <T extends Event> void call(T event) {
        for (Event event2 : events) {
            var clazz = event2.getClass();

            System.out.println("MARCO " + clazz.getDeclaredMethods().length);
            for (Method method : Arrays.stream(clazz.getDeclaredMethods()).filter(it -> {
                System.out.println("MARCO");
                if(it.isAnnotationPresent(Subscribe.class)) {
                    System.out.println("POLO");
                }
                if(it.getParameterCount() == 1) {
                    System.out.println("MARCO2");
                }

                return it.isAnnotationPresent(Subscribe.class) && it.getParameterCount() == 1 && Arrays.stream(it.getParameterTypes()).anyMatch(it2 -> it2 == event.getClass());
            }).toList()) {
                System.out.println("FINISH");

                try {
                    method.invoke(null, event);
                } catch (IllegalAccessException | InvocationTargetException exception) {
                    throw new RuntimeException(exception);
                }
            }
        }
    }

    @Override
    public <T extends Event> void register(T event) {
        System.out.println("POLO REGISTER");
        events.add(event);
    }
}
