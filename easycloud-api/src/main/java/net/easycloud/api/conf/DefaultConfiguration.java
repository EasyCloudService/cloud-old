package net.easycloud.api.conf;

import net.bytemc.evelon.cradinates.DatabaseCradinates;

@Options(name = "config")
public record DefaultConfiguration(DatabaseCradinates database) {
}
