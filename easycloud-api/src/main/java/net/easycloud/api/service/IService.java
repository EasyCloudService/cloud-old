package net.easycloud.api.service;

import net.easycloud.api.group.Group;

import java.nio.file.Path;

public interface IService {
    Group getGroup();
    int getPort();
    String getId();
    Path getDirectory();
}
