package net.easycloud.api.network.packet;

import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import lombok.Getter;

@Getter
public final class ServiceRequestStopPacket extends Packet {
    private String serviceId;

    public ServiceRequestStopPacket(String serviceId) {
        this.serviceId = serviceId;

        this.getBuffer().writeString(this.serviceId);
    }

    public ServiceRequestStopPacket(CodecBuffer buffer) {
        super(buffer);

        this.serviceId = buffer.readString();
    }
}
