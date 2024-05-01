package net.easycloud.api.network.packet;

import net.easycloud.api.network.NetworkBuf;

import java.io.Serializable;

public interface Packet extends Serializable {
    void handle(NetworkBuf buf);
    void write(NetworkBuf buf);
}
