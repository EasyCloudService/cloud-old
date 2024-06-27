package net.easycloud.api.service;

import net.easycloud.api.group.Group;
import net.easycloud.api.service.state.ServiceState;

import java.nio.file.Path;

public interface IService {
    Group getGroup();
    String getId();
    int getPort();

    ServiceState getState();
    Path getDirectory();
}
