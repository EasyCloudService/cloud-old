package net.easycloud.wrapper.group;

import dev.httpmarco.evelon.MariaDbLayer;
import dev.httpmarco.evelon.Repository;
import lombok.Getter;
import net.easycloud.api.group.Group;
import net.easycloud.api.group.GroupProvider;
import net.easycloud.api.group.misc.GroupType;
import net.easycloud.api.misc.DownloadHelper;
import net.easycloud.api.misc.Reflections;
import net.easycloud.api.network.packet.GroupCreatePacket;
import net.easycloud.wrapper.Wrapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Getter
public final class SimpleGroupHandler implements GroupProvider {
    private final Repository<Group> repository;

    public SimpleGroupHandler() {
        this.repository = Repository.build(Group.class).withId("groups").withLayer(MariaDbLayer.class).build();
    }

    @Override
    public Group getOrThrow(String name) {
        return repository.query().match("name", name).findFirst();
    }

    @Override
    public void create(Group group) {
        Wrapper.getInstance().getNettyClient().sendPacket(new GroupCreatePacket(group));
    }
}
