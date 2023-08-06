package net.purecloud.api.network.packet;

import net.purecloud.api.network.NetworkBuf;

import java.io.Serializable;

public interface Packet extends Serializable {
    void handle(NetworkBuf buf);
    void write(NetworkBuf buf);
}
