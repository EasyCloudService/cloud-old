package net.easycloud.base.setup;

import net.easycloud.api.console.LogType;
import net.easycloud.base.Base;
import net.easycloud.base.logger.SimpleLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class ConsoleSetup {
    private static final Map<String, String> cache = new HashMap<>();

    public static boolean SETUP_ENABLED = false;
    private static List<SetupBuilder<?>> list;
    private static Consumer<Map<String, String>> consumer;

    private static void nextLine() {
        ((SimpleLogger) Base.instance().logger()).getConsole().clearConsole();

        if(list.isEmpty()) {
            consumer.accept(cache);
            cache.clear();
            SETUP_ENABLED = false;
            Base.instance().logger().log("&rSetup was &asucessfully &rfinished&7.", LogType.SUCCESS);
            return;
        }
        var item = list.getFirst();
        Base.instance().logger().log("&7" + item.question(), LogType.INFO);
        if(item.possible() != null) {
            var stringbuilder = new StringBuilder();
            item.possible().forEach(it -> {
                if(!stringbuilder.isEmpty()) stringbuilder.append("&7, ");
                stringbuilder.append("&e").append(it);
            });

            Base.instance().logger().log("&f[" + stringbuilder + "&f]", LogType.INFO);
        }
    }

    public static void pushLine(String line) {
        var item = list.getFirst();
        if(item.possible() != null) {
            var result = item.possible().stream().filter(it -> it.toString().equalsIgnoreCase(line)).findFirst().orElse(null);
            if(result == null) {
                Base.instance().logger().log("&cInvalid answer.", LogType.ERROR);
                return;
            }
        }
        cache.put(item.key(), line);
        list.removeFirst();
        nextLine();
    }

    public static void subscribe(List<SetupBuilder<?>> setupBuilder, Consumer<Map<String, String>> onFinish) {
        SETUP_ENABLED = true;
        list = new ArrayList<>(setupBuilder);
        consumer = onFinish;
        nextLine();
    }
}
