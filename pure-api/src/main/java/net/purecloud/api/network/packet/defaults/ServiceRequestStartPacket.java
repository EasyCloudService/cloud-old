package net.purecloud.api.network.packet.defaults;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.purecloud.api.network.NetworkBuf;
import net.purecloud.api.network.packet.Packet;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class ServiceRequestStartPacket implements Packet {
    private String groupName;
    private int count;

    @Override
    public void write(@NotNull NetworkBuf buf) {
        buf.writeString(groupName).writeInt(count);
    }

    @Override
    public void handle(NetworkBuf buf) {
        this.groupName = buf.readString();
        this.count = buf.readInt();
    }
}
