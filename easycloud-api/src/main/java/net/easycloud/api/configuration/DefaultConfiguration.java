package net.easycloud.api.configuration;

import net.bytemc.evelon.cradinates.DatabaseCradinates;
import net.easycloud.api.configuration.file.FileName;

@FileName(name = "config")
public record DefaultConfiguration(DatabaseCradinates database, String adminKey) {
}
