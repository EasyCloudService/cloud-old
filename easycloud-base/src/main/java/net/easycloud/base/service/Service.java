package net.easycloud.base.service;

import lombok.Getter;
import lombok.Setter;
import net.easycloud.api.configuration.file.FileHelper;
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
    private final int port;
    private final String id;
    private final List<String> consoleCache;

    @Setter
    private boolean console;
    private Process process;
    private BufferedWriter writer;

    public Service(Group group, int port, String id) {
        this.group = group;
        this.port = port;
        this.id = id;
        this.consoleCache = new ArrayList<>();
        this.console = false;
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
                        Base.getInstance().getLogger().log("§7[§r" + id + "§7] §r" + line);
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

    public void stop() {
        if (this.process != null) {
            this.process.destroy();
            this.process.toHandle().destroyForcibly();
        }

        if (!group.isStaticService()) {
            var path = Path.of(System.getProperty("user.dir") + File.separator + "tmp" + File.separator + group.getType().getFolder() + File.separator + id);
            FileHelper.removeDirectory(path);
        }
        System.out.println(id + " was successfully §cstopped&7!");
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
