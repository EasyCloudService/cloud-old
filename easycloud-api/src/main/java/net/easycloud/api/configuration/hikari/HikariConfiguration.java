package net.easycloud.api.configuration.hikari;

import dev.httpmarco.evelon.layer.connection.ConnectionAuthentication;
import lombok.Getter;
import net.easycloud.api.utils.file.FileName;

@Getter
@FileName(name = "evelon-connection-credentials")
public class HikariConfiguration extends ConnectionAuthentication {
    private final String hostname;
    private final String database;
    private final String username;
    private final String password;
    private final int port;

    public HikariConfiguration(String hostname, String database, String username, String password, int port) {
        super("MARIADB", true);
        this.hostname = hostname;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
    }
}
