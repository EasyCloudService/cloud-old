package net.easycloud.api.user;

import dev.httpmarco.evelon.Repository;

import java.util.List;
import java.util.UUID;

public interface UserProvider {
    Repository<CloudUser> getRepository();
    List<CloudUser> getUsers();
    List<CloudUser> getOnlineUsers();

    void removeUser(UUID uuid);
    CloudUser createUserIfNotExists(UUID uuid);
    CloudUser getUser(UUID uuid);
    CloudUser getOfflineUser(UUID uuid);
}
