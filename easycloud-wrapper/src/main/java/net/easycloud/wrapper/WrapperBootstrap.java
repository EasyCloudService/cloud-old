package net.easycloud.wrapper;

import dev.httpmarco.evelon.MariaDbLayer;
import dev.httpmarco.evelon.Repository;
import lombok.Getter;
import net.easycloud.api.configuration.SecretConfiguration;
import net.easycloud.api.service.state.ServiceState;
import net.easycloud.api.utils.file.FileHelper;
import net.easycloud.api.group.Group;
import net.easycloud.wrapper.classloader.ApplicationExternalObjectLoader;
import net.easycloud.wrapper.service.Service;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class WrapperBootstrap {
    private static Instrumentation instrumentation;
    @Getter
    private static Thread thread;

    public static void premain(String s, Instrumentation instrumentation) {
        WrapperBootstrap.instrumentation = instrumentation;
    }

    public static void main(String[] args) {
        var repo = Repository.build(Group.class).withId("groups").withLayer(MariaDbLayer.class).build();
        var service = new Service(repo.query().match("name", args[0]).findFirst(), args[1], Integer.parseInt(args[2]), ServiceState.STARTING);

        try {
            Files.copy(Path.of(System.getProperty("user.dir")).getParent().getParent().getParent().resolve("storage").resolve("jars").resolve("ECS-Plugin.jar"), service.getDirectory().resolve("plugins").resolve("ECS-Plugin.jar"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        thread = ApplicationExternalObjectLoader.init(service, instrumentation);

        new Wrapper(service.getId(), FileHelper.read(Path.of(System.getProperty("user.dir")).resolve("../../../storage/data/"), SecretConfiguration.class).value());
    }
}
