package net.easycloud.api.group.misc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupVersion {
    PAPER_1_20_1("https://api.papermc.io/v2/projects/paper/versions/1.20.1/builds/95/downloads/paper-1.20.1-95.jar"),
    PAPER_1_20_4("https://api.papermc.io/v2/projects/paper/versions/1.20.4/builds/381/downloads/paper-1.20.4-381.jar"),
    VELOCITY("https://api.papermc.io/v2/projects/velocity/versions/3.3.0-SNAPSHOT/builds/320/downloads/velocity-3.3.0-SNAPSHOT-320.jar");

    private final String url;
}
