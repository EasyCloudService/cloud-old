package net.easycloud.wrapper.group;

import dev.httpmarco.evelon.Repository;
import dev.httpmarco.evelon.sql.h2.H2Layer;
import lombok.Getter;
import net.easycloud.api.group.Group;
import net.easycloud.api.group.GroupProvider;
import net.easycloud.api.network.packet.GroupCreatePacket;
import net.easycloud.wrapper.Wrapper;

@Getter
public final class SimpleGroupHandler implements GroupProvider {
    private final Repository<Group> repository;

    public SimpleGroupHandler() {
        this.repository = Repository.build(Group.class).withId("groups").withLayer(H2Layer.class).build();
    }

    @Override
    public Group getOrThrow(String name) {
        return repository.query().match("name", name).findFirst();
    }

    @Override
    public void create(Group group) {
        Wrapper.instance().nettyClient().sendPacket(new GroupCreatePacket(group));
    }
}
