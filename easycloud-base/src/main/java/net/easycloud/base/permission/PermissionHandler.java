package net.easycloud.base.permission;

import lombok.Getter;
import net.bytemc.evelon.repository.Filter;
import net.bytemc.evelon.repository.Repository;
import net.easycloud.api.permission.PermissionProvider;
import net.easycloud.api.permission.PermissionUser;

import java.util.List;
import java.util.UUID;

@Getter
public final class PermissionHandler implements PermissionProvider {
    private final Repository<PermissionUser> repository;

    public PermissionHandler() {
        this.repository = Repository.create(PermissionUser.class);
    }

    @Override
    public List<PermissionUser> getUsers() {
        return repository.query().database().findAll();
    }

    @Override
    public PermissionUser getUser(UUID uuid) {
        return repository.query().filter(Filter.match("uuid", uuid)).database().findFirst();
    }
}
