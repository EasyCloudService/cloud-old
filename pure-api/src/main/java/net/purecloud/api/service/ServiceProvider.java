package net.purecloud.api.service;

import net.purecloud.api.group.Group;

import java.util.List;

public interface ServiceProvider {
    List<IService> getServices();

    void start(Group group, int count);
    void stop(String id);
}
