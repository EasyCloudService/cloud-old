package net.easycloud.api.network.packet;

import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import lombok.Getter;

import java.util.UUID;

@Getter
@SuppressWarnings("ALL")
public final class PlayerConnectPacket extends Packet {
    private UUID uniqueId;

    public PlayerConnectPacket(UUID uniqueId) {
        this.uniqueId = uniqueId;

        this.getBuffer().writeUniqueId(this.uniqueId);
    }

    public PlayerConnectPacket(CodecBuffer buffer) {
        super(buffer);

        this.uniqueId = buffer.readUniqueId();
    }
}
