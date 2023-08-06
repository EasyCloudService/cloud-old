package net.purecloud.api.group.misc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupVersion {
    PAPER_1_20_1("https://api.papermc.io/v2/projects/paper/versions/1.20.1/builds/95/downloads/paper-1.20.1-95.jar"),
    VELOCITY("https://api.papermc.io/v2/projects/velocity/versions/3.2.0-SNAPSHOT/builds/260/downloads/velocity-3.2.0-SNAPSHOT-260.jar");

    private final String url;
}
