package net.easycloud.api.network.packet.defaults;

import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import lombok.Getter;
import net.bytemc.evelon.repository.Filter;
import net.bytemc.evelon.repository.Repository;
import net.easycloud.api.group.Group;

@Getter
public final class ServiceDisconnectPacket extends Packet {
    private Group group;
    private String name;
    private int port;

    public ServiceDisconnectPacket(Group group, String name, int port) {
        this.group = group;
        this.name = name;
        this.port = port;

        this.getBuffer().writeString(this.group.getName()).writeString(this.name).writeInt(this.port);
    }

    public ServiceDisconnectPacket(CodecBuffer buffer) {
        super(buffer);

        var repo = Repository.create(Group.class);
        this.group = repo.query().filter(Filter.match("name", buffer.readString()))
                .database()
                .findFirst();
        this.name = buffer.readString();
        this.port = buffer.readInt();
    }
}
