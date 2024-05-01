package net.easycloud.api.conf;

import net.bytemc.evelon.cradinates.DatabaseCradinates;
import net.http.aeon.annotations.Comment;
import net.http.aeon.annotations.Options;

@Options(name = "config")
public record DefaultConfiguration(@Comment(comment = "MARIADB, MYSQL, H2, POSTGRESQL, MARIADB") DatabaseCradinates database) {
}
