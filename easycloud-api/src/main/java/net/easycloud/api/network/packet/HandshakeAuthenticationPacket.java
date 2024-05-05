package net.easycloud.api.network.packet;

import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import lombok.Getter;

@Getter
public final class HandshakeAuthenticationPacket extends Packet {
    private String name;
    private String key;

    public HandshakeAuthenticationPacket(String name, String key) {
        this.name = name;
        this.key = key;

        this.getBuffer().writeString(this.name).writeString(this.key);
    }

    public HandshakeAuthenticationPacket(CodecBuffer buffer) {
        super(buffer);

        this.name = buffer.readString();
        this.key = buffer.readString();
    }
}
