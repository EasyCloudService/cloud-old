package net.easycloud.api.network.packet.defaults;

import de.flxwdns.oraculusdb.repository.Repository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
