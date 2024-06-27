package net.easycloud.wrapper.user;

import dev.httpmarco.evelon.MariaDbLayer;
import dev.httpmarco.evelon.Repository;
import lombok.Getter;
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
        this.repository = Repository.build(CloudUser.class).withId("users").withLayer(MariaDbLayer.class).build();;
        this.onlineUsers = new HashMap<>();
    }

    @Override
    public List<CloudUser> getUsers() {
        return repository.query().find();
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
        if(!repository.query().match("uniqueId", uuid).exists()) {
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
        return repository.query().match("uniqueId", uuid).findFirst();
    }
}
