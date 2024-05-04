package net.easycloud.api.network.packet.defaults;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytemc.evelon.repository.Filter;
import net.bytemc.evelon.repository.Repository;
import net.easycloud.api.group.Group;
import net.easycloud.api.network.NetworkBuf;
import net.easycloud.api.network.packet.Packet;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class PlayerConnectPacket implements Packet {
    private UUID uniqueId;

    @Override
    public void write(@NotNull NetworkBuf buf) {
        buf.writeUUID(uniqueId);
    }

    @Override
    public void handle(NetworkBuf buf) {
        this.uniqueId = buf.readUUID();
    }
}
