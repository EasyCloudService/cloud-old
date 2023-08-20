package net.purecloud.base.group;

import de.flxwdns.oraculusdb.repository.Repository;
import lombok.Getter;
import net.purecloud.api.group.Group;
import net.purecloud.api.group.GroupProvider;
import net.purecloud.api.group.misc.GroupType;
import net.purecloud.api.misc.FileHelper;
import net.purecloud.api.misc.Reflections;
import net.purecloud.base.Base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Getter
public final class SimpleGroupHandler implements GroupProvider {
    private final Repository<Group> repository;

    public SimpleGroupHandler() {
        this.repository = new Repository<>(Group.class);

        if(!repository.findAll().isEmpty()) {
            Base.getInstance().getLogger().log("Following groups were loaded:");
        }
        repository.findAll().forEach(it -> {
            Base.getInstance().getLogger().log(" §7- §b" + it.getName());
        });
        if(!repository.findAll().isEmpty()) {
            System.out.flush();
            System.out.println("\r ");
        }

        var files = List.of(Path.of(
                System.getProperty("user.dir") + File.separator + "template" + File.separator + "EVERY" + File.separator + "plugins"),
                Path.of(System.getProperty("user.dir") + File.separator + "template" + File.separator + "EVERY_SERVER" + File.separator + "plugins"),
                Path.of(System.getProperty("user.dir") + File.separator + "template" + File.separator + "EVERY_PROXY" + File.separator + "plugins"));
        files.forEach(file -> {
            if(!file.toFile().exists()) {
                file.toFile().mkdirs();
            }
        });
    }

    @Override
    public Group getOrThrow(String name) {
        return repository.filter()
                .value("name", name)
                .complete()
                .findFirst().orElseThrow(null);
    }

    @Override
    public void create(Group group) {
        Base.getInstance().getLogger().log("Group " + group.getName() + " will be created...");

        Path directory = Path.of(System.getProperty("user.dir") + File.separator +  "template" + File.separator + group.getType() + File.separator + group.getName());
        Reflections.createPath(directory);
        Reflections.createPath(directory.resolve("plugins"));

        FileHelper.downloadFromUrl(group.getVersion().getUrl(), "Server.jar", directory);
        if(group.getType().equals(GroupType.SERVER)) {
            var processBuilder = new ProcessBuilder("java", "-Dpaperclip.patchonly=true", "-jar", "Server.jar");
            processBuilder.directory(directory.toFile());
            try {
                processBuilder.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        repository.insert(group);

        Base.getInstance().getLogger().log("Group " + group.getName() + " was successfully created!");
    }
}
