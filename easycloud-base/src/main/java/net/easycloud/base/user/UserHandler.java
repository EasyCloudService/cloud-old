package net.easycloud.base.user;

import dev.httpmarco.evelon.Repository;
import dev.httpmarco.evelon.sql.h2.H2Layer;
import lombok.Getter;
import net.easycloud.api.network.packet.PlayerConnectPacket;
import net.easycloud.api.network.packet.PlayerDisconnectPacket;
import net.easycloud.api.user.UserProvider;
import net.easycloud.api.user.CloudUser;
import net.easycloud.base.Base;

import java.util.*;

public final class UserHandler implements UserProvider {
    @Getter
    private final Repository<CloudUser> repository;
    private final Map<UUID, CloudUser> onlineUsers;

    public UserHandler() {
        this.repository = Repository.build(CloudUser.class).withId("users").withLayer(H2Layer.class).build();
        this.onlineUsers = new HashMap<>();

        Base.instance().nettyServer().listen(PlayerConnectPacket.class, (channel, packet) -> {
            var user = createUserIfNotExists(packet.getUniqueId());
            if(user == null) {
                user = getOfflineUser(packet.getUniqueId());
            }
            this.onlineUsers.put(packet.getUniqueId(), user);
        });
        Base.instance().nettyServer().listen(PlayerDisconnectPacket.class, (channel, packet) -> {
            this.onlineUsers.remove(packet.getUniqueId());
        });
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
            var user = new CloudUser(uuid);
            repository.query().create(user);
            return user;
        }
        return null;
    }

    @Override
    public CloudUser getUser(UUID uuid) {
        return onlineUsers.get(uuid);
    }

    @Override
    public CloudUser getOfflineUser(UUID uuid) {
        return repository.query().match("uniqueId", uuid).findFirst();
    }
}
