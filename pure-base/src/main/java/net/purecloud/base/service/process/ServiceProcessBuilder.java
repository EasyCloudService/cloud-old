package net.purecloud.base.service.process;

import net.purecloud.api.group.misc.GroupType;
import net.purecloud.base.service.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarInputStream;

import static net.purecloud.base.CloudPath.STORAGE;

public final class ServiceProcessBuilder {

    private static final List<String> SERVER_FLAGS = Arrays.asList(
            "-XX:+UseG1GC",
            "-XX:+ParallelRefProcEnabled",
            "-XX:MaxGCPauseMillis=200",
            "-XX:+UnlockExperimentalVMOptions",
            "-XX:+DisableExplicitGC",
            "-XX:+AlwaysPreTouch",
            "-XX:G1NewSizePercent=30",
            "-XX:G1MaxNewSizePercent=40",
            "-XX:G1HeapRegionSize=8M",
            "-XX:G1ReservePercent=20",
            "-XX:G1HeapWastePercent=5",
            "-XX:G1MixedGCCountTarget=4",
            "-XX:InitiatingHeapOccupancyPercent=15",
            "-XX:G1MixedGCLiveThresholdPercent=90",
            "-XX:G1RSetUpdatingPauseTimePercent=5",
            "-XX:SurvivorRatio=32",
            "-XX:+PerfDisableSharedMem",
            "-XX:MaxTenuringThreshold=1",
            "-Dusing.aikars.flags=https://mcflags.emc.gs",
            "-Daikars.new.flags=true",
            "-XX:-UseAdaptiveSizePolicy",
            "-XX:CompileThreshold=100",
            "-Dio.netty.recycler.maxCapacity=0",
            "-Dio.netty.recycler.maxCapacity.default=0",
            "-Djline.terminal=jline.UnsupportedTerminal",
            "-Dfile.encoding=UTF-8",
            "-Dclient.encoding.override=UTF-8",
            "-DIReallyKnowWhatIAmDoingISwear=true"
    );
    private static final List<String> PROXY_FLAGS = Arrays.asList(
            "-XX:+UseG1GC",
            "-XX:G1HeapRegionSize=4M",
            "-XX:+UnlockExperimentalVMOptions",
            "-XX:+ParallelRefProcEnabled",
            "-XX:+AlwaysPreTouch",
            "-XX:MaxInlineLevel=15"
    );

    public static Process buildProcess(Service service) {
        try {
            var builder = new ProcessBuilder(arguments(service)).directory(service.getDirectory().toFile());
            builder.redirectErrorStream(true);

            return builder.start();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static List<String> arguments(Service service) {
        try {
            var wrapper = STORAGE.resolve("wrapper.jar").toAbsolutePath();
            var main = new JarInputStream(Files.newInputStream(wrapper)).getManifest().getMainAttributes().getValue("Main-Class");
            var group = service.getGroup();
            List<String> arguments = new ArrayList<>();

            arguments.add("java");
            if (service.getGroup().getType().equals(GroupType.PROXY)) {
                arguments.addAll(PROXY_FLAGS);
            } else {
                arguments.addAll(SERVER_FLAGS);
            }
            arguments.add("-Xms" + group.getMaxMemory() + "M");
            arguments.add("-Xmx" + group.getMaxMemory() + "M");
            arguments.add("-cp");
            arguments.add(wrapper.toString());
            arguments.add("-javaagent:" + wrapper);

            arguments.add(main);
            arguments.add(group.getName());
            arguments.add(service.getId());
            arguments.add(String.valueOf(service.getPort()));

            return arguments;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
