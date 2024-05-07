package net.easycloud.base.group;

import dev.httpmarco.evelon.MariaDbLayer;
import dev.httpmarco.evelon.Repository;
import lombok.Getter;
import net.easycloud.api.group.Group;
import net.easycloud.api.group.GroupProvider;
import net.easycloud.api.group.misc.GroupType;
import net.easycloud.api.group.misc.GroupVersion;
import net.easycloud.api.misc.DownloadHelper;
import net.easycloud.api.misc.Reflections;
import net.easycloud.base.Base;
import net.easycloud.base.setup.ConsoleSetup;
import net.easycloud.base.setup.SetupBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Getter
public final class SimpleGroupHandler implements GroupProvider {
    private final Repository<Group> repository;

    public SimpleGroupHandler() {
        this.repository = Repository.build(Group.class).withId("groups").withLayer(MariaDbLayer.class).build();

        if(this.repository.query().find().isEmpty()) {
            boolean[] setup = {true};

            ConsoleSetup.subscribe(List.of(
                    SetupBuilder.<Boolean>get()
                            .key("service.create.proxy")
                            .question("&7Dou you want to create a proxy?")
                            .possibleResults(List.of(true, false))
                            .build(),
                    SetupBuilder.<Boolean>get()
                            .key("service.create.lobby")
                            .question("&7Dou you want to create a lobby? (1.20.4)")
                            .possibleResults(List.of(true, false))
                            .build()
            ), values -> {
                if(Boolean.parseBoolean(values.get("service.create.proxy"))) {
                    create(new Group("Proxy", 512, 1, 1, 50, false, "ANVIL", GroupType.PROXY, GroupVersion.VELOCITY_LATEST));
                }
                if(Boolean.parseBoolean(values.get("service.create.lobby"))) {
                    create(new Group("Lobby", 1024, 1, -1, 50, false, "ANVIL", GroupType.LOBBY, GroupVersion.PAPER_1_20_4));
                }
                setup[0] = false;
            });

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(!setup[0]) {
                    break;
                }
            }
        }

        repository.query().find().forEach(it -> {
            Base.getInstance().getLogger().log("&7Loaded &9" + it.getName() + " &7as &9" + it.getType().name() + " &7service-group.");
        });
        if(repository.query().find().isEmpty()) {
            Base.getInstance().getLogger().log("No service-group was found!");
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
        return repository.query().match("name", name).findFirst();
    }

    @Override
    public void create(Group group) {
        Base.getInstance().getLogger().log("Group " + group.getName() + " will be created...");

        Path directory = Path.of(System.getProperty("user.dir") + File.separator +  "template" + File.separator + group.getType().getFolder() + File.separator + group.getName());
        Reflections.createPath(directory);
        Reflections.createPath(directory.resolve("plugins"));

        DownloadHelper.downloadFromUrl(group.getVersion().getUrl(), "Server.jar", directory);
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
