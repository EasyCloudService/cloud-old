package net.easycloud.api.network.packet;

import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import lombok.Getter;

@Getter
public final class HandshakeAuthenticationPacket extends Packet {
    private String key;

    public HandshakeAuthenticationPacket(String key) {
        this.key = key;

        this.getBuffer().writeString(this.key);
    }

    public HandshakeAuthenticationPacket(CodecBuffer buffer) {
        super(buffer);

        this.key = buffer.readString();
    }
}
