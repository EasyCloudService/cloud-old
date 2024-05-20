package net.easycloud.api.group;

import dev.httpmarco.evelon.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.easycloud.api.group.misc.GroupType;
import net.easycloud.api.group.misc.GroupVersion;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public final class Group {
    @PrimaryKey
    private final UUID uniqueId;
    private final String name;

    private int maxMemory;
    private int minOnline;
    private int maxOnline;
    private final int maxPlayers;

    private boolean staticService;
    private String material;
    private final GroupType type;
    private final GroupVersion version;
}
