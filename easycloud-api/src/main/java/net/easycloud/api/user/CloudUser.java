package net.easycloud.api.user;

import dev.httpmarco.evelon.PrimaryKey;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.easycloud.api.CloudDriver;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public final class CloudUser {
    @PrimaryKey
    private final UUID uniqueId;

    private void update() {
        CloudDriver.instance().userProvider().getRepository().query().update(this);
    }
}
