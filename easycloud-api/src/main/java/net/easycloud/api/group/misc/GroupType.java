package net.easycloud.api.group.misc;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum GroupType {
    SERVER("SERVER", List.of("--nogui", "--online-mode=false")),
    LOBBY("SERVER", List.of("--nogui", "--online-mode=false")),
    PROXY("PROXY", List.of());

    private final String folder;
    private final List<String> arguments;
}
