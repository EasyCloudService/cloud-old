package net.easycloud.wrapper.user;

import lombok.Getter;
import net.bytemc.evelon.repository.Filter;
import net.bytemc.evelon.repository.Repository;
import net.easycloud.api.user.UserProvider;
import net.easycloud.api.user.CloudUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class UserHandler implements UserProvider {
    @Getter
    private final Repository<CloudUser> repository;
    private final Map<UUID, CloudUser> onlineUsers;

    public UserHandler() {
        this.repository = Repository.create(CloudUser.class);
        this.onlineUsers = new HashMap<>();
    }

    @Override
    public List<CloudUser> getUsers() {
        return repository.query().database().findAll();
    }

    @Override
    public List<CloudUser> getOnlineUsers() {
        return this.onlineUsers.values().stream().toList();
    }

    @Override
    public void removeUser(UUID uuid) {
        this.onlineUsers.remove(uuid);
    }

    @Override
    public CloudUser createUserIfNotExists(UUID uuid) {
        if(!repository.query().filter(Filter.match("uniqueId", uuid)).database().exists()) {
            var user = new CloudUser(uuid, "");
            repository.query().create(user);
            return user;
        }
        return null;
    }

    @Override
    public CloudUser getUser(UUID uuid) {
        if(!onlineUsers.containsKey(uuid)) {
            var user = getOfflineUser(uuid);
            if(user != null) {
                onlineUsers.put(uuid, user);
            }
        }
        return onlineUsers.get(uuid);
    }

    @Override
    public CloudUser getOfflineUser(UUID uuid) {
        return repository.query().filter(Filter.match("uniqueId", uuid)).database().findFirst();
    }
}
