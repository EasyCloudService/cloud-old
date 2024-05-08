package net.easycloud.api.group;

import dev.httpmarco.evelon.Repository;

public interface GroupProvider {
    Repository<Group> getRepository();

    Group getOrThrow(String name);
    void create(Group group);
}
