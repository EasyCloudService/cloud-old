package net.easycloud.api.network.packet;

import dev.httpmarco.evelon.MariaDbLayer;
import dev.httpmarco.evelon.Repository;
import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import lombok.Getter;
import net.easycloud.api.group.Group;

@Getter
@SuppressWarnings("ALL")
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

        var repo = Repository.build(Group.class).withId("groups").withLayer(MariaDbLayer.class).build();
        this.group = repo.query().match("name", buffer.readString()).findFirst();
        this.name = buffer.readString();
        this.port = buffer.readInt();
    }
}
