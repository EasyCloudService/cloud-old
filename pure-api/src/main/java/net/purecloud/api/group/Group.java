package net.purecloud.api.group;

import de.flxwdns.oraculusdb.misc.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.purecloud.api.group.misc.GroupType;
import net.purecloud.api.group.misc.GroupVersion;

@Getter
@AllArgsConstructor
public final class Group {
    @PrimaryKey
    private final String name;
    private final int maxMemory;
    private final int minOnline;
    private final int maxOnline;
    private final int maxPlayers;

    private final GroupType type;
    private final GroupVersion version;
}
