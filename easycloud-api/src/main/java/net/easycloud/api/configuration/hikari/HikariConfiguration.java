package net.easycloud.api.configuration.hikari;

import dev.httpmarco.evelon.layer.connection.ConnectionAuthentication;
import lombok.Getter;
import net.easycloud.api.utils.file.FileName;

import java.nio.file.Path;

@Getter
@FileName(name = "sql")
public class HikariConfiguration extends ConnectionAuthentication {
    private final String path;

    public HikariConfiguration() {
        super("H2", true);
        this.path = Path.of("storage/database.h2").toAbsolutePath().toString();
    }
}

