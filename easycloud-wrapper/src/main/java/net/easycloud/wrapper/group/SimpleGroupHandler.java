package net.easycloud.wrapper.group;

import dev.httpmarco.evelon.MariaDbLayer;
import dev.httpmarco.evelon.Repository;
import lombok.Getter;
import net.easycloud.api.group.Group;
import net.easycloud.api.group.GroupProvider;

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
    //TODO
    public void create(Group group) {

    }
}
