package net.easycloud.api.configuration;

import net.easycloud.api.utils.file.FileName;

@FileName(name = "secret")
public record SecretConfiguration(String value) {
}
