package net.easycloud.wrapper.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.easycloud.api.group.Group;
import net.easycloud.api.service.IService;
import net.easycloud.api.service.state.ServiceState;

import java.nio.file.Path;

@Getter
@AllArgsConstructor
@SuppressWarnings("ClassCanBeRecord")
public final class Service implements IService {
    private final Group group;
    private final String id;
    private final int port;

    @Setter
    private ServiceState state;

    @Override
    public ServiceState getState() {
        return this.state;
    }

    //TODO
    @Override
    public Path getDirectory() {
        return Path.of("");
    }
}
