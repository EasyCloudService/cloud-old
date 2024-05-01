package net.easycloud.wrapper.group;

import de.flxwdns.oraculusdb.repository.Repository;
import lombok.Getter;
import net.easycloud.api.group.Group;
import net.easycloud.api.group.GroupProvider;

@Getter
public final class SimpleGroupHandler implements GroupProvider {
    private final Repository<Group> repository;

    public SimpleGroupHandler() {
        this.repository = new Repository<>(Group.class);
    }

    @Override
    public Group getOrThrow(String name) {
        return repository.filter()
                .value("name", name)
                .complete()
                .findFirst().orElseThrow(null);
    }

    @Override
    //TODO
    public void create(Group group) {

    }
}
