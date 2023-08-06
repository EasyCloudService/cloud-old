package net.purecloud.api.group;

import de.flxwdns.oraculusdb.repository.Repository;

public interface GroupProvider {
    Repository<Group> getRepository();

    Group getOrThrow(String name);
    void create(Group group);
}
