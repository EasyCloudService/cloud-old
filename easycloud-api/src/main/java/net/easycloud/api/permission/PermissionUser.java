package net.easycloud.api.permission;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.evelon.repository.Filter;
import net.bytemc.evelon.repository.annotations.PrimaryKey;
import net.easycloud.api.CloudDriver;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public final class PermissionUser {
    @PrimaryKey
    private final UUID uuid;

    @Getter(AccessLevel.NONE)
    private String permissions;

    public void addPermission(String permission) {
        if(permissions.isEmpty()) {
            permissions = permission.toLowerCase();
            update();
            return;
        }

        if(permissions.contains(", " + permission.toLowerCase()) || (permissions.equals(permission.toLowerCase()) || permissions.startsWith(permission.toLowerCase() + ","))) {
            return;
        }

        permissions = permissions + ", " + permission.toLowerCase();
        update();
    }

    public void removePermission(String permission) {
        permissions = permissions.replace(permission.toLowerCase() + ", ", "").replace(permission.toLowerCase(), "");
        update();
    }

    private void update() {
        CloudDriver.getInstance().getPermissionProvider().getRepository().query().filter(Filter.match("uuid", uuid)).database().update(this);
    }

    public List<String> getPermissions() {
        return Arrays.stream(permissions.split(", ")).toList();
    }
}
