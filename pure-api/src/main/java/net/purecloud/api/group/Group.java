package net.purecloud.api.group;

import de.flxwdns.oraculusdb.misc.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.purecloud.api.group.misc.GroupType;
import net.purecloud.api.group.misc.GroupVersion;

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
