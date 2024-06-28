package net.easycloud.base.service;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.easycloud.api.console.LogType;
import net.easycloud.api.service.state.ServiceState;
import net.easycloud.api.utils.file.FileHelper;
import net.easycloud.api.group.Group;
import net.easycloud.api.group.misc.GroupType;
import net.easycloud.api.service.IService;
import net.easycloud.base.Base;
import net.easycloud.base.CloudPath;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Service implements IService {
    private final Group group;
    private final String id;
    private final int port;
    private final List<String> consoleCache;

    @Setter
    private boolean console;
    @Setter
    private ServiceState state;
    private Process process;
    private BufferedWriter writer;

    public Service(Group group, int port, String id) {
        this.group = group;
        this.port = port;
        this.id = id;
        this.consoleCache = new ArrayList<>();
        this.console = false;
        this.state = ServiceState.STARTING;
    }

    public void setProcess(Process process) {
        this.process = process;
        this.writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    //System.out.println(id + " ADDED LINE " + consoleCache.size());
                    consoleCache.add(line);
                    if (console) {
                        Base.instance().logger().log("§7[§r" + id + "§7] §r" + line);
                    }
                }
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }).start();
    }

    public void execute(String command) {
        try {
            this.writer.write(command);
            this.writer.newLine();
            this.writer.flush();
        } catch (IOException ignored) {
        }
    }

    @SneakyThrows
    public void stop(boolean update) {
        execute("stop");

        Thread.sleep(500);

        if (this.process != null) {
            this.process.destroy();
            this.process.toHandle().destroyForcibly();
        }

        if (!group.isStaticService()) {
            var path = Path.of(System.getProperty("user.dir") + File.separator + "tmp" + File.separator + group.getType().getFolder() + File.separator + id);
            FileHelper.removeDirectory(path);
        }
        Base.instance().logger().log(id + " was successfully §cstopped&7!", LogType.WRAPPER);

        if(update) {
            ((SimpleServiceHandler) Base.instance().serviceProvider()).update();
        }
    }

    @Override
    public ServiceState getState() {
        return this.state;
    }

    @Override
    public Path getDirectory() {
        var path = CloudPath.TEMP;
        if(group.isStaticService()) {
            path = CloudPath.STATIC;
        }
        return path.resolve((group.getType().equals(GroupType.LOBBY) ? GroupType.SERVER.toString() : group.getType().toString())).resolve(id);
    }
}
