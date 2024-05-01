package net.easycloud.base.permission;

import de.flxwdns.oraculusdb.repository.Repository;
import lombok.Getter;
import net.easycloud.api.permission.PermissionProvider;
import net.easycloud.api.permission.PermissionUser;

import java.util.List;
import java.util.UUID;

@Getter
public final class PermissionHandler implements PermissionProvider {
    private final Repository<PermissionUser> repository;

    public PermissionHandler() {
        this.repository = new Repository<>(PermissionUser.class);
    }

    @Override
    public List<PermissionUser> getUsers() {
        return repository.findAll();
    }

    @Override
    public PermissionUser getUser(UUID uuid) {
        return repository.filter().value("uuid", uuid).complete().findFirst().orElse(null);
    }
}
