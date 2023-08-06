package net.purecloud.wrapper;

import de.flxwdns.oraculusdb.repository.Repository;
import de.flxwdns.oraculusdb.sql.DatabaseCredentials;
import de.flxwdns.oraculusdb.sql.query.ConnectionQueryHelper;
import lombok.Getter;
import net.http.aeon.Aeon;
import net.purecloud.api.conf.DefaultConfiguration;
import net.purecloud.api.group.Group;
import net.purecloud.wrapper.classloader.ApplicationExternalObjectLoader;
import net.purecloud.wrapper.service.Service;

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
        var configuration = Aeon.insert(new DefaultConfiguration(new DatabaseCredentials("127.0.0.1", 3306, "cloud", "root", "test123")), Path.of(System.getProperty("user.dir")).resolve("../../../"));
        ConnectionQueryHelper.init(configuration.database());

        var repo = new Repository<>(Group.class);
        var service = new Service(repo.filter().value("name", args[0]).complete().findFirst().orElseThrow(), args[1], Integer.parseInt(args[2]));

        try {
            Files.copy(Path.of(System.getProperty("user.dir")).getParent().getParent().getParent().resolve("storage").resolve("plugin.jar"), service.getDirectory().resolve("plugins").resolve("Cloud-API.jar"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        thread = ApplicationExternalObjectLoader.init(service, instrumentation);

        new Wrapper(service.getId());
    }
}
