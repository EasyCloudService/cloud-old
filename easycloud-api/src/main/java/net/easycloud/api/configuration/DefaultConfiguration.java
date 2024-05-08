package net.easycloud.api.configuration;

import net.easycloud.api.utils.file.FileName;

@FileName(name = "config")
public record DefaultConfiguration(String adminKey) {
}
