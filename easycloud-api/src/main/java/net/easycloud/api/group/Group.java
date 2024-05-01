package net.easycloud.api.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.bytemc.evelon.repository.annotations.Entity;
import net.bytemc.evelon.repository.annotations.PrimaryKey;
import net.easycloud.api.group.misc.GroupType;
import net.easycloud.api.group.misc.GroupVersion;

@Getter
@Setter
@AllArgsConstructor
@Entity(name = "serviceGroup")
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
