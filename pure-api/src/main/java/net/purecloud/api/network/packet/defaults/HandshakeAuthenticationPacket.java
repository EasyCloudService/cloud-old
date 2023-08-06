package net.purecloud.api.network.packet.defaults;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.purecloud.api.network.NetworkBuf;
import net.purecloud.api.network.packet.Packet;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class HandshakeAuthenticationPacket implements Packet {
    private String key;

    @Override
    public void handle(NetworkBuf buf) {
        this.key = buf.readString();
    }

    @Override
    public void write(NetworkBuf byteBuf) {
        byteBuf.writeString(this.key);
    }
}
