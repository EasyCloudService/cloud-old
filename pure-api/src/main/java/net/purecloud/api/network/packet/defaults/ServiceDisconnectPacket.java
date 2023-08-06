package net.purecloud.api.network.packet.defaults;

import de.flxwdns.oraculusdb.repository.Repository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.purecloud.api.group.Group;
import net.purecloud.api.network.NetworkBuf;
import net.purecloud.api.network.packet.Packet;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class ServiceDisconnectPacket implements Packet {
    private String name;
    private Group group;
    private int port;

    @Override
    public void write(NetworkBuf buf) {
        buf.writeString(name).writeString(group.getName()).writeInt(port);
    }

    @Override
    public void handle(NetworkBuf buf) {
        var repo = new Repository<>(Group.class);
        this.group = repo.filter()
                .value("name", buf.readString())
                .complete()
                .findFirst()
                .orElseThrow();
        this.name = buf.readString();
        this.port = buf.readInt();
    }
}
