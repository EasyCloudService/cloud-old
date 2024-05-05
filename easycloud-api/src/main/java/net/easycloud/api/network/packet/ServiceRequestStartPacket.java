package net.easycloud.api.network.packet;

import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import lombok.Getter;

@Getter
public final class ServiceRequestStartPacket extends Packet {
    private String groupName;
    private int count;

    public ServiceRequestStartPacket(String groupName, int count) {
        this.groupName = groupName;
        this.count = count;

        this.getBuffer().writeString(this.groupName).writeInt(count);
    }

    public ServiceRequestStartPacket(CodecBuffer buffer) {
        super(buffer);

        this.groupName = buffer.readString();
        this.count = buffer.readInt();
    }
}
