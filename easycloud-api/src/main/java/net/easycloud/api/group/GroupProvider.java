package net.easycloud.api.group;

import net.bytemc.evelon.repository.Repository;

public interface GroupProvider {
    Repository<Group> getRepository();

    Group getOrThrow(String name);
    void create(Group group);
}
