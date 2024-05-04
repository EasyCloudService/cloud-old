package net.easycloud.wrapper.user;

import lombok.Getter;
import net.bytemc.evelon.repository.Filter;
import net.bytemc.evelon.repository.Repository;
import net.easycloud.api.CloudDriver;
import net.easycloud.api.network.packet.defaults.PlayerConnectPacket;
import net.easycloud.api.network.packet.defaults.PlayerDisconnectPacket;
import net.easycloud.api.user.UserProvider;
import net.easycloud.api.user.CloudUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public final class UserHandler implements UserProvider {
    private final Repository<CloudUser> repository;
    private final Map<UUID, CloudUser> onlineCache;

    public UserHandler() {
        this.repository = Repository.create(CloudUser.class);
        this.onlineCache = new HashMap<>();
    }

    @Override
    public List<CloudUser> getUsers() {
        return repository.query().database().findAll();
    }

    @Override
    public List<CloudUser> getOnlineUsers() {
        return this.onlineCache.values().stream().toList();
    }

    @Override
    public CloudUser createUserIfNotExists(UUID uuid) {
        if(repository.query().filter(Filter.match("uniqueId", uuid)).database().exists()) {
            return repository.query().filter(Filter.match("uniqueId", uuid)).database().findFirst();
        }
        return null;
    }

    @Override
    public CloudUser getUser(UUID uuid) {
        onlineCache.forEach((uuid1, cloudUser) -> System.out.println(uuid1));
        System.out.println(onlineCache.size() + " " + uuid);
        if(!onlineCache.containsKey(uuid)) {
            var user = getOfflineUser(uuid);
            if(user != null) {
                onlineCache.put(uuid, user);
            }
        }
        return onlineCache.get(uuid);
    }

    @Override
    public CloudUser getOfflineUser(UUID uuid) {
        return repository.query().filter(Filter.match("uniqueId", uuid)).database().findFirst();
    }
}
