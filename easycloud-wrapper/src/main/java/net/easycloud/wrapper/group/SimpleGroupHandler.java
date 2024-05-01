package net.easycloud.wrapper.group;

import lombok.Getter;
import net.bytemc.evelon.repository.Filter;
import net.bytemc.evelon.repository.Repository;
import net.easycloud.api.group.Group;
import net.easycloud.api.group.GroupProvider;

@Getter
public final class SimpleGroupHandler implements GroupProvider {
    private final Repository<Group> repository;

    public SimpleGroupHandler() {
        this.repository = Repository.create(Group.class);
    }

    @Override
    public Group getOrThrow(String name) {
        return repository.query().filter(Filter.match("name", name))
                .database()
                .findFirst();
    }

    @Override
    //TODO
    public void create(Group group) {

    }
}
