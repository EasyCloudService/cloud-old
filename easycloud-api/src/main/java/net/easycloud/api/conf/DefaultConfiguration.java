package net.easycloud.api.conf;

import net.bytemc.evelon.cradinates.DatabaseCradinates;

@FileName(name = "config")
public record DefaultConfiguration(DatabaseCradinates database, String adminKey) {
}
