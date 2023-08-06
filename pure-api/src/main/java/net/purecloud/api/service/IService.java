package net.purecloud.api.service;

import net.purecloud.api.group.Group;

import java.nio.file.Path;

public interface IService {
    Group getGroup();
    int getPort();
    String getId();
    Path getDirectory();
}
