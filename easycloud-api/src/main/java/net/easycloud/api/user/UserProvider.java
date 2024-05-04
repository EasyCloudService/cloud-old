package net.easycloud.api.user;

import net.bytemc.evelon.repository.Repository;

import java.util.List;
import java.util.UUID;

public interface UserProvider {
    Repository<CloudUser> getRepository();
    List<CloudUser> getUsers();
    List<CloudUser> getOnlineUsers();

    CloudUser createUserIfNotExists(UUID uuid);
    CloudUser getUser(UUID uuid);
    CloudUser getOfflineUser(UUID uuid);
}
