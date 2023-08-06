package net.purecloud.api.group.misc;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum GroupType {
    SERVER(List.of("--nogui", "--online-mode=false")),
    PROXY(List.of());

    private final List<String> arguments;
}
