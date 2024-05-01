package net.easycloud.api.conf;

import de.flxwdns.oraculusdb.sql.DatabaseCredentials;
import net.http.aeon.annotations.Options;

@Options(name = "config")
public record DefaultConfiguration(DatabaseCredentials database) {
}
