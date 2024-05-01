package net.easycloud.api.network.packet.defaults;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytemc.evelon.repository.Filter;
import net.bytemc.evelon.repository.Repository;
import net.easycloud.api.group.Group;
import net.easycloud.api.network.NetworkBuf;
import net.easycloud.api.network.packet.Packet;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class ServiceDisconnectPacket implements Packet {
    private Group group;
    private String name;
    private int port;

    @Override
    public void write(NetworkBuf buf) {
        buf.writeString(group.getName()).writeString(name).writeInt(port);
    }

    @Override
    public void handle(NetworkBuf buf) {
        var repo = Repository.create(Group.class);
        this.group = repo.query().filter(Filter.match("name", buf.readString()))
                .database()
                .findFirst();
        this.name = buf.readString();
        this.port = buf.readInt();
    }
}
