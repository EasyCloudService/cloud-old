package net.easycloud.api.service;

import net.easycloud.api.group.Group;

import java.util.List;

public interface ServiceProvider {
    List<IService> getServices();

    void start(Group group, int count);
    void stop(String id);

    IService getCurrentService();
}
