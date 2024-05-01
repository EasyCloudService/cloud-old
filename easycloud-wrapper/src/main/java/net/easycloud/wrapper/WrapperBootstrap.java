package net.easycloud.wrapper;

import lombok.Getter;
import net.bytemc.evelon.DatabaseProtocol;
import net.bytemc.evelon.Evelon;
import net.bytemc.evelon.cradinates.DatabaseCradinates;
import net.bytemc.evelon.repository.Filter;
import net.bytemc.evelon.repository.Repository;
import net.http.aeon.Aeon;
import net.easycloud.api.conf.DefaultConfiguration;
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
        var configuration = Aeon.insert(new DefaultConfiguration(new DatabaseCradinates(DatabaseProtocol.H2, "127.0.0.1", "test123", "root", "cloud", 3306)), Path.of(System.getProperty("user.dir")).resolve("../../../"));
        Evelon.setCradinates(configuration.database());


        var repo = Repository.create(Group.class);
        var service = new Service(repo.query().filter(Filter.match("name", args[0])).database().findFirst(), args[1], Integer.parseInt(args[2]));

        try {
            Files.copy(Path.of(System.getProperty("user.dir")).getParent().getParent().getParent().resolve("storage").resolve("plugin.jar"), service.getDirectory().resolve("plugins").resolve("Cloud-API.jar"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        thread = ApplicationExternalObjectLoader.init(service, instrumentation);

        new Wrapper(service.getId());
    }
}
