package net.easycloud.api.permission;

import net.bytemc.evelon.repository.Repository;

import java.util.List;
import java.util.UUID;

public interface PermissionProvider {
    Repository<PermissionUser> getRepository();
    List<PermissionUser> getUsers();

    PermissionUser getUser(UUID uuid);
}
