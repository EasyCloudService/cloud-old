package net.easycloud.base.group;

import dev.httpmarco.evelon.MariaDbLayer;
import dev.httpmarco.evelon.Repository;
import lombok.Getter;
import net.easycloud.api.console.LogType;
import net.easycloud.api.group.Group;
import net.easycloud.api.group.GroupProvider;
import net.easycloud.api.group.misc.GroupType;
import net.easycloud.api.group.misc.GroupVersion;
import net.easycloud.api.misc.DownloadHelper;
import net.easycloud.api.misc.Reflections;
import net.easycloud.api.network.packet.GroupCreatePacket;
import net.easycloud.base.Base;
import net.easycloud.base.service.SimpleServiceHandler;
import net.easycloud.base.setup.ConsoleSetup;
import net.easycloud.base.setup.SetupBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
public final class SimpleGroupHandler implements GroupProvider {
    private final Repository<Group> repository;

    public SimpleGroupHandler() {
        this.repository = Repository.build(Group.class).withId("groups").withLayer(MariaDbLayer.class).build();

        if(this.repository.query().find().isEmpty()) {
            Base.instance().logger().log("No group was found! Please setup a group first. (group setup)", LogType.WARNING);
        }

        repository.query().find().forEach(it -> {
            Base.instance().logger().log("&7Loaded &9" + it.getName() + " &7as &9" + it.getType().name() + " &7service-group.");
        });
        if(repository.query().find().isEmpty()) {
            Base.instance().logger().log("No service-group was found!");
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

        Base.instance().nettyServer().listen(GroupCreatePacket.class, (transmit, packet) -> {
            create(packet.getGroup());
        });
    }

    public void groupSetup() {
        ConsoleSetup.subscribe(List.of(
                SetupBuilder.<String>get()
                        .key("group.name")
                        .question("&7What is you group name?")
                        .build(),
                SetupBuilder.<String>get()
                        .key("group.memory")
                        .question("&7What is you group memory?")
                        .build(),
                SetupBuilder.<Integer>get()
                        .key("group.minOnline")
                        .question("&7What is you group minOnline count?")
                        .build(),
                SetupBuilder.<String>get()
                        .key("group.maxOnline")
                        .question("&7What is you group maxOnline count? (-1 = unlimited)")
                        .build(),
                SetupBuilder.<String>get()
                        .key("group.maxPlayers")
                        .question("&7What is you group maxPlayers?")
                        .build(),
                SetupBuilder.<Boolean>get()
                        .key("group.static")
                        .question("&7Is your group static?")
                        .possibleResults(List.of(true, false))
                        .build(),
                SetupBuilder.<String>get()
                        .key("group.type")
                        .question("&7What is you group type?")
                        .possibleResults(Arrays.stream(GroupType.values()).map(Enum::name).toList())
                        .build(),
                SetupBuilder.<String>get()
                        .key("group.version")
                        .question("&7What is you group version?")
                        .possibleResults(Arrays.stream(GroupVersion.values()).map(Enum::name).toList())
                        .build()
        ), values -> {
            Base.instance().groupProvider().getRepository().query().match("name", values.get("group.name"))
                    .find().stream().findFirst()
                    .ifPresentOrElse(group -> {
                        Base.instance().logger().log("Group " + values.get("group.name") + " already exists!", LogType.WARNING);
                    }, () -> {
                        Base.instance().groupProvider().create(new Group(
                                UUID.randomUUID(),
                                values.get("group.name"),
                                Integer.parseInt(values.get("group.memory")),
                                Integer.parseInt(values.get("group.minOnline")),
                                Integer.parseInt(values.get("group.maxOnline")),
                                Integer.parseInt(values.get("group.maxPlayers")),
                                Boolean.parseBoolean(values.get("group.static")),
                                "ANVIL",
                                GroupType.valueOf(values.get("group.type")),
                                GroupVersion.valueOf(values.get("group.version"))
                        ));
                    });
            ((SimpleServiceHandler) Base.instance().serviceProvider()).update();
        });
    }

    @Override
    public Group getOrThrow(String name) {
        return repository.query().match("name", name).findFirst();
    }

    @Override
    public void create(Group group) {
        Base.instance().logger().log("Group " + group.getName() + " will be created...");

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

        Base.instance().logger().log("Group " + group.getName() + " was successfully created!");
    }
}
