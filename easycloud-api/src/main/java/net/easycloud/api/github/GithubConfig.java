package net.easycloud.api.github;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.easycloud.api.utils.file.FileName;

@Getter
@AllArgsConstructor
@FileName(name = "github")
@SuppressWarnings("ClassCanBeRecord")
public final class GithubConfig {
    private final String version;
}
