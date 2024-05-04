package net.easycloud.base.user;

import lombok.Getter;
import net.bytemc.evelon.repository.Filter;
import net.bytemc.evelon.repository.Repository;
import net.easycloud.api.network.packet.defaults.PlayerConnectPacket;
import net.easycloud.api.network.packet.defaults.PlayerDisconnectPacket;
import net.easycloud.api.user.UserProvider;
import net.easycloud.api.user.CloudUser;
import net.easycloud.base.Base;

import java.util.*;

public final class UserHandler implements UserProvider {
    @Getter
    private final Repository<CloudUser> repository;
    private final Map<UUID, CloudUser> onlineUsers;

    public UserHandler() {
        this.repository = Repository.create(CloudUser.class);
        this.onlineUsers = new HashMap<>();

        Base.getInstance().getNettyProvider().getPacketHandler().subscribe(PlayerConnectPacket.class, (channel, packet) -> {
            var user = createUserIfNotExists(packet.getUniqueId());
            if(user == null) {
                user = getOfflineUser(packet.getUniqueId());
            }
            this.onlineUsers.put(packet.getUniqueId(), user);
        });
        Base.getInstance().getNettyProvider().getPacketHandler().subscribe(PlayerDisconnectPacket.class, (channel, packet) -> {
            this.onlineUsers.remove(packet.getUniqueId());
        });
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
    public CloudUser createUserIfNotExists(UUID uuid) {
        if(repository.query().filter(Filter.match("uniqueId", uuid)).database().exists()) {
            return repository.query().filter(Filter.match("uniqueId", uuid)).database().findFirst();
        }
        return null;
    }

    @Override
    public CloudUser getUser(UUID uuid) {
        return onlineUsers.get(uuid);
    }

    @Override
    public CloudUser getOfflineUser(UUID uuid) {
        return repository.query().filter(Filter.match("uniqueId", uuid)).database().findFirst();
    }
}
