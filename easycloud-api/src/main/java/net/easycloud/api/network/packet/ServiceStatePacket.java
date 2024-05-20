package net.easycloud.api.network.packet;

import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import lombok.Getter;
import net.easycloud.api.service.state.ServiceState;

@Getter
@SuppressWarnings("ALL")
public final class ServiceStatePacket extends Packet {
    private String name;
    private ServiceState state;

    public ServiceStatePacket(String name, ServiceState state) {
        this.name = name;
        this.state = state;

        this.getBuffer().writeString(this.name).writeEnum(this.state);
    }

    public ServiceStatePacket(CodecBuffer buffer) {
        super(buffer);

        this.name = buffer.readString();
        this.state = buffer.readEnum(ServiceState.class);
    }
}
