package net.easycloud.api.network.packet;

import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import lombok.Getter;
import net.easycloud.api.group.Group;
import net.easycloud.api.group.misc.GroupType;
import net.easycloud.api.group.misc.GroupVersion;

@Getter
@SuppressWarnings("ALL")
public final class GroupCreatePacket extends Packet {
    private Group group;

    public GroupCreatePacket(Group group) {
        this.group = group;

        this.getBuffer().writeUniqueId(this.group.getUniqueId());
        this.getBuffer().writeString(this.group.getName());
        this.getBuffer().writeInt(this.group.getMaxMemory());
        this.getBuffer().writeInt(this.group.getMinOnline());
        this.getBuffer().writeInt(this.group.getMaxOnline());
        this.getBuffer().writeInt(this.group.getMaxPlayers());
        this.getBuffer().writeBoolean(this.group.isStaticService());
        this.getBuffer().writeString(this.group.getMaterial());
        this.getBuffer().writeEnum(this.group.getType());
        this.getBuffer().writeEnum(this.group.getVersion());
    }

    public GroupCreatePacket(CodecBuffer buffer) {
        super(buffer);

        this.group = new Group(buffer.readUniqueId(), buffer.readString(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readBoolean(), buffer.readString(), buffer.readEnum(GroupType.class), buffer.readEnum(GroupVersion.class));
    }
}
