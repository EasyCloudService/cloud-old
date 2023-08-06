package net.purecloud.wrapper.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.purecloud.api.group.Group;
import net.purecloud.api.service.IService;

import java.nio.file.Path;

@Getter
@AllArgsConstructor
public final class Service implements IService {
    private final Group group;
    private final String id;
    private final int port;

    @Override
    public Path getDirectory() {
        return Path.of("");
    }
}
