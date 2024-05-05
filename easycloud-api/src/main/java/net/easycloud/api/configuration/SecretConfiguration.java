package net.easycloud.api.configuration;

import net.easycloud.api.configuration.file.FileName;

@FileName(name = "secret")
public record SecretConfiguration(String value) {
}
