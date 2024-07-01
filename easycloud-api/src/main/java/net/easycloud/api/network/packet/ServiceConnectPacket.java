package net.easycloud.api.network.packet;

import dev.httpmarco.evelon.Repository;
import dev.httpmarco.evelon.sql.h2.H2Layer;
import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import lombok.Getter;
import net.easycloud.api.group.Group;

@Getter
@SuppressWarnings("ALL")
public final class ServiceConnectPacket extends Packet {
    private Group group;
    private String name;
    private int port;

    public ServiceConnectPacket(Group group, String name, int port) {
        this.group = group;
        this.name = name;
        this.port = port;

        this.getBuffer().writeString(this.group.getName()).writeString(this.name).writeInt(this.port);
    }

    public ServiceConnectPacket(CodecBuffer buffer) {
        super(buffer);

        var repo = Repository.build(Group.class).withId("groups").withLayer(H2Layer.class).build();
        this.group = repo.query().match("name", buffer.readString()).findFirst();
        this.name = buffer.readString();
        this.port = buffer.readInt();
    }
}
