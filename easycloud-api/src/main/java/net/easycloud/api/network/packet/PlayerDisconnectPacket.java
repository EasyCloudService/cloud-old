package net.easycloud.api.network.packet;

import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import lombok.Getter;

import java.util.UUID;

@Getter
public final class PlayerDisconnectPacket extends Packet {
    private UUID uniqueId;

    public PlayerDisconnectPacket(UUID uniqueId) {
        this.uniqueId = uniqueId;

        this.getBuffer().writeUniqueId(this.uniqueId);
    }

    public PlayerDisconnectPacket(CodecBuffer buffer) {
        super(buffer);

        this.uniqueId = buffer.readUniqueId();
    }
}
