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
public final class ServiceRequestStopPacket implements Packet {
    private String serviceId;

    @Override
    public void write(@NotNull NetworkBuf buf) {
        buf.writeString(serviceId);
    }

    @Override
    public void handle(NetworkBuf buf) {
        this.serviceId = buf.readString();
    }
}
