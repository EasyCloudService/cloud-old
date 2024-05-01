package net.easycloud.base.group;

import lombok.Getter;
import net.bytemc.evelon.repository.Filter;
import net.bytemc.evelon.repository.Repository;
import net.easycloud.api.group.Group;
import net.easycloud.api.group.GroupProvider;
import net.easycloud.api.group.misc.GroupType;
import net.easycloud.api.misc.FileHelper;
import net.easycloud.api.misc.Reflections;
import net.easycloud.base.Base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Getter
public final class SimpleGroupHandler implements GroupProvider {
    private final Repository<Group> repository;

    public SimpleGroupHandler() {
        this.repository = Repository.create(Group.class);

        if(!repository.query().database().findAll().isEmpty()) {
            Base.getInstance().getLogger().log("Following groups were loaded:");
        }
        repository.query().database().findAll().forEach(it -> {
            Base.getInstance().getLogger().log(" §7- §b" + it.getName());
        });
        if(!repository.query().database().findAll().isEmpty()) {
            Base.getInstance().getLogger().log("");
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
        return repository.query().filter(Filter.match("name", name))
                .database()
                .findFirst();
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

        repository.query().create(group);

        Base.getInstance().getLogger().log("Group " + group.getName() + " was successfully created!");
    }
}
