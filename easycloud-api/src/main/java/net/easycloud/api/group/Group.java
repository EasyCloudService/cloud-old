package net.easycloud.api.group;

import de.flxwdns.oraculusdb.misc.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.easycloud.api.group.misc.GroupType;
import net.easycloud.api.group.misc.GroupVersion;

@Getter
@Setter
@AllArgsConstructor
public final class Group {
    @PrimaryKey
    private final String name;
    private final int maxMemory;
    private final int minOnline;
    private final int maxOnline;
    private final int maxPlayers;

    private String material;
    private final GroupType type;
    private final GroupVersion version;
}
